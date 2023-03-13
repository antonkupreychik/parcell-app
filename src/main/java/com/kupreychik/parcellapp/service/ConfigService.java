package com.kupreychik.parcellapp.service;

import com.kupreychik.parcellapp.enums.ConfigName;

public interface ConfigService {

    Double getAsDouble(ConfigName name);

    Long getAsLong(ConfigName name);
}
