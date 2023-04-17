package com.shepherdmoney.interviewproject.model;

import java.time.Instant;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "BalanceHistory")
public class BalanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    
    private LocalDate date;

    private double balance;

    // credit card's history.
    @ManyToOne
    @JoinColumn(name = "credit_card_id")
    private CreditCard creditCard;

    public BalanceHistory(double curBalance, LocalDate date, CreditCard creditCard) {
        this.balance = curBalance;
        this.date = date;
        this.creditCard = creditCard;
    }
}
