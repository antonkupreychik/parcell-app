package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.enums.ConfigName;
import com.kupreychik.parcellapp.service.ConfigService;
import com.kupreychik.parcellapp.service.DistanceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.geom.Point2D;
import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class DistanceServiceImpl implements DistanceService {

    private final ConfigService configService;

    @Override
    public BigDecimal calculatePriceBetweenCoordinates(String startPont, String endPoint) {
        Double startPointLat = Double.valueOf(startPont.split(",")[0]);
        Double startPointLon = Double.valueOf(startPont.split(",")[1]);

        Double endPointLat = Double.valueOf(endPoint.split(",")[0]);
        Double endPointLon = Double.valueOf(endPoint.split(",")[1]);

        Double pricePerOneKm = configService.getAsDouble(ConfigName.PRICE_PER_ONE_KM);
        Double distance = calculateDistance(startPointLat, startPointLon, endPointLat, endPointLon);
        return BigDecimal.valueOf(distance * pricePerOneKm);
    }

    private Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        return Point2D.distance(lat1, lon1, lat2, lon2);
    }
}
