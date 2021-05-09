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
@With
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer", "handler"})
public class MonthlySpending {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BudgetType type;
    private YearMonth date;
    private boolean isDefault;
    private boolean inProgress;
    private Float rent;
    private Float groceries;
    private Float travel;
    private Float restaurants;
    private Float loans;
    private Float clothing;
    private Float general_merchandise;
    private Float home_improvement;
    private Float hobbies;
    private Float cable;
    private Float entertainment;
    private Float gifts;
    private Float utilities;
    private Float atm;
    private Float gas;
    private Float healthcare;
    private Float subscriptions;
    private Float gym;
    private Float automotive;
    private Float food_delivery;
    private Float taxes;
    private Float transportation;
    private Float uber;
    private Float charitable_giving;
    private Float mortgage;
    private Float total;

    public MonthlySpending calculateTotal() {
        this.total = this.rent + this.groceries + this.travel + this.restaurants + this.loans + this.clothing +
                this.general_merchandise + this.home_improvement + this.hobbies + this.cable + this.entertainment +
                this.gifts + this.utilities + this.atm + this.gas + this.healthcare + this.subscriptions +
                this.uber + this.food_delivery + this.charitable_giving + this.transportation + this.gym + this.taxes +
                this.automotive + this.mortgage;
        return this;
    }

    public MonthlySpending setNullsToZero() {
        rent = rent == null ? 0F : rent;
        groceries = groceries == null ? 0F : groceries;
        travel = travel == null ? 0F : travel;
        restaurants = restaurants == null ? 0F : restaurants;
        loans = loans == null ? 0F : loans;
        clothing = clothing == null ? 0F : clothing;
        general_merchandise = general_merchandise == null ? 0F : general_merchandise;
        home_improvement = home_improvement == null ? 0F : home_improvement;
        hobbies = hobbies == null ? 0F : hobbies;
        cable = cable == null ? 0F : cable;
        entertainment = entertainment == null ? 0F : entertainment;
        gifts = gifts == null ? 0F : gifts;
        utilities = utilities == null ? 0F : utilities;
        atm = atm == null ? 0F : atm;
        gas = gas == null ? 0F : gas;
        healthcare = healthcare == null ? 0F : healthcare;
        subscriptions = subscriptions == null ? 0F : subscriptions;
        uber = uber == null ? 0F : uber;
        food_delivery = food_delivery == null ? 0F : food_delivery;
        charitable_giving = charitable_giving == null ? 0F : charitable_giving;
        transportation = transportation == null ? 0F : transportation;
        gym = gym == null ? 0F : gym;
        taxes = taxes == null ? 0F : taxes;
        automotive = automotive == null ? 0F : automotive;
        mortgage = mortgage == null ? 0F : mortgage;
        return this;
    }
}
