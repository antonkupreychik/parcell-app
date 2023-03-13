package com.kupreychik.parcellapp.service;

import java.math.BigDecimal;

public interface DistanceService {
    BigDecimal calculatePriceBetweenCoordinates(String startPont, String endPoint);
}
