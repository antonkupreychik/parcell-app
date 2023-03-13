package com.kupreychik.parcellapp.middlewear;

import com.kupreychik.parcellapp.command.Command;
import com.kupreychik.parcellapp.command.CreateParcelCommand;
import com.kupreychik.parcellapp.enums.ConfigName;
import com.kupreychik.parcellapp.service.ConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParcelPriceMiddleware extends Middleware {

    private final ConfigService configService;

    @Override
    public boolean check(Command command) {
        Double minPrice = configService.getAsDouble(ConfigName.MIN_PRICE_PER_ONE_PARCEL);
        Double maxPrice = configService.getAsDouble(ConfigName.MAX_PRICE_PER_ONE_PARCEL);
        Double pricePerOneKg = configService.getAsDouble(ConfigName.PRICE_PER_ONE_KG);

        CreateParcelCommand createParcelCommand = (CreateParcelCommand) command;

        Double price = createParcelCommand.getWeight() * pricePerOneKg;
        if (price > maxPrice || price < minPrice) {
            log.info("Parcel price is not valid. Price: {}", price);
            return false;
        } else {
            log.info("Parcel price is valid");
            return checkNext(command);
        }
    }
}
