package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.command.CreateAddressCommand;
import com.kupreychik.parcellapp.command.CreateParcelCommand;
import com.kupreychik.parcellapp.command.UpdateUserBalanceCommand;
import com.kupreychik.parcellapp.dto.AddressDTO;
import com.kupreychik.parcellapp.dto.PageDTO;
import com.kupreychik.parcellapp.dto.ParcelDTO;
import com.kupreychik.parcellapp.dto.ParcelShortDTO;
import com.kupreychik.parcellapp.enums.OperationType;
import com.kupreychik.parcellapp.enums.ParcelStatus;
import com.kupreychik.parcellapp.enums.RoleName;
import com.kupreychik.parcellapp.exception.UiError;
import com.kupreychik.parcellapp.mapper.ParcelMapper;
import com.kupreychik.parcellapp.middlewear.Middleware;
import com.kupreychik.parcellapp.middlewear.ParcelParamsMiddleware;
import com.kupreychik.parcellapp.middlewear.ParcelPriceMiddleware;
import com.kupreychik.parcellapp.model.Parcel;
import com.kupreychik.parcellapp.model.User;
import com.kupreychik.parcellapp.repository.ParcelRepository;
import com.kupreychik.parcellapp.service.AddressService;
import com.kupreychik.parcellapp.service.ConfigService;
import com.kupreychik.parcellapp.service.DistanceService;
import com.kupreychik.parcellapp.service.ParcelHistoryService;
import com.kupreychik.parcellapp.service.ParcelService;
import com.kupreychik.parcellapp.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

