package com.trspo.main.requestDTO;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TrainingDTO {
    private final UUID trainerId;
    private final UUID horseId;
    private final UUID clientId;
    private final LocalDateTime startTime;
}
