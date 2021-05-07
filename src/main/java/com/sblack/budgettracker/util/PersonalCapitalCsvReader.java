package com.sblack.budgettracker.util;

import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sblack.budgettracker.exception.BudgetTrackerException;
import com.sblack.budgettracker.model.MonthlySpending;
import com.sblack.budgettracker.model.PersonalCapitalTransaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonalCapitalCsvReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonalCapitalCsvReader.class);

    private static final String[] HEADERS = { "Date", "Account", "Description", "Category", "Tags", "Amount"};
    private static final String AMOUNT = HEADERS[5];
    private static final String DATE = HEADERS[0];
    private static final String CATEGORY = HEADERS[3];

    public static MonthlySpending csvToMonthlyTransactions(Reader fileReader) throws BudgetTrackerException {
        try {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(HEADERS)
                    .withFirstRecordAsHeader()
                    .parse(fileReader);

            List<PersonalCapitalTransaction> transactions = new ArrayList<>();
            YearMonth date = YearMonth.of(0,1);
            YearMonth compareDate = YearMonth.of(0,1);
            for (CSVRecord record : records) {
                if (date.equals(compareDate)) {
                    date = getDate(record.get(DATE));
                }

                PersonalCapitalTransaction transaction = PersonalCapitalTransaction.builder()
                        .category(sanitizeCategoryName(record.get(CATEGORY)))
                        .amount(Float.parseFloat(record.get(AMOUNT)))
                        .build();
                transactions.add(transaction);
            }

            MonthlySpending monthlySpending = summarizeTransactions(transactions);
            monthlySpending.setDate(date);
            LOGGER.info("Uploading transactions: ", monthlySpending.toString());
            return monthlySpending;
        } catch (IOException e) {
            throw new BudgetTrackerException("Unable to parse 'Personal Capital' CSV file", e);
        }
    }

    private static YearMonth getDate(String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        return YearMonth.of(parsedDate.getYear(), parsedDate.getMonth());
    }

    private static MonthlySpending summarizeTransactions(List<PersonalCapitalTransaction> transactions) {
        Map<String, Float> transactionSummary = transactions.stream().collect(Collectors.toMap(
                PersonalCapitalTransaction::getCategory,
                PersonalCapitalTransaction::getAmount,
                (entry1, entry2) -> {
                    return entry1 + entry2;
        }));

        // Cable & Utilities bills are shared & should be split.
        Float cable = transactionSummary.get("cable");
        Float utilities = transactionSummary.get("utilities");
        if (cable != null) {
            transactionSummary.replace("cable", (cable/2));
        }
        if (utilities != null) {
            transactionSummary.replace("utilities", (utilities/2));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(transactionSummary, MonthlySpending.class).calculateTotal();
    }

    private static String sanitizeCategoryName(String categoryName) {
        categoryName = categoryName.replace(" ", "_");
        return categoryName.split("/")[0].toLowerCase();
    }

}
