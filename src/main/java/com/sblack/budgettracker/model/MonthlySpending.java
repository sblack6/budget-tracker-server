package com.sblack.budgettracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.YearMonth;
import java.util.Date;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
public class MonthlySpending {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BudgetType type;
    private YearMonth date;
    private boolean inProgress;
    private float rent;
    private float groceries;
    private float travel;
    private float restaurants;
    private float loans;
    private float clothing;
    private float general_merchandise;
    private float home_improvement;
    private float hobbies;
    private float cable;
    private float entertainment;
    private float gifts;
    private float utilities;
    private float atm;
    private float gas;
    private float healthcare;
    private float subscriptions;
    private float gym;
    private float automotive;
    private float food_delivery;
    private float taxes;
    private float transportation;
    private float uber;
    private float charitable_giving;
    private float mortgage;
    private float total;

    public MonthlySpending calculateTotal() {
        this.total = this.rent + this.groceries + this.travel + this.restaurants + this.loans + this.clothing +
                this.general_merchandise + this.home_improvement + this.hobbies + this.cable + this.entertainment +
                this.gifts + this.utilities + this.atm + this.gas + this.healthcare + this.subscriptions +
                this.uber + this.food_delivery + this.charitable_giving + this.transportation + this.gym + this.taxes +
                this.automotive + this.mortgage;
        return this;
    }
}
