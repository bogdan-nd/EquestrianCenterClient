package com.trspo.main.requestDTO;

import com.trspo.main.enums.SportsCategory;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TrainerDTO {
    private final String name;
    private final int  salary;
    private final int trainingPrice;
    private SportsCategory sportCategory;


}
