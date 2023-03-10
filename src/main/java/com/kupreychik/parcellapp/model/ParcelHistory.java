package com.kupreychik.parcellapp.model;


import com.kupreychik.parcellapp.enums.ParcelStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "parcel_history")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "parcel_history_seq_gen", sequenceName = "parcel_history_id_seq", allocationSize = 1)
public class ParcelHistory {

    @Id
    @GeneratedValue(generator = "parcel_history_seq_gen")
    private Long id;

    @Column(name = "parcel_id")
    private Long parcelId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ParcelStatus status;

    @Column(name = "date")
    private LocalDateTime date;

}
