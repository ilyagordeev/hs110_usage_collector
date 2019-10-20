package com.gordeev.hs110.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.aeonbits.owner.ConfigFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

public class Login {
    private String token;
    private String terminalUUID;
    private String deviceId;
    private Client client;
    private WebTarget target;
    private Gson gson;

    public Client getClient() {
        return client;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getTerminalUUID() {
        return terminalUUID;
    }

    public String getToken() {
        return token;
    }

    public Login() {
        ClientConfig cfg = ConfigFactory.create(ClientConfig.class);
        loginRR(cfg.cloudUserName(), cfg.cloudPassword());
    }

    public Login(String cloudUserName, String cloudPassword) {
        loginRR(cloudUserName, cloudPassword);
    }

    private void loginRR(String cloudUserName, String cloudPassword) {
        gson = new Gson();
        LoginRequest loginRequest = new LoginRequest(cloudUserName, cloudPassword);
        client = ClientBuilder.newClient();
        target = client.target("https://wap.tplinkcloud.com/");
        String response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(gson.toJson(loginRequest)), String.class);

        LoginResponse loginResponse = gson.fromJson(response, LoginResponse.class);

        token = loginResponse.getResult().getToken();
        terminalUUID = loginRequest.getParams().getTerminalUUID();
        deviceId = device();
    }

    private String device() {
        DeviceListRequest deviceListRequest = new DeviceListRequest(this.getTerminalUUID(), this.getToken());
        String response = target.request(MediaType.APPLICATION_JSON_TYPE)
                .header("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 6.0.1; A0001 Build/M4B30X)")
                .post(Entity.json(gson.toJson(deviceListRequest)), String.class);

        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        JsonObject Device = jsonObject.get("result").getAsJsonObject().get("deviceList").getAsJsonArray().get(0).getAsJsonObject();
        return Device.get("deviceId").getAsString();
    }

}
