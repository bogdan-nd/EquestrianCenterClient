package com.trspo.main.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private UUID id;
    private UUID horseId;
    private UUID specialistId;

    public Appointment(UUID horseId, UUID specialistId){
        this.id = UUID.randomUUID();
        this.horseId = horseId;
        this.specialistId = specialistId;
    }

}
