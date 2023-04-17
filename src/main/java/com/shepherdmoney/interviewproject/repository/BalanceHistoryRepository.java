package com.shepherdmoney.interviewproject.repository;
import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository("BalanceHistoryRepo")
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, Integer> {

    @Query("SELECT b FROM BalanceHistory b WHERE b.creditCard.id = :creditCardId AND b.date = :transactionTime")
    BalanceHistory getBalanceHistoryByCreditCardIdAndDate(Integer creditCardId, LocalDate transactionTime);

    // get the earliest older balance
    @Query("SELECT b FROM BalanceHistory b WHERE b.creditCard.id = :creditCardId AND b.date < :transactionTime ORDER BY b.date DESC")
    List<BalanceHistory> getBalanceHistoryByCreditCardIdAndDateBeforeOrderByDateDesc(Integer creditCardId, LocalDate transactionTime);

    // update a balance history record
    @Modifying
    @Query("UPDATE BalanceHistory b SET b.balance = :curBalance WHERE b.creditCard = :creditCard AND b.date = :date")
    void updateBalanceHistory(double curBalance, CreditCard creditCard, LocalDate date);
}
