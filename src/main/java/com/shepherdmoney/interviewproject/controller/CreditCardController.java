package com.shepherdmoney.interviewproject.controller;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import com.shepherdmoney.interviewproject.repository.BalanceHistoryRepository;
import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import com.shepherdmoney.interviewproject.services.Balance;
import com.shepherdmoney.interviewproject.vo.request.AddCreditCardToUserPayload;
import com.shepherdmoney.interviewproject.vo.request.UpdateBalancePayload;
import com.shepherdmoney.interviewproject.vo.response.CreditCardView;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class CreditCardController {

    // TODO: wire in CreditCard repository here (~1 line)
    private final UserRepository userRepository;
    private final CreditCardRepository creditCardRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;

    public CreditCardController(UserRepository userRepository, CreditCardRepository creditCardRepository, BalanceHistoryRepository balanceHistoryRepository) {
        this.userRepository = userRepository;
        this.creditCardRepository = creditCardRepository;
        this.balanceHistoryRepository = balanceHistoryRepository;
    }

    @PostMapping("/credit-card")
    @Transactional
    public ResponseEntity<Integer> addCreditCardToUser(@RequestBody AddCreditCardToUserPayload payload) {
        // TODO: Create a credit card entity, and then associate that credit card with user with given userId
        //       Return 200 OK with the credit card id if the user exists and credit card is successfully associated with the user
        //       Return other appropriate response code for other exception cases
        //       Do not worry about validating the card number, assume card number could be any arbitrary format and length
        // check if the user already exists
        if(userRepository.findUserById(payload.getUserId()) == null) {
            // no such user, return code 400
            System.out.println("No such user");
            return ResponseEntity.status(400).body(-1);
        }
        // check if the credit card already attached to the user, assume one card can be attached to one person
        if(creditCardRepository.checkCreditCardAttached(payload.getUserId(), payload.getCardNumber()) != null) {
            // the credit card already attached to the user
            System.out.println("the credit card already attached to the user");
            return ResponseEntity.status(400).body(-1);
        }
        // create a new credit card and attach it to the user
        User user = userRepository.findUserById(payload.getUserId());
        CreditCard creditCard = new CreditCard(payload.getCardIssuanceBank(), payload.getCardNumber(), user);
        creditCardRepository.save(creditCard);
        return ResponseEntity.status(200).body(creditCard.getId());
    }

    @GetMapping("/credit-card:all")
    public ResponseEntity<List<CreditCardView>> getAllCardOfUser(@RequestParam int userId) {
        // TODO: return a list of all credit card associated with the given userId, using CreditCardView class
        //       if the user has no credit card, return empty list, never return null
        System.out.println("Going to get all credit cards of user: " + userId);
        List<CreditCard> creditCards = creditCardRepository.getAllCreditCards(userId);
        System.out.println("Got all credit cards of user: " + userId);
        List<CreditCardView> creditCardViews = new LinkedList<>();
        System.out.println("Going to convert credit cards to credit card views");
        for(CreditCard card:creditCards) creditCardViews.add(new CreditCardView(card.getIssuanceBank(), card.getNumber()));
        return ResponseEntity.status(200).body(creditCardViews);
 }

    @GetMapping("/credit-card:user-id")
    public ResponseEntity<List<Integer>> getUserIdForCreditCard(@RequestParam String creditCardNumber) {
        // TODO: Given a credit card number, efficiently find whether there is a user associated with the credit card
        //       If so, return the user id in a 200 OK response. If no such user exists, return 400 Bad Request
        List<Integer> users = creditCardRepository.findAllUsersByCreditCardNumber(creditCardNumber);
        return users.isEmpty() ? ResponseEntity.status(400).body(new LinkedList<>()) : ResponseEntity.status(200).body(users);
    }

    @PostMapping("/credit-card:update-balance")
    @Transactional
    public ResponseEntity<String> updateBalance(@RequestBody UpdateBalancePayload[] payload) {
        //TODO: Given a list of transactions, update credit cards' balance history.
        //      For example: if today is 4/12, a credit card's balanceHistory is [{date: 4/12, balance: 110}, {date: 4/10, balance: 100}],
        //      Given a transaction of {date: 4/10, amount: 10}, the new balanceHistory is
        //      [{date: 4/12, balance: 120}, {date: 4/11, balance: 110}, {date: 4/10, balance: 110}]
        //      Return 200 OK if update is done and successful, 400 Bad Request if the given card number
        //        is not associated with a card.

        // get all credit card numbers and check if they exist
        for(UpdateBalancePayload updateBalancePayload:payload) {
            String creditCardNumber = updateBalancePayload.getCreditCardNumber();
            if(creditCardRepository.checkCreditCardExists(creditCardNumber)== null) {
                // no such credit card
                return ResponseEntity.status(400).body(creditCardNumber + " does not exist");
            }
        }
        // update the balance history
        Balance balance = new Balance(balanceHistoryRepository);
        for(UpdateBalancePayload updateBalancePayload:payload) {
            String creditCardNumber = updateBalancePayload.getCreditCardNumber();
            CreditCard creditCard = creditCardRepository.checkCreditCardExists(creditCardNumber);
            balance.updateBalanceHistory(creditCard, updateBalancePayload.getTransactionTime(), updateBalancePayload.getCurrentBalance());
        }
        return ResponseEntity.status(200).body("Balance updated");
    }
    
}
