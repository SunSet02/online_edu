package com.sunset.msmservice.service;

import java.util.Map;

public interface MsmService {
    boolean sendCode(String code, String phone);
}
