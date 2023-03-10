package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.command.CreateAddressCommand;
import com.kupreychik.parcellapp.command.CreateParcelCommand;
import com.kupreychik.parcellapp.dto.AddressDTO;
import com.kupreychik.parcellapp.dto.PageDTO;
import com.kupreychik.parcellapp.dto.ParcelDTO;
import com.kupreychik.parcellapp.dto.ParcelShortDTO;
import com.kupreychik.parcellapp.enums.ParcelStatus;
import com.kupreychik.parcellapp.enums.RoleName;
import com.kupreychik.parcellapp.exception.UiError;
import com.kupreychik.parcellapp.mapper.ParcelMapper;
import com.kupreychik.parcellapp.model.Parcel;
import com.kupreychik.parcellapp.model.User;
import com.kupreychik.parcellapp.repository.ParcelRepository;
import com.kupreychik.parcellapp.service.AddressService;
import com.kupreychik.parcellapp.service.ConfigService;
import com.kupreychik.parcellapp.service.ParcelHistoryService;
import com.kupreychik.parcellapp.service.ParcelService;
import com.kupreychik.parcellapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.kupreychik.parcellapp.exception.ParcelExceptionUtils.createParcelException;
import static java.util.Objects.nonNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParcelServiceImpl implements ParcelService {

    private final UserService userService;
    private final ParcelHistoryService parcelHistoryService;
    private final ConfigService configService;
    private final AddressService addressService;
    private final ParcelRepository parcelRepository;
    private final ParcelMapper parcelMapper;

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
            checkForAvailableToCreateParcel(userId);
            var parcel = parcelMapper.mapToEntity(createParcelCommand);
            parcel.setStatus(ParcelStatus.NEW);
            //todo remove it. change for jwt token
            parcel.setCustomer(userService.findUserByUserId(userId));

            var address = saveAddress(createParcelCommand.getAddress());
            parcel = parcelRepository.save(parcel);
            log.info("Parcel created with id: {}", parcel.getId());
            var parcelDTO = parcelMapper.mapToDTO(parcel);
            parcelDTO.setAddress(address);
            return parcelDTO;
        } catch (Exception e) {
            log.error("Error while creating parcel. Command: {}", createParcelCommand, e);
            throw e;
        }
    }

    /**
     * Get parcel page
     *
     * @param userId   user id
     * @param pageable pageable
     * @return {@link PageDTO} with parcel data
     */
    @Override
    @Transactional
    public PageDTO<ParcelShortDTO> getMyParcels(Long userId, String search, Pageable pageable) {
        try {
            log.info("Getting parcel by id: {}", userId);
            search = nonNull(search) ? search.toLowerCase() : "";
            var parcelPage = parcelRepository.findAllByUserId(userId, search, pageable);
            return new PageDTO<>(parcelPage, pageable);
        } catch (Exception e) {
            log.error("Error while getting parcel by id: {}", userId, e);
            throw e;
        }
    }

    /**
     * Get parcel page by status
     *
     * @param status   parcel status
     * @param userId   user id
     * @param pageable pageable
     * @return {@link PageDTO} with parcel data
     */
    @Override
    @Transactional
    public PageDTO<ParcelShortDTO> getMyParcelsByStatus(ParcelStatus status, Long userId, String search, Pageable pageable) {
        try {
            log.info("Getting parcel by status: {} and user id: {}", status, userId);
            search = nonNull(search) ? search.toLowerCase() : "";
            var parcelPage = parcelRepository.findAllByStatusAndCustomerId(status, userId, search, pageable);
            return new PageDTO<>(parcelPage, pageable);
        } catch (Exception e) {
            log.error("Error while getting parcel by status: {} and user id: {}", status, userId, e);
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
            parcel.setStatus(ParcelStatus.CANCELED);
            parcelRepository.save(parcel);
            log.info("Parcel canceled. Parcel id: {}", parcelId);
        } catch (Exception e) {
            log.error("Error while canceling parcel. Parcel id: {}", parcelId, e);
            throw e;
        }
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
            checkForUserIsCourier(courier);
            checkForAvailableToAssignCourier(parcel, courier);
            parcel.setCourier(courier);
            parcel.setStatus(ParcelStatus.ASSIGNED);
            parcel = parcelRepository.save(parcel);
            var parcelDTO = parcelMapper.mapToDTO(parcel);
            //todo решить проблему с маппингом и перенести ее в маппер
            parcelDTO.setAddress(addressService.getAddressById(parcel.getAddress().getId()));
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
            //todo решить проблему с маппингом и перенести ее в маппер
            parcelDTO.setAddress(addressService.getAddressById(parcel.getAddress().getId()));
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
        Long maxParcelsPerCourier = configService.getCountOfActiveParcelsPerCourier();
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
     * Check for user is courier
     *
     * @param courier courier
     */
    private void checkForUserIsCourier(User courier) {
        if (!Objects.equals(courier.getRole().getName(), RoleName.ROLE_COURIER.name())) {
            throw createParcelException(UiError.USER_IS_NOT_COURIER);
        }
    }

    /**
     * Check for canceling parcel
     *
     * @param parcel parcel
     */
    private void checkForAvailableToCancelParcel(Parcel parcel) {
        if (parcel.getStatus() != ParcelStatus.NEW) {
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
     * Check if user has available parcel count
     *
     * @param userId user id
     */
    private void checkForAvailableToCreateParcel(Long userId) {
        Long maxParcelCount = configService.getCountOfActiveParcelsPerUser();
        if (nonNull(maxParcelCount)) {
            var parcelCount = parcelRepository.countByCustomerIdAndStatus(userId, ParcelStatus.NEW);
            if (parcelCount >= maxParcelCount) {
                log.error("User with id: {} has reached max parcel count: {}", userId, maxParcelCount);
                throw createParcelException(UiError.MAX_PARCEL_COUNT_REACHED);
            }
        }
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
}
