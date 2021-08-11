package com.sblack.budgettracker.controller;

import com.sblack.budgettracker.exception.BudgetTrackerException;
import com.sblack.budgettracker.model.BudgetType;
import com.sblack.budgettracker.model.MonthlySpending;
import com.sblack.budgettracker.repositories.MonthlyTransactionsRepository;
import com.sblack.budgettracker.services.FileUploadService;
import com.sblack.budgettracker.util.MonthlySpendingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/transactions")
public class MonthlyTransactionsController {

    private static final YearMonth DEFAULT_DATE = YearMonth.of(0, 1);
    private static final String SUCCESS = "Success";
    private static final String DEFAULT_ERROR_MSG = "User cannot add items with 'isDefault=true'.  Please use the Post /default-budget API.";

    @Autowired
    private MonthlyTransactionsRepository transactionsRepo;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping
    public List<MonthlySpending> list() {
        List<MonthlySpending> transactionList = transactionsRepo.findAll();
        return transactionList.stream().filter(item -> !item.isDefault() || item.getType().equals(BudgetType.TEST)).collect(Collectors.toList());
    }

    @GetMapping("/list-all")
    public List<MonthlySpending> listAll() {
        return transactionsRepo.findAll();
    }

    @GetMapping("/{id}")
    public MonthlySpending get(@PathVariable long id) {
        return transactionsRepo.getOne(id);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody MonthlySpending transactions) {
        if (transactions.isDefault()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DEFAULT_ERROR_MSG);
        }
        transactions.calculateTotal();
        transactionsRepo.save(transactions);
        return ResponseEntity.status(HttpStatus.OK).body(SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable long id, @RequestBody MonthlySpending monthlySpending) {
        if (monthlySpending.isDefault()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DEFAULT_ERROR_MSG);
        }
        monthlySpending.setId(id);
        monthlySpending.calculateTotal();
        transactionsRepo.save(monthlySpending);
        return ResponseEntity.status(HttpStatus.OK).body(monthlySpending);
    }

    @PutMapping("/merge/{type}/{date}")
    public ResponseEntity merge(@PathVariable String type, @PathVariable String date, @RequestBody MonthlySpending spendingToMerge) {
        if (spendingToMerge.isDefault()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(DEFAULT_ERROR_MSG);
        }
        MonthlySpending base = this.searchForMonthlyTransactions(type, date).get(0);
        MonthlySpending merged = MonthlySpendingUtil.merge(base, spendingToMerge);
        transactionsRepo.save(merged);
        return ResponseEntity.status(HttpStatus.OK).body(merged);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {
        transactionsRepo.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(SUCCESS);
    }

    @PostMapping("/upload-transactions")
    public ResponseEntity uploadTransactions(@RequestParam String source,
                                           @RequestBody MultipartFile file,
                                           @RequestParam("type") String type,
                                           @RequestParam("inProgress") boolean inProgress) {
        MonthlySpending monthlySpending;
        try {
            monthlySpending = fileUploadService.readTransactions(file, source);
            monthlySpending.setType(BudgetType.valueOf(type.toUpperCase()));
            monthlySpending.setInProgress(inProgress);
            monthlySpending.setDefault(false);
            transactionsRepo.save(monthlySpending);
        } catch (BudgetTrackerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(monthlySpending);
    }

    @PostMapping("/default-budget")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity createDefaultBudget(@RequestBody MonthlySpending transactions) {
        transactions.setId(null);
        transactions.calculateTotal();
        transactions.setDefault(true);
        transactions.setType(BudgetType.BUDGET);
        transactions.setDate(DEFAULT_DATE);
        transactions.setInProgress(false);
        transactionsRepo.save(transactions);
        return ResponseEntity.status(HttpStatus.OK).body(transactions);
    }

    @GetMapping("/default-budget")
    public MonthlySpending getDefaultBudget() {
        ExampleMatcher matcher = ExampleMatcher.matchingAll().withIgnoreNullValues();
        MonthlySpending defaultBudgetExample = MonthlySpending.builder()
                .isDefault(true)
                .date(DEFAULT_DATE)
                .type(BudgetType.BUDGET)
                .build();
        List<MonthlySpending> results = transactionsRepo.findAll(Example.of(defaultBudgetExample, matcher));
        Optional<MonthlySpending> result = results.stream().max(Comparator.comparing(MonthlySpending::getId));
        return result.isPresent() ? result.get() : null;
    }

    // date of format yyyy-mm
    @PostMapping("/default-budget/{date}")
    public ResponseEntity newBudgetFromDefault(@PathVariable String date, @RequestParam boolean inProgress) {
        YearMonth parsedDate = YearMonth.parse(date);
        MonthlySpending defaultBudget = this.getDefaultBudget();
        MonthlySpending newBudgetFromDefault = defaultBudget.withId(null);
        newBudgetFromDefault.setDefault(false);
        newBudgetFromDefault.setInProgress(inProgress);
        newBudgetFromDefault.setDate(parsedDate);
        newBudgetFromDefault.setId(null);
        transactionsRepo.save(newBudgetFromDefault);
        return ResponseEntity.status(HttpStatus.OK).body(newBudgetFromDefault);
    }

    // Search API
    @GetMapping("/search/{type}/{date}")
    public List<MonthlySpending> searchForMonthlyTransactions(@PathVariable String type, @PathVariable String date) {
        ExampleMatcher ignoreInProgressMatcher = ExampleMatcher.matchingAll()
                .withIgnorePaths("inProgress");
        MonthlySpending searchExample = MonthlySpending.builder()
                .date(YearMonth.parse(date))
                .type(BudgetType.valueOf(type.toUpperCase()))
                .isDefault(false)
                .build();
        return transactionsRepo.findAll(Example.of(searchExample, ignoreInProgressMatcher));
    }

    @DeleteMapping("/delete-if-exists/{type}/{date}")
    public ResponseEntity deleteIfExists(@PathVariable String type, @PathVariable String date) {
        List<MonthlySpending> entriesToDelete = this.searchForMonthlyTransactions(type, date);
        transactionsRepo.deleteAll(entriesToDelete);
        return ResponseEntity.status(HttpStatus.OK).body(entriesToDelete);
    }
}

