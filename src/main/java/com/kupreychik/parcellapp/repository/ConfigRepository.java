package com.kupreychik.parcellapp.repository;

import com.kupreychik.parcellapp.enums.ConfigName;
import com.kupreychik.parcellapp.model.Config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {

    Optional<Config> findByName(ConfigName name);
}
