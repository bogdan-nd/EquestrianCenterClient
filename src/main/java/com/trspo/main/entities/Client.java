package com.trspo.main.entities;

import com.trspo.main.enums.HorsemanStatus;
import com.trspo.main.enums.SportsCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Client {
    private UUID id;
    private String name;
    private int creditMoney;
    private SportsCategory sportCategory;
    private HorsemanStatus horsemanStatus;
}
