package com.kupreychik.parcellapp.service;

import com.kupreychik.parcellapp.command.CreateParcelCommand;

public interface ParcelService {

    void createParcel(CreateParcelCommand createParcelCommand);
}
