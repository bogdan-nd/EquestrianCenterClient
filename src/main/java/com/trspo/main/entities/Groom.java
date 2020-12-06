package com.trspo.main.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Groom {
    private UUID id;
    private String name;
    private int  salary;
    private int carePrice;
    private int horseFedNumber;
}
