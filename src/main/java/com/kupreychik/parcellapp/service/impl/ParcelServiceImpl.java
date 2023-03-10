package com.kupreychik.parcellapp.service.impl;

import com.kupreychik.parcellapp.command.CreateParcelCommand;
import com.kupreychik.parcellapp.mapper.ParcelMapper;
import com.kupreychik.parcellapp.repository.ParcelRepository;
import com.kupreychik.parcellapp.service.ParcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ParcelServiceImpl implements ParcelService {

    private final ParcelRepository parcelRepository;
    private final ParcelMapper parcelMapper;

    @Override
    public void createParcel(CreateParcelCommand createParcelCommand) {

    }
}
