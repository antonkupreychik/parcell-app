package com.kupreychik.parcellapp.repository;

import com.kupreychik.parcellapp.model.Parcel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Parcel repository
 */
@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {
}
