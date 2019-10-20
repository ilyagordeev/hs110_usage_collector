package com.gordeev.hs110.login;

public class DeviceListRequest {

    public final String method = "getDeviceList";
    public final Params params;

    public DeviceListRequest(String termID, String token) {
        this.params = new Params(termID, token);
    }

    public class Params {
        public final String appName = "Kasa_Android";
        public final String termID;
        public final String appVer = "1.4.4.607";
        public final String ospf = "Android+6.0.1";
        public final String netType = "wifi";
        public final String locale = "es_ES";
        public final String token;


        public Params(String termID, String token) {
            this.termID = termID;
            this.token = token;
        }


    }
}
