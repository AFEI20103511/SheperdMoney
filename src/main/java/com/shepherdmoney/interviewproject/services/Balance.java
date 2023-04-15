package com.shepherdmoney.interviewproject.services;

import com.shepherdmoney.interviewproject.model.BalanceHistory;
import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.repository.BalanceHistoryRepository;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class Balance {
    private BalanceHistoryRepository balanceHistoryRepository;
    private CreditCardRepository creditCardRepository;

    public Balance(BalanceHistoryRepository balanceHistoryRepository, CreditCardRepository creditCardRepository) {
        this.balanceHistoryRepository = balanceHistoryRepository;
        this.creditCardRepository = creditCardRepository;
    }

    public void updateBalanceHistory(String creditCardNumber, LocalDate transactionTime, double amount) {


    }

    // get the balance of this credit card on the transaction time
    private double getCurBalance(Integer creditCardId, LocalDate transactionTime) {
        BalanceHistory balanceHistory = balanceHistoryRepository.getBalanceHistoryByCreditCardIdAndDate(creditCardId, transactionTime);
        if(balanceHistory != null) {
            return balanceHistory.getBalance();
        }
        return 0;
    }

    // get the latest older balance
    private double getPreBalance(Integer creditCardId, LocalDate transactionTime) {
        List<BalanceHistory> balanceHistoryList = balanceHistoryRepository.getBalanceHistoryByCreditCardIdAndDateBeforeOrderByDateDesc(creditCardId, transactionTime);
        if(balanceHistoryList.size() > 0) {
            return balanceHistoryList.get(0).getBalance();
        }
        return 0;
    }
}
