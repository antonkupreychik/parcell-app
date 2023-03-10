package com.kupreychik.parcellapp.repository;

import com.kupreychik.parcellapp.dto.ParcelShortDTO;
import com.kupreychik.parcellapp.enums.ParcelStatus;
import com.kupreychik.parcellapp.model.Parcel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Repository for {@link Parcel} entity
 */
@Repository
public interface ParcelRepository extends JpaRepository<Parcel, Long> {

    @Query(value = """
            select new com.kupreychik.parcellapp.dto.ParcelShortDTO(p.id, p.name, p.weight, p.width, p.height, p.length, p.description, p.status)
                from Parcel p
             where p.customer.id = ?1 and p.name like %?2%""",
            countQuery = """
                    select count(p) from Parcel p where p.customer.id = ?1 and lower(p.name) like %?2%
                    """)
    Page<ParcelShortDTO> findAllByUserId(Long userId, String search, Pageable pageable);

    @Query(value = """
            select new com.kupreychik.parcellapp.dto.ParcelShortDTO(p.id, p.name, p.weight, p.width, p.height, p.length, p.description, p.status)
                from Parcel p
            where p.customer.id = ?2 and p.status = ?1 and lower(p.name) like %?3%
            """,
            countQuery = """
                    select count(p) from Parcel p where p.customer.id = ?2 and p.status = ?1 and p.name like %?3%
                    """)
    Page<ParcelShortDTO> findAllByStatusAndCustomerId(ParcelStatus status, Long userId, String search, Pageable pageable);


    @Query(value = "select new com.kupreychik.parcellapp.dto.ParcelShortDTO(p.id, p.name, p.weight, p.width, p.height, p.length, p.description, p.status) from Parcel p",
            countQuery = "select count(p) from Parcel p")
    Page<ParcelShortDTO> findAllParcels(Pageable pageable);

    Long countByCourierId(Long courierId);

    Long countByCustomerIdAndStatus(Long userId, ParcelStatus status);
}
