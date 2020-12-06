package com.trspo.main.requestDTO;
import com.trspo.main.enums.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientDTO {
    private final String name;
    private final SportsCategory sportsCategory;
    private final HorsemanStatus horsemanStatus;
}
