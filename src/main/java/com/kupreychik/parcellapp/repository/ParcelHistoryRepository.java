package com.kupreychik.parcellapp.repository;

import com.kupreychik.parcellapp.model.ParcelHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelHistoryRepository extends JpaRepository<ParcelHistory, Long> {
}
