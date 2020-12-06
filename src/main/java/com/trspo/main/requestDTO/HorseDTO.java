package com.trspo.main.requestDTO;

import com.trspo.main.enums.HorsemanStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public final class HorseDTO {
    private final String name;
    private final UUID ownerId;
    private final HorsemanStatus horsemanStatus;
    private final int price;
}
