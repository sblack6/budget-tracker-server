package com.sblack.budgettracker.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String category;
    private float amount;
}
