package com.kupreychik.parcellapp.model;

import com.kupreychik.parcellapp.enums.ConfigName;
import lombok.AllArgsConstructor;
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

@Entity
@Table(name = "config")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "config_seq_gen", sequenceName = "config_id_seq", allocationSize = 1)
public class Config {

    @Id
    @GeneratedValue(generator = "config_seq_gen")
    private Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private ConfigName name;

    @Column(name = "value")
    private String value;
}
