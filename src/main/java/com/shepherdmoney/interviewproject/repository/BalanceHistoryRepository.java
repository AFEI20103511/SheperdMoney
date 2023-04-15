package com.shepherdmoney.interviewproject.repository;
import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Repository("BalanceHistoryRepository")
public interface BalanceHistoryRepository {

    @Query("SELECT b FROM BalanceHistory b WHERE b.creditCard.id = ?1 AND b.date = ?2")
    BalanceHistory getBalanceHistoryByCreditCardIdAndDate(Integer creditCardId, LocalDate transactionTime);

    // get the earliest older balance
    @Query("SELECT b FROM BalanceHistory b WHERE b.creditCard.id = ?1 AND b.date < ?2 ORDER BY b.date DESC")
    List<BalanceHistory> getBalanceHistoryByCreditCardIdAndDateBeforeOrderByDateDesc(Integer creditCardId, LocalDate transactionTime);

}
