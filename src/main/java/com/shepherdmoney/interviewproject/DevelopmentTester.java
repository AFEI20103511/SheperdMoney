package com.shepherdmoney.interviewproject;

import com.shepherdmoney.interviewproject.repository.CreditCardRepository;
import com.shepherdmoney.interviewproject.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DevelopmentTester implements ApplicationRunner {
    UserRepository userRepository;
    CreditCardRepository creditCardRepository;

    public DevelopmentTester(UserRepository userRepository, CreditCardRepository creditCardRepository) {
        this.userRepository = userRepository;
        this.creditCardRepository = creditCardRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        // add credit card to user
//        System.out.println("I am going to add a credit card to user");
//        creditCardRepository.saveCreditCard(252, "HSBC", "123456789");
//        System.out.println("I have added a credit card to user");
    }
}
