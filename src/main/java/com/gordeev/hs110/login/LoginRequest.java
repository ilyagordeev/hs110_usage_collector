package com.gordeev.hs110.login;

import java.util.UUID;

public class LoginRequest {
    private final String method;
    private final Params params;

    public LoginRequest(String cloudUserName, String cloudPassword) {
        this.params = new Params(cloudUserName, cloudPassword);
        this.method = "login";
    }

    public String getMethod() {
        return method;
    }

    public Params getParams() {
        return params;
    }

    public class Params {
        private final String appType;
        private final String cloudUserName;
        private final String cloudPassword;
        private final String terminalUUID;

        public Params(String cloudUserName, String cloudPassword) {
            this.cloudUserName = cloudUserName;
            this.cloudPassword = cloudPassword;
            this.terminalUUID = UUID.randomUUID().toString();
            this.appType = "Kasa_Android";
        }

        public String getAppType() {
            return appType;
        }

        public String getCloudUserName() {
            return cloudUserName;
        }

        public String getCloudPassword() {
            return cloudPassword;
        }

        public String getTerminalUUID() {
            return terminalUUID;
        }
    }
}