import static com.kupreychik.parcellapp.enums.ConfigName.COUNT_OF_ACTIVE_PARCELS_PER_COURIER;
import static com.kupreychik.parcellapp.enums.ConfigName.COUNT_OF_ACTIVE_PARCELS_PER_USER;
import static com.kupreychik.parcellapp.enums.ConfigName.MAX_PRICE_PER_ONE_PARCEL;
import static com.kupreychik.parcellapp.enums.ConfigName.MIN_PRICE_PER_ONE_PARCEL;
import static com.kupreychik.parcellapp.enums.ConfigName.PERCENT_OF_CANCEL_IN_ASSIGNED_STATUS;
import static com.kupreychik.parcellapp.enums.ConfigName.PERCENT_OF_CANCEL_IN_IN_PROGRESS_STATUS;
import static com.kupreychik.parcellapp.enums.ConfigName.PRICE_PER_ONE_KG;
import static com.kupreychik.parcellapp.exception.ParcelExceptionUtils.createParcelException;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class ParcelServiceImpl implements ParcelService {

    private static final String COMMA = ",";
    private final UserService userService;
    private final ParcelHistoryService parcelHistoryService;
    private final DistanceService distanceService;
    private final ConfigService configService;
    private final AddressService addressService;
    private final ParcelRepository parcelRepository;
    private final ParcelMapper parcelMapper;
    private final Middleware createParcelMiddleware;

    public ParcelServiceImpl(UserService userService,
                             ParcelHistoryService parcelHistoryService,
                             DistanceService distanceService,
                             ConfigService configService,
                             AddressService addressService,
                             ParcelRepository parcelRepository,
                             ParcelMapper parcelMapper) {
        this.userService = userService;
        this.parcelHistoryService = parcelHistoryService;
        this.distanceService = distanceService;
        this.configService = configService;
        this.addressService = addressService;
        this.parcelRepository = parcelRepository;
        this.parcelMapper = parcelMapper;
        this.createParcelMiddleware = Middleware.link(
                new ParcelPriceMiddleware(configService),
                new ParcelParamsMiddleware(configService)
        );
    }

    /**
     * Create parcel
     *
     * @param createParcelCommand command with parcel data
     * @param userId              user id
     * @return {@link ParcelDTO} with parcel data
     */
    @Override
    @Transactional
    public ParcelDTO createParcel(CreateParcelCommand createParcelCommand, Long userId) {
        try {
            log.info("Creating parcel with command: {}", createParcelCommand);
            if (createParcelMiddleware.check(createParcelCommand) && checkForAvailableToCreateParcel(createParcelCommand, userId)) {
                var parcel = parcelMapper.mapToEntity(createParcelCommand);
                parcel = addParamsAndSave(createParcelCommand, userId, parcel);
                processPaymentForUser(userId, parcel.getPrice(), OperationType.SUBTRACT);
                return parcelMapper.mapToDTO(parcel);
            } else {
                throw createParcelException(UiError.PARCEL_PARAMS_NOT_VALID);
            }
        } catch (Exception e) {
            log.error("Error while creating parcel. Command: {}", createParcelCommand, e);
            throw e;
        }
    }

    /**
     * Get parcel page by status
     *
     * @param statuses   parcel statuses
     * @param userId   user id
     * @param pageable pageable
     * @return {@link PageDTO} with parcel data
     */
    @Override
    @Transactional
    public PageDTO<ParcelShortDTO> getMyParcels(ParcelStatus[] statuses, Long userId, String search, Pageable pageable) {
        try {
            log.info("Getting parcel by statuses: {} and user id: {}", statuses, userId);
            search = nonNull(search) ? search.toLowerCase() : "";
            var user = userService.findUserByUserId(userId);
            var role = user.getRole().getAuthority();
            Page<ParcelShortDTO> parcelPage;
            if (role.equals(RoleName.ROLE_COURIER)) {
                log.info("Getting parcel by courier id: {}", userId);
                parcelPage = parcelRepository.findAllByCourierIdAndStatus(userId, search, statuses, pageable);
            } else if (role.equals(RoleName.ROLE_USER)) {
                log.info("Getting parcel by user id: {}", userId);
                parcelPage = parcelRepository.findAllByCustomerIdAndStatus(userId, search, statuses, pageable);
            } else {
                throw createParcelException(UiError.USER_NOT_FOUND);
            }
            return new PageDTO<>(parcelPage, pageable);
        } catch (Exception e) {
            log.error("Error while getting parcel by status: {} and user id: {}", statuses, userId, e);
            throw e;
        }
    }

    /**
     * Update parcel address by parcel id
     *
     * @param userId               user id
     * @param parcelId             parcel id
     * @param createAddressCommand address data
     * @return {@link ParcelDTO} with parcel data
     */
    @Override
    @Transactional
    public ParcelDTO updateParcelAddress(Long userId, Long parcelId, CreateAddressCommand createAddressCommand) {
        try {
            log.info("Updating parcel address. Parcel id: {}, address: {}", parcelId, createAddressCommand);
            var parcel = parcelRepository.getReferenceById(parcelId);
            checkForParcelOwner(userId, parcel);
            checkForAvailableToUpdateParcelAddress(parcel);
            var address = saveAddress(createAddressCommand);
            parcelRepository.save(parcel);
            var parcelDTO = parcelMapper.mapToDTO(parcel);
            parcelDTO.setAddress(address);
            log.info("Parcel address updated. Parcel id: {}, address: {}", parcelId, createAddressCommand);
            return parcelDTO;
        } catch (Exception e) {
            log.error("Error while updating parcel address. Parcel id: {}, address: {}", parcelId, createAddressCommand, e);
            throw e;
        }
    }

    /**
     * Cancel parcel
     *
     * @param userId   user id
     * @param parcelId parcel id
     */
    @Override
    @Transactional
    public void cancelParcel(Long userId, Long parcelId) {
        try {
            log.info("Canceling parcel. Parcel id: {}", parcelId);
            var parcel = parcelRepository.getReferenceById(parcelId);
            checkForParcelOwner(userId, parcel);
            checkForAvailableToCancelParcel(parcel);
            returnMoneyToUser(userId, parcel);
            parcel.setStatus(ParcelStatus.CANCELED);
            parcelRepository.save(parcel);
            log.info("Parcel canceled. Parcel id: {}", parcelId);
        } catch (Exception e) {
            log.error("Error while canceling parcel. Parcel id: {}", parcelId, e);
            throw e;
        }
    }

    private void returnMoneyToUser(Long userId, Parcel parcel) {
        ParcelStatus status = parcel.getStatus();
        BigDecimal actualPriceForParcel = parcel.getPrice();
        log.info("Return money to user. Parcel id: {}, status: {}, actual price: {}", parcel.getId(), status, actualPriceForParcel);
        if (status == ParcelStatus.ASSIGNED) {
            Double returnPercentForAssignStatus = configService.getAsDouble(PERCENT_OF_CANCEL_IN_ASSIGNED_STATUS);
            BigDecimal returnMoney = actualPriceForParcel.multiply(BigDecimal.valueOf(returnPercentForAssignStatus));
            processPaymentForUser(userId, returnMoney, OperationType.ADD);
        } else if (status == ParcelStatus.IN_PROGRESS) {
            Double returnPercentForInProgressStatus = configService.getAsDouble(PERCENT_OF_CANCEL_IN_IN_PROGRESS_STATUS);
            BigDecimal returnMoney = actualPriceForParcel.multiply(BigDecimal.valueOf(returnPercentForInProgressStatus));
            processPaymentForUser(userId, returnMoney, OperationType.ADD);
        } else if (status == ParcelStatus.NEW) {
            processPaymentForUser(userId, actualPriceForParcel, OperationType.ADD);
        }
        log.info("Money returned to user. Parcel id: {}, status: {}, actual price: {}", parcel.getId(), status, actualPriceForParcel);
    }

    /**
     * Assign courier to parcel
     *
     * @param parcelId  parcel id
     * @param courierId courier id
     * @return {@link ParcelDTO} with parcel data
     */
    @Override
    @Transactional
    public ParcelDTO assignCourier(Long parcelId, Long courierId) {
        try {
            log.info("Assigning courier. Parcel id: {}, courier id: {}", parcelId, courierId);
            var parcel = parcelRepository.getReferenceById(parcelId);
            var courier = userService.findUserByUserId(courierId);
            var customer = userService.findUserByUserId(parcel.getCustomer().getId());
            checkForUserIsCourier(courier);
            checkForAvailableToAssignCourier(parcel, courier);
            parcel = setAdditionalParamsToParcelAndAssignCourier(parcel, courier, customer);
            var parcelDTO = parcelMapper.mapToDTO(parcel);
            log.info("Courier assigned. Parcel id: {}, courier id: {}", parcelId, courierId);
            parcelHistoryService.changeStatus(parcel.getId(), ParcelStatus.ASSIGNED);
            return parcelDTO;
        } catch (Exception e) {
            log.error("Error while assigning courier. Parcel id: {}, courier id: {}", parcelId, courierId, e);
            throw e;
        }
    }

    /**
     * Change parcel status
     *
     * @param parcelId parcel id
     * @param status   parcel status
     * @return {@link ParcelDTO} with parcel data
     */
    @Override
    @Transactional
    public ParcelDTO changeParcelStatus(Long parcelId, ParcelStatus status) {
        try {
            log.info("Changing parcel status. Parcel id: {}, status: {}", parcelId, status);
            var parcel = parcelRepository.getReferenceById(parcelId);
            parcel.setStatus(status);
            parcel = parcelRepository.save(parcel);
            var parcelDTO = parcelMapper.mapToDTO(parcel);
            log.info("Parcel status changed. Parcel id: {}, status: {}", parcelId, status);
            parcelHistoryService.changeStatus(parcel.getId(), status);
            return parcelDTO;
        } catch (Exception e) {
            log.error("Error while changing parcel status. Parcel id: {}, status: {}", parcelId, status, e);
            throw e;
        }
    }

    @Override
    @Cacheable(value = "parcels")
    public PageDTO<ParcelShortDTO> getAllParcels(Pageable pageable) {
        try {
            log.info("Getting all parcels");
            var parcelPage = parcelRepository.findAllParcels(pageable);
            return new PageDTO<>(parcelPage, pageable);
        } catch (Exception e) {
            log.error("Error while getting all parcels", e);
            throw e;
        }
    }

    /**
     * Check for parcel is not assigned to courier yet
     *
     * @param parcel parcel
     */
    private void checkForAvailableToAssignCourier(Parcel parcel, User courier) {
        Long currentCourierAssigned = parcelRepository.countByCourierId(courier.getId());
        Long maxParcelsPerCourier = configService.getAsLong(COUNT_OF_ACTIVE_PARCELS_PER_COURIER);
        if (currentCourierAssigned >= maxParcelsPerCourier) {
            throw createParcelException(UiError.COURIER_LIMIT_PER_PARCEL_EXCEEDED);
        }
        if (nonNull(parcel.getCourier())) {
            throw createParcelException(UiError.PARCEL_ALREADY_ASSIGNED_TO_COURIER);
        }
        if (parcel.getStatus() != ParcelStatus.NEW) {
            throw createParcelException(UiError.PARCEL_NOT_AVAILABLE_TO_ASSIGN_COURIER);
        }
    }

    /**
     * Check for user is couriered
     *
     * @param courier courier
     */
    private void checkForUserIsCourier(User courier) {
        if (!Objects.equals(courier.getRole().getAuthority(), RoleName.ROLE_COURIER)) {
            throw createParcelException(UiError.USER_IS_NOT_COURIER);
        }
    }

    /**
     * Check for canceling parcel
     *
     * @param parcel parcel
     */
    private void checkForAvailableToCancelParcel(Parcel parcel) {
        if (!parcel.getStatus().isAvailableToCancel()) {
            throw createParcelException(UiError.PARCEL_NOT_AVAILABLE_TO_CANCEL);
        }
    }

    /**
     * Get parcel by id
     *
     * @param userId   user id
     * @param parcelId parcel id
     * @return {@link ParcelDTO} with parcel data
     */
    @Override
    public ParcelDTO getParcelById(Long userId, Long parcelId) {
        try {
            log.info("Getting parcel by id: {}", parcelId);
            var parcel = parcelRepository.getReferenceById(parcelId);
            checkForParcelOwner(userId, parcel);
            var parcelDTO = parcelMapper.mapToDTO(parcel);
            parcelDTO.setAddress(addressService.getAddressById(parcel.getAddress().getId()));
            return parcelDTO;
        } catch (Exception e) {
            log.error("Error while getting parcel by id: {}", parcelId, e);
            throw e;
        }
    }

    /**
     * Check if parcel belongs to the user. If not - throw exception
     *
     * @param userId user id
     * @param parcel parcel
     */
    private void checkForParcelOwner(Long userId, Parcel parcel) {
        if (!parcel.getCustomer().getId().equals(userId)) {
            log.error("Parcel with id: {} not found for user with id: {}", parcel.getId(), userId);
            throw createParcelException(UiError.PARCEL_NOT_FOUND);
        }
    }


    /**
     * Check if parcel is available to change address. If not - throw exception
     *
     * @param parcel parcel
     */
    private void checkForAvailableToUpdateParcelAddress(Parcel parcel) {
        if (parcel.getStatus() != ParcelStatus.NEW) {
            log.error("Parcel with id: {} is not available to change address", parcel.getId());
            throw createParcelException(UiError.PARCEL_NOT_AVAILABLE_TO_CHANGE_ADDRESS);
        }
    }

    /**
     * Check if user is available to create a parcel. If not - throw exception
     *
     * @param userId user id
     */
    private boolean checkForAvailableToCreateParcel(CreateParcelCommand createParcelCommand, Long userId) {
        try {
            checkForMaxParcelCountPerUser(userId);
            checkForPriceRange(createParcelCommand);
            return true;
        } catch (Exception e) {
            log.warn("User with id: {} is not available to create parcel", userId);
            return false;
        }
    }

    /**
     * Check for parcel price range
     *
     * @param createParcelCommand create parcel command
     */
    private void checkForPriceRange(CreateParcelCommand createParcelCommand) {
        BigDecimal currentPrice = getPriceForParcel(createParcelCommand.getWeight());
        Double minAvailablePrice = configService.getAsDouble(MIN_PRICE_PER_ONE_PARCEL);
        Double maxAvailablePrice = configService.getAsDouble(MAX_PRICE_PER_ONE_PARCEL);

        if (nonNull(minAvailablePrice) && currentPrice.compareTo(BigDecimal.valueOf(minAvailablePrice)) < 0) {
            log.error("Price for parcel is less than min available price: {}", minAvailablePrice);
            throw createParcelException(UiError.MIN_PRICE_PER_ONE_PARCEL_REACHED);
        }
        if (nonNull(maxAvailablePrice) && currentPrice.compareTo(BigDecimal.valueOf(maxAvailablePrice)) > 0) {
            log.error("Price for parcel is more than max available price: {}", maxAvailablePrice);
            throw createParcelException(UiError.MAX_PRICE_PER_ONE_PARCEL_REACHED);
        }
    }

    /**
     * Check that user has not reached max parcel count. If not - throw exception
     *
     * @param userId user id
     */
    private void checkForMaxParcelCountPerUser(Long userId) {
        Long maxParcelCount = configService.getAsLong(COUNT_OF_ACTIVE_PARCELS_PER_USER);
        if (nonNull(maxParcelCount)) {
            var parcelCount = parcelRepository.countByCustomerIdAndStatus(userId, ParcelStatus.NEW);
            if (parcelCount >= maxParcelCount) {
                log.error("User with id: {} has reached max parcel count: {}", userId, maxParcelCount);
                throw createParcelException(UiError.MAX_PARCEL_COUNT_REACHED);
            }
        }
    }

    /**
     * Calculate price for parcel
     *
     * @param currentWeight parcel weight
     * @return price for parcel
     */
    private BigDecimal getPriceForParcel(Double currentWeight) {
        Double pricePerOneKg = configService.getAsDouble(PRICE_PER_ONE_KG);
        double price = pricePerOneKg * currentWeight;
        return BigDecimal.valueOf(price);
    }

    /**
     * Save address
     *
     * @param createAddressCommand command with address data
     * @return {@link AddressDTO} with address data
     */
    private AddressDTO saveAddress(CreateAddressCommand createAddressCommand) {
        return addressService.saveAddress(createAddressCommand);
    }

    /**
     * Add params to parcel and save it
     *
     * @param userId user id
     * @param parcel parcel
     * @return parcel with added params
     */
    private Parcel addParamsAndSave(CreateParcelCommand createParcelCommand, Long userId, Parcel parcel) {
        parcel.setCustomer(userService.findUserByUserId(userId));
        parcel.setPrice(calculatePreliminaryPrice(createParcelCommand, userId));
        parcel = parcelRepository.save(parcel);
        log.info("Parcel created with id: {}", parcel.getId());
        return parcel;
    }

    /**
     * Calculate preliminary price for parcel
     *
     * @param createParcelCommand command with parcel data
     * @param userId              user id
     * @return preliminary price for parcel
     */
    private BigDecimal calculatePreliminaryPrice(CreateParcelCommand createParcelCommand, Long userId) {
        BigDecimal priceForKg = getPriceForParcel(createParcelCommand.getWeight());
        BigDecimal priceForDistance = getPriceForDistance(createParcelCommand, userId);
        return priceForKg.add(priceForDistance);
    }

    /**
     * Calculate price for parcel by distance between start and end points
     *
     * @param createParcelCommand command with parcel data
     * @param userId              user id
     * @return price for distance between start and end points
     */
    private BigDecimal getPriceForDistance(CreateParcelCommand createParcelCommand, Long userId) {
        String userCoordinates = userService.findUserByUserId(userId).getCoordinates();
        String endPointCoordinates = createParcelCommand.getAddress().getCoordinates().getLatitude()
                + COMMA + createParcelCommand.getAddress().getCoordinates().getLongitude();
        return distanceService.calculatePriceBetweenCoordinates(userCoordinates, endPointCoordinates);
    }

    /**
     * Assign courier and recalculate price for parcel
     *
     * @param parcel   parcel
     * @param courier  courier
     * @param customer customer
     * @return parcel with assigned courier
     */
    private Parcel setAdditionalParamsToParcelAndAssignCourier(Parcel parcel, User courier, User customer) {
        parcel.setCourier(courier);
        parcel.setStatus(ParcelStatus.ASSIGNED);
        parcel.setPrice(recalculatePrice(customer, courier, parcel));
        parcel = parcelRepository.save(parcel);
        return parcel;
    }

    /**
     * Recalculate price for parcel include distance between courier and customer
     *
     * @param customer customer
     * @param courier  courier
     * @param parcel   parcel
     * @return recalculate price for parcel
     */
    private BigDecimal recalculatePrice(User customer, User courier, Parcel parcel) {
        BigDecimal currentPrice = parcel.getPrice();
        String courierCoordinates = courier.getCoordinates();
        String userCoordinates = customer.getCoordinates();
        BigDecimal priceForDistanceBetweenCustomerAndCourier = distanceService.calculatePriceBetweenCoordinates(courierCoordinates, userCoordinates);
        processPaymentForUser(customer.getId(), priceForDistanceBetweenCustomerAndCourier, OperationType.SUBTRACT);
        return currentPrice.add(priceForDistanceBetweenCustomerAndCourier);
    }

    /**
     * Withdraw money from user
     *
     * @param userId user id
     * @param value  parcel
     */
    private void processPaymentForUser(Long userId, BigDecimal value, OperationType operationType) {
        log.info("Trying to withdraw money from user with id: {}", userId);
        UpdateUserBalanceCommand withdrawMoneyCommand = UpdateUserBalanceCommand.builder()
                .amount(value)
                .operationType(operationType)
                .userId(userId)
                .build();
        userService.updateUserBalance(withdrawMoneyCommand);
    }

}
