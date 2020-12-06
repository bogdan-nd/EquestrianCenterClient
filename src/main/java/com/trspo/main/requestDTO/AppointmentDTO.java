package com.trspo.main.requestDTO;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AppointmentDTO {
    private UUID horseId;
    private UUID specialistId;
}
