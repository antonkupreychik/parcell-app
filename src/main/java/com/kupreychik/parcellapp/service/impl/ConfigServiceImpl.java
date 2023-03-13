package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.enums.ConfigName;
import com.kupreychik.parcellapp.exception.UiError;
import com.kupreychik.parcellapp.model.Config;
import com.kupreychik.parcellapp.repository.ConfigRepository;
import com.kupreychik.parcellapp.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.kupreychik.parcellapp.exception.ParcelExceptionUtils.createParcelException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigServiceImpl implements ConfigService {

    private static final String CAN_T_GET_CONFIG_VALUE_FOR = "Can't get config value for {}";
    private final ConfigRepository configRepository;


    @Override
    public Double getAsDouble(ConfigName name) {
        try {
            return configRepository.findByName(name)
                    .map(config -> Double.valueOf(config.getValue()))
                    .orElseThrow(() -> createParcelException(UiError.CONFIG_NOT_FOUND));
        } catch (Exception e) {
            log.warn(CAN_T_GET_CONFIG_VALUE_FOR, name, e);
            throw e;
        }
    }

    @Override
    public Long getAsLong(ConfigName name) {
        try {
            return configRepository.findByName(name)
                    .map(config -> Long.valueOf(config.getValue()))
                    .orElseThrow(() -> createParcelException(UiError.CONFIG_NOT_FOUND));
        } catch (Exception e) {
            log.warn(CAN_T_GET_CONFIG_VALUE_FOR, name, e);
            throw e;
        }
    }
}
