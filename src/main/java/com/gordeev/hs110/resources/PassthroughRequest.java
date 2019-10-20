package com.gordeev.hs110.resources;

public class PassthroughRequest {
    private String method = "passthrough";
    private Params params;

    public PassthroughRequest(String deviceId, String requestData) {
        this.params = new Params(deviceId, requestData);
    }

    private class Params {
        public final String deviceId;
        public String requestData;

        public Params(String deviceId, String requestData) {
            this.deviceId = deviceId;
            this.requestData = requestData;
        }
    }
}
