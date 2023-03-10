package com.kupreychik.parcellapp.repository;

import com.kupreychik.parcellapp.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository for {@link Address} entity
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

}
