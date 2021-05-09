package com.sblack.budgettracker.reader;

import com.sblack.budgettracker.exception.BudgetTrackerException;
import com.sblack.budgettracker.model.MonthlySpending;

import java.io.Reader;

public interface TransactionsReader {

    MonthlySpending readTransactions(Reader fileReader) throws BudgetTrackerException;
}
