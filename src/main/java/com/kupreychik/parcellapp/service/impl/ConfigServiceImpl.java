package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.enums.ConfigName;
import com.kupreychik.parcellapp.exception.UiError;
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

    private static final String CONFIG_VALUE_IS_NOT_A_NUMBER = "Config value is not a number";
    private static final String GET_CONFIG_VALUE_FOR = "Get config value for {}";
    private final ConfigRepository configRepository;

    @Override
    public Long getCountOfActiveParcelsPerCourier() {
        try {
            log.info(GET_CONFIG_VALUE_FOR, ConfigName.COUNT_OF_ACTIVE_PARCELS_PER_COURIER);
            return Long.parseLong(configRepository.findByName(ConfigName.COUNT_OF_ACTIVE_PARCELS_PER_COURIER)
                    .orElseThrow(() -> createParcelException(UiError.CONFIG_NOT_FOUND))
                    .getValue());
        } catch (Exception e) {
            log.error(CONFIG_VALUE_IS_NOT_A_NUMBER, e);
            throw e;
        }
    }

    @Override
    public Long getCountOfActiveParcelsPerUser() {
        try {
            log.info(GET_CONFIG_VALUE_FOR, ConfigName.COUNT_OF_ACTIVE_PARCELS_PER_USER);
            return Long.parseLong(configRepository.findByName(ConfigName.COUNT_OF_ACTIVE_PARCELS_PER_USER)
                    .orElseThrow(() -> createParcelException(UiError.CONFIG_NOT_FOUND))
                    .getValue());
        } catch (Exception e) {
            log.error(CONFIG_VALUE_IS_NOT_A_NUMBER, e);
            throw e;
        }
    }
}
