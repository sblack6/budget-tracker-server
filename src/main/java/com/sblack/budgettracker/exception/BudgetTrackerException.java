package com.sblack.budgettracker.exception;

import java.io.IOException;

public class BudgetTrackerException extends Exception {
    public BudgetTrackerException(String s, Exception e) {
        super(s, e);
    }
}
