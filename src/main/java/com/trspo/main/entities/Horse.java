package com.trspo.main.entities;

import com.trspo.main.enums.HorsemanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Horse {
    private UUID id;
    private String name;
    private UUID ownerId;
    private HorsemanStatus horsemanStatus;
    private int price;
    private boolean isIll;
    private LocalDateTime lastTimeEat;
}
