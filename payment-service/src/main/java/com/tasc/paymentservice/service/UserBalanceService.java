package com.tasc.paymentservice.service;

import com.tasc.paymentservice.entity.UserBalance;
import com.tasc.paymentservice.repository.UserBalanceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.tass.microservice.model.ApplicationException;
import vn.tass.microservice.model.BaseResponseV2;
import vn.tass.microservice.model.ERROR;
import vn.tass.microservice.model.userauthen.UserDTO;

import java.util.Optional;

@Service
@Log4j2
public class UserBalanceService extends BaseService{

    @Autowired
    UserBalanceRepository userBalanceRepository;

    public BaseResponseV2<UserBalance> getUserBalance() throws ApplicationException {

        UserDTO userDTO = getUserDTO();

        Optional<UserBalance> userBalanceOpt = userBalanceRepository.findById(userDTO.getUserId());

        if (userBalanceOpt.isEmpty()) {
            log.debug("cannot find user balance with userId {}", userDTO.getUserId());
            throw new ApplicationException(ERROR.INVALID_PARAM, "userid invalid");
        }

        UserBalance userBalance = userBalanceOpt.get();

        BaseResponseV2<UserBalance> response = new BaseResponseV2<>();
        response.setData(userBalance);

        return response;
    }
}
