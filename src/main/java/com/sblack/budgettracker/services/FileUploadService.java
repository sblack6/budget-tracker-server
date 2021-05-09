package com.sblack.budgettracker.services;

import com.sblack.budgettracker.reader.TransactionsReader;
import com.sblack.budgettracker.exception.BudgetTrackerException;
import com.sblack.budgettracker.model.MonthlySpending;
import com.sblack.budgettracker.reader.PersonalCapitalCsvReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FileUploadService {

    static final String PERSONAL_CAPITAL_SOURCE = "personal-capital";
    static final String SOURCE_ERROR_MSG = "Source must be 'personal-capital'.  This endpoint currently only supports parsing csv files exported from personal capital";
    static final String FILE_PARSE_EXCEPTION = "Unable to parse file.";

    @Autowired
    private TransactionsReader personalCapitalTransactionsReader;

    public MonthlySpending readTransactions(MultipartFile file, String source) throws BudgetTrackerException {
        if (PERSONAL_CAPITAL_SOURCE.equals(source)) {
            return personalCapitalTransactionsReader.readTransactions(this.getFileReader(file));
        } else {
            throw new BudgetTrackerException(SOURCE_ERROR_MSG);
        }
    }

    private Reader getFileReader(MultipartFile file) throws BudgetTrackerException {
        try {
            return new InputStreamReader(file.getInputStream());
        } catch (IOException e) {
            throw new BudgetTrackerException(FILE_PARSE_EXCEPTION, e);
        }

    }
}
