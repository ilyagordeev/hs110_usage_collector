package com.gordeev;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.gordeev.hs110.login.Login;
import com.gordeev.hs110.resources.PassthroughRequest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.TimeUnit;


public class Main {
    Gson gson = new Gson();
    Login login = new Login();

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        JsonObject emeter;
        while (true) {
            emeter = main.passthroughRequest("{\"emeter\":{\"get_realtime\": null}}}");
            System.out.print("Power: " + main.getRealtime(emeter, "power_mw"));
            System.out.print(" | Voltage: " + main.getRealtime(emeter, "voltage_mv"));
            System.out.print(" | Amperage: " + main.getRealtime(emeter, "current_ma"));
            System.out.println(" | Total w/h: " + main.getRealtime(emeter, "total_wh"));
            TimeUnit.SECONDS.sleep(3);
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

    public String getRealtime(JsonObject emeter, String request) {
        return emeter.getAsJsonObject("emeter")
                .getAsJsonObject("get_realtime")
                .get(request)
                .getAsString();
    }
}
