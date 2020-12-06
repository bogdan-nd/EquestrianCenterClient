package com.trspo.main.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FinancePayment {
    private UUID id;
    private UUID senderId;
    private UUID receiverId;
    private int moneyAmount;
    private String description;

    public FinancePayment(UUID senderId, UUID receiverId, int moneyAmount, String description){
        this.id = UUID.randomUUID();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.moneyAmount = moneyAmount;
        this.description = description;
    }
}
