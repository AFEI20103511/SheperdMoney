package com.shepherdmoney.interviewproject.repository;

import com.shepherdmoney.interviewproject.model.CreditCard;
import com.shepherdmoney.interviewproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Crud repository to store credit cards
 */
@Repository("CreditCardRepo")
public interface CreditCardRepository extends JpaRepository<CreditCard, Integer> {

    // get all credit cards for a user
    @Query(value = "SELECT * FROM CREDIT_CARD WHERE user_id = :userId", nativeQuery = true)
    List<CreditCard> getAllCreditCards(int userId);

    // check if a credit card exists for a user
    @Query(value = "SELECT * FROM CREDIT_CARD WHERE user_id = :userId AND number = :cardNumber", nativeQuery = true)
    CreditCard checkCreditCardAttached(int userId, String cardNumber);

    // get all users with a given credit card number
    @Query(value = "SELECT user_id FROM CREDIT_CARD WHERE number = :creditCardNumber", nativeQuery = true)
    List<Integer> findAllUsersByCreditCardNumber(String creditCardNumber);

    // check if a credit card exists
    @Query(value = "SELECT * FROM CREDIT_CARD WHERE number = :creditCardNumber", nativeQuery = true)
    CreditCard checkCreditCardExists(String creditCardNumber);
}
