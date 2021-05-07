package com.sblack.budgettracker.repositories;

import com.sblack.budgettracker.model.MonthlySpending;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlyTransactionsRepository extends JpaRepository<MonthlySpending, Long> {

}
