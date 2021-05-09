package com.sblack.budgettracker.controller;

import com.sblack.budgettracker.exception.BudgetTrackerException;
import com.sblack.budgettracker.model.BudgetType;
import com.sblack.budgettracker.model.MonthlySpending;
import com.sblack.budgettracker.repositories.MonthlyTransactionsRepository;
import com.sblack.budgettracker.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
public class MonthlyTransactionsController {
    @Autowired
    private MonthlyTransactionsRepository transactionsRepo;

    @Autowired
    private FileUploadService fileUploadService;

    @GetMapping
    public List<MonthlySpending> list() {
        return transactionsRepo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void create(@RequestBody MonthlySpending transactions) {
        transactions.calculateTotal();
        transactionsRepo.save(transactions);
    }

    @PostMapping("/total")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> calculateTotals() {
        List<MonthlySpending> allMonthlySpendings = transactionsRepo.findAll();
        for( MonthlySpending monthlySpending : allMonthlySpendings) {
            monthlySpending.calculateTotal();
        }
        transactionsRepo.saveAll(allMonthlySpendings);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/parse-csv/{source}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> parseCsv(@PathVariable String source,
                                           @RequestParam("file") MultipartFile file,
                                           @RequestParam("type") String type,
                                           @RequestParam("inProgress") boolean inProgress) {
        try {
            MonthlySpending monthlySpending = fileUploadService.readTransactions(file, source);
            monthlySpending.setType(BudgetType.valueOf(type));
            monthlySpending.setInProgress(inProgress);
            transactionsRepo.save(monthlySpending);
        } catch (BudgetTrackerException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public MonthlySpending get(@PathVariable long id) {
        return transactionsRepo.getOne(id);
    }

    @PutMapping("/modify/{id}")
    public ResponseEntity<String> modify(@PathVariable long id, @RequestParam(required = false) Float rent) {
        MonthlySpending fromDB = transactionsRepo.getOne(id);
        if (rent != null) {
            fromDB.setRent(rent);
        }
        transactionsRepo.save(fromDB);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable long id, @RequestBody MonthlySpending monthlySpending) {
        monthlySpending.setId(id);
        transactionsRepo.save(monthlySpending);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable long id) {
        transactionsRepo.deleteById(id);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
