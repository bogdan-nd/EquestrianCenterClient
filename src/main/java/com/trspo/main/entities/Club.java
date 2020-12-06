package com.trspo.main.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Club {
    private UUID id;
    private int seedCapital;
    private int moneyAmount;
}
