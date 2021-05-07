package com.sblack.budgettracker.services;

import com.sblack.budgettracker.exception.BudgetTrackerException;
import com.sblack.budgettracker.model.MonthlySpending;
import com.sblack.budgettracker.util.PersonalCapitalCsvReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileUploadService {
    private static final String COMMA_DELIMITER = ",";

    public MonthlySpending parseCsv(MultipartFile file) throws BudgetTrackerException {
        return PersonalCapitalCsvReader.csvToMonthlyTransactions(this.getFileReader(file));
    }

    private Reader getFileReader(MultipartFile file) throws BudgetTrackerException {
        try {
            return new InputStreamReader(file.getInputStream());
        } catch (IOException e) {
            throw new BudgetTrackerException("Unable to parse file.", e);
        }

    }
    private List<List<String>> readRowsFromFile(MultipartFile file) throws BudgetTrackerException {
        try {
            InputStream fileInputStream = file.getInputStream();
            List<List<String>> records = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(COMMA_DELIMITER);
                    records.add(Arrays.asList(values));
                }
                return records;
            }
        } catch (IOException e) {
            throw new BudgetTrackerException("Unable to parse file.", e);
        }
    }
}
