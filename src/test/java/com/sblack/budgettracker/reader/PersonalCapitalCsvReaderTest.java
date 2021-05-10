package com.sblack.budgettracker.reader;

import com.sblack.budgettracker.exception.BudgetTrackerException;
import com.sblack.budgettracker.model.MonthlySpending;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.YearMonth;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
public class PersonalCapitalCsvReaderTest {

    TransactionsReader classUnderTest = new PersonalCapitalCsvReader();
    MonthlySpending expectedMonthlySpending;
    File pcCsvFile;

    @Before
    public void setup() throws IOException {
        pcCsvFile = new File("src/test/resources/2021-01-transactions.csv");
        System.out.println("File: " + pcCsvFile.canRead() + pcCsvFile.toString() + pcCsvFile.getCanonicalPath());
        expectedMonthlySpending = MonthlySpending.builder()
                .date(YearMonth.of(2021, 01))
                .inProgress(false)
                .restaurants(-20F)
                .groceries(-40F)
                .transportation(-30F)
                .general_merchandise(-60F)
                .cable(-35F)
                .utilities(-50F)
                .healthcare(-80F)
                .uber(-20F)
                .clothing(-50F)
                .atm(-60F)
                .charitable_giving(-80F)
                .build()
                .setNullsToZero()
                .calculateTotal();
    }

    @Test
    public void testParseCsv() throws IOException, BudgetTrackerException {
        MultipartFile testFile = new MockMultipartFile("test.csv", new FileInputStream(pcCsvFile));
        MonthlySpending result = classUnderTest.readTransactions(new InputStreamReader(testFile.getInputStream()));
        assertEquals(expectedMonthlySpending, result);
    }

}