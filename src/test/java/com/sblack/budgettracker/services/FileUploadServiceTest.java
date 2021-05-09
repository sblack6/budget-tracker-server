package com.sblack.budgettracker.services;

import com.sblack.budgettracker.exception.BudgetTrackerException;
import com.sblack.budgettracker.model.MonthlySpending;
import com.sblack.budgettracker.reader.TransactionsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringJUnit4ClassRunner.class)
public class FileUploadServiceTest {

    @Mock
    private TransactionsReader personalCapitalTransactionsReader;

    @InjectMocks
    FileUploadService fileUploadService;

    MultipartFile file;
    MonthlySpending monthlySpending = new MonthlySpending();

    @Before
    public void setup() throws BudgetTrackerException, IOException {
        InputStream inputStreamMock = Mockito.mock(InputStream.class);
        file = Mockito.mock(MultipartFile.class);
        Mockito.when(file.getInputStream()).thenReturn(inputStreamMock);
        Mockito.when(personalCapitalTransactionsReader.readTransactions(any())).thenReturn(monthlySpending);
    }

    @Test
    public void testReadTransactions() throws BudgetTrackerException {
        MonthlySpending result = fileUploadService.readTransactions(file, FileUploadService.PERSONAL_CAPITAL_SOURCE);
        assertEquals(monthlySpending, result);
    }

}