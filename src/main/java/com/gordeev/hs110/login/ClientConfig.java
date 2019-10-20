package com.gordeev.hs110.login;

import org.aeonbits.owner.Config;

public interface ClientConfig extends Config {
    String cloudUserName();
    String cloudPassword();
}