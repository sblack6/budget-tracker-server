package com.sblack.budgettracker.util;

import com.sblack.budgettracker.model.MonthlySpending;

public class MonthlySpendingUtil {

    public static MonthlySpending merge(MonthlySpending base, MonthlySpending toMerge) {
        toMerge.setNullsToZero();
        if (toMerge.getAtm() != 0) {
            base.setAtm(toMerge.getAtm());
        }
        if (toMerge.getAutomotive() != 0) {
            base.setAutomotive(toMerge.getAutomotive());
        }
        if (toMerge.getCable() != 0) {
            base.setCable(toMerge.getCable());
        }
        if (toMerge.getCharitable_giving() != 0) {
            base.setCharitable_giving(toMerge.getCharitable_giving());
        }
        if (toMerge.getClothing() != 0) {
            base.setClothing(toMerge.getClothing());
        }
        if (toMerge.getEntertainment() != 0) {
            base.setEntertainment(toMerge.getEntertainment());
        }
        if (toMerge.getFood_delivery() != 0) {
            base.setFood_delivery(toMerge.getFood_delivery());
        }
        if (toMerge.getGas() != 0) {
            base.setGas(toMerge.getGas());
        }
        if (toMerge.getGeneral_merchandise() != 0 ) {
            base.setGeneral_merchandise(toMerge.getGeneral_merchandise());
        }
        if (toMerge.getGifts() != 0) {
            base.setGifts(toMerge.getGifts());
        }
        if (toMerge.getGroceries() != 0) {
            base.setGroceries(toMerge.getGroceries());
        }
        if (toMerge.getGym() != 0) {
            base.setGym(toMerge.getGym());
        }
        if (toMerge.getHealthcare() != 0) {
            base.setHealthcare(toMerge.getHealthcare());
        }
        if (toMerge.getHobbies() != 0) {
            base.setHobbies(toMerge.getHobbies());
        }
        if (toMerge.getHome_improvement() != 0) {
            base.setHome_improvement(toMerge.getHome_improvement());
        }
        if (toMerge.getLoans() != 0) {
            base.setLoans(toMerge.getLoans());
        }
        if (toMerge.getMortgage() != 0) {
            base.setMortgage(toMerge.getLoans());
        }
        if (toMerge.getRent() != 0) {
            base.setRent(toMerge.getRent());
        }
        if (toMerge.getRestaurants() != 0) {
            base.setRestaurants(toMerge.getRestaurants());
        }
        if (toMerge.getSubscriptions() != 0) {
            base.setSubscriptions(toMerge.getSubscriptions());
        }
        if (toMerge.getTaxes() != 0) {
            base.setTaxes(toMerge.getTaxes());
        }
        if (toMerge.getTransportation() != 0) {
            base.setTransportation(toMerge.getTransportation());
        }
        if (toMerge.getTravel() != 0) {
            base.setTravel(toMerge.getTravel());
        }
        if (toMerge.getUber() != 0) {
            base.setUber(toMerge.getUber());
        }
        if (toMerge.getUtilities() != 0) {
            base.setUtilities(toMerge.getUtilities());
        }
        return base.calculateTotal();
    }
}
