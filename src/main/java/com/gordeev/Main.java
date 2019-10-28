package com.gordeev;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gordeev.hs110.login.Login;
import com.gordeev.hs110.resources.PassthroughRequest;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Main {
    Gson gson = new Gson();
    Login login = new Login();
    static JsonObject emeter;

    public Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/julia", "julia", "hs110");
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void main(String[] args) throws InterruptedException, SQLException {
        Main main = new Main();

        String createSQL = "CREATE TABLE IF NOT EXISTS hs_emetr (time timestamp, power int, voltage int, amperage int, total_wh int);";
        try {
            Connection con = main.connect();
            con.createStatement().execute(createSQL);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO hs_emetr VALUES (now(), ?, ?, ?, ?);";
        PreparedStatement preparedStatement = main.connect().prepareStatement(sql);

        while (true) {
            emeter = main.passthroughRequest("{\"emeter\":{\"get_realtime\": null}}}");
            System.out.print("Power: " + getRealtime("power_mw"));
            System.out.print(" | Voltage: " + getRealtime("voltage_mv"));
            System.out.print(" | Amperage: " + getRealtime("current_ma"));
            System.out.println(" | Total w/h: " + getRealtime("total_wh"));

            preparedStatement.setInt(1, getRealtime("power_mw"));
            preparedStatement.setInt(2, getRealtime("voltage_mv"));
            preparedStatement.setInt(3, getRealtime("current_ma"));
            preparedStatement.setInt(4, getRealtime("total_wh"));
            preparedStatement.executeUpdate();
            Thread.sleep(3000);
        }
    }

    public JsonObject passthroughRequest(String requestData) {
        PassthroughRequest passthroughRequest = new PassthroughRequest(login.getDeviceId(), requestData);
        WebTarget target = login.getClient().target("https://wap.tplinkcloud.com/?token=" + login.getToken());
        String response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("cache-control", "no-cache")
                .header("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 6.0.1; A0001 Build/M4B30X)")
                .post(Entity.json(gson.toJson(passthroughRequest)), String.class);

        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        String responseData = jsonObject.getAsJsonObject("result").getAsJsonPrimitive("responseData").getAsString();
        jsonObject = gson.fromJson(responseData, JsonObject.class);

        return jsonObject;
    }

    public static int getRealtime(String request) {
        return emeter.getAsJsonObject("emeter")
                .getAsJsonObject("get_realtime")
                .get(request)
                .getAsInt();
    }
}
