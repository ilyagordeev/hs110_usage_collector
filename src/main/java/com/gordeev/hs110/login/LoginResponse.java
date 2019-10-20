package com.gordeev.hs110.login;

public class LoginResponse {
    private final int error_code;
    private Result result;

    public LoginResponse(int error_code) {
        this.error_code = error_code;
    }
    public int getError_code() {
        return error_code;
    }
    public Result getResult() {
        return result;
    }

    public class Result {
        private final String accountId;
        private final String regTime;
        private final String email;
        private final String token;

        public Result(String accountId, String regTime, String email, String token) {
            this.accountId = accountId;
            this.regTime = regTime;
            this.email = email;
            this.token = token;
        }

        public final String getAccountId() {
            return accountId;
        }
        public final String getRegTime() {
            return regTime;
        }
        public final String getEmail() {
            return email;
        }
        public final String getToken() {
            return token;
        }
    }
}
