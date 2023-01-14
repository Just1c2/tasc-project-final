package com.tasc.paymentservice.domain;

import com.tasc.paymentservice.entity.UserBalance;
import com.tasc.paymentservice.repository.UserBalanceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.tass.microservice.model.ApplicationException;
import vn.tass.microservice.model.ERROR;

import java.util.Optional;

@Component
@Log4j2
public class PaymentDomain {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    public UserBalance getBalanceInfoByUserId(long userId) throws ApplicationException {
        Optional<UserBalance> userBalanceOpt = userBalanceRepository.findById(userId);

        if (userBalanceOpt.isEmpty()) {
            log.debug("cannot found balance info with userId {}", userId);
            throw new ApplicationException(ERROR.INVALID_PARAM, "param balance is invalid");
        }

        return userBalanceOpt.get();
    }

    public void validateUserBalance(UserBalance userBalance, double balance) throws ApplicationException{
        if (userBalance.getBalance() < balance) {
            log.debug("balance userId {} - balance {} - request balance {}", userBalance.getUserId(), userBalance.getBalance(), balance);

            throw new ApplicationException(ERROR.BALANCE_INSUFFICIENT);
        }
    }

}
