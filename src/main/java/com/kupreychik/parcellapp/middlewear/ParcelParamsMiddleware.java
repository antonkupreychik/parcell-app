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
public class ParcelParamsMiddleware extends Middleware {

    private final ConfigService configService;

    @Override
    public boolean check(Command command) {
        Double maxLength = configService.getAsDouble(ConfigName.MAX_LENGTH_PER_ONE_PARCEL);
        Double minLength = configService.getAsDouble(ConfigName.MIN_LENGTH_PER_ONE_PARCEL);
        Double minHeight = configService.getAsDouble(ConfigName.MIN_HEIGHT_PER_ONE_PARCEL);
        Double maxHeight = configService.getAsDouble(ConfigName.MAX_HEIGHT_PER_ONE_PARCEL);
        Double minWidth = configService.getAsDouble(ConfigName.MIN_WIDTH_PER_ONE_PARCEL);
        Double maxWidth = configService.getAsDouble(ConfigName.MAX_WIDTH_PER_ONE_PARCEL);

        Double maxWeight = configService.getAsDouble(ConfigName.MAX_WEIGHT_PER_ONE_PARCEL);
        Double minWeight = configService.getAsDouble(ConfigName.MIN_WEIGHT_PER_ONE_PARCEL);

        CreateParcelCommand createParcelCommand = (CreateParcelCommand) command;
        if (createParcelCommand.getLength() > maxLength || createParcelCommand.getLength() < minLength) {
            log.info("Parcel length is not valid. Length: {}", createParcelCommand.getLength());
            return false;
        } else if (createParcelCommand.getHeight() > maxHeight || createParcelCommand.getHeight() < minHeight) {
            log.info("Parcel height is not valid. Height: {}", createParcelCommand.getHeight());
            return false;
        } else if (createParcelCommand.getWidth() > maxWidth || createParcelCommand.getWidth() < minWidth) {
            log.info("Parcel width is not valid. Width: {}", createParcelCommand.getWidth());
            return false;
        } else if (createParcelCommand.getWeight() > maxWeight || createParcelCommand.getWeight() < minWeight) {
            log.info("Parcel weight is not valid. Weight: {}", createParcelCommand.getWeight());
            return false;
        } else {
            log.info("Parcel params are valid");
            return checkNext(command);
        }
    }
}
