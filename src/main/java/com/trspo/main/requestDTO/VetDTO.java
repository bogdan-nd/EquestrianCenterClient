package com.trspo.main.requestDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VetDTO {
    private final String name;
    private final int  salary;
    private final int consultationPrice;
}
