package com.trspo.main.entities;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    private UUID id;
    private UUID trainerId;
    private UUID horseId;
    private UUID clientId;
    private LocalDateTime startTime;
}
