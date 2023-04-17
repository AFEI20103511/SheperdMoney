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
    private final BalanceHistoryRepository balanceHistoryRepository;

    public Balance(BalanceHistoryRepository balanceHistoryRepository) {
        this.balanceHistoryRepository = balanceHistoryRepository;
    }

    // get the balance of this credit card on the transaction time; return 0 if no record
    private double getCurBalance(Integer creditCardId, LocalDate transactionTime) {
        BalanceHistory balanceHistory = balanceHistoryRepository.getBalanceHistoryByCreditCardIdAndDate(creditCardId, transactionTime);
        if(balanceHistory != null) {
            return balanceHistory.getBalance();
        }
        return 0;
    }

    // update the balance history given creditCard, transaction time and amount from the transaction date and onwards
    public void updateBalanceHistory(CreditCard creditCard, LocalDate transactionTime, double amount) {
        // System.out.println("id is " + creditCard.getId() + "; transactionTime is " + transactionTime + "; amount is " + amount);
        double preBalance = getPreBalance(creditCard.getId(), transactionTime);
        // deal with the transaction date balance record
        double curBalance = getCurBalance(creditCard.getId(), transactionTime);
        // if curBalance is 0, use preBalance to calculate the balance on the transaction date
        if(curBalance == 0) {
            // System.out.println("curBalance is 0, amount is " + amount);
            curBalance = preBalance + amount;
            BalanceHistory balanceHistory = new BalanceHistory(curBalance, transactionTime, creditCard);
            balanceHistoryRepository.save(balanceHistory);
        } else {
            curBalance += amount;
            balanceHistoryRepository.updateBalanceHistory(curBalance, creditCard, transactionTime);
        }
        preBalance = curBalance;

        // deal with the following dates
        for(LocalDate date = transactionTime.plusDays(1); !date.isAfter(LocalDate.now()); date = date.plusDays(1)) {
            curBalance = getCurBalance(creditCard.getId(), date);
            // curBalance means no record for this date this credit card, assume it has same balance as previous data
            if(curBalance == 0) {
                curBalance = preBalance;
                BalanceHistory balanceHistory = new BalanceHistory(curBalance, date, creditCard);
                balanceHistoryRepository.save(balanceHistory);
            } else {
                // else update the balance history if there is already a record for this date this credit card
                curBalance += amount;
                balanceHistoryRepository.updateBalanceHistory(curBalance, creditCard, date);
            }
            preBalance = curBalance;
        }
    }

    // get the balance of latest older date
    private double getPreBalance(Integer creditCardId, LocalDate transactionTime) {
        List<BalanceHistory> balanceHistoryList = balanceHistoryRepository.getBalanceHistoryByCreditCardIdAndDateBeforeOrderByDateDesc(creditCardId, transactionTime);
        if(balanceHistoryList.size() > 0) {
            return balanceHistoryList.get(0).getBalance();
        }
        return 0;
    }
}
