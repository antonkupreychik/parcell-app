package com.kupreychik.parcellapp.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SortDTO {
    private String field;
    private String direction;


    public SortDTO(Sort sort) {
        if (sort.isSorted()) {
            Optional<Sort.Order> s = sort.get().findFirst();

            if (s.isPresent()) {
                this.field = s.get().getProperty();
                this.direction = s.get().getDirection().name();
                return;
            }
        }

        this.field = "";
        this.direction = "";
    }
}
