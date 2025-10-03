package com.user_service.feature.user.service.impl;

import com.user_service.feature.user.enums.AccountStatus;
import com.user_service.feature.user.repository.IUserRepository;
import com.user_service.feature.user.service.IUserAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserAccountServiceImpl implements IUserAccountService {

    private final IUserRepository userRepository;

    @Override
    public void updateUserAccountStatus(Long userId, AccountStatus accountStatus) {
        userRepository.updateAccountStatus(userId, accountStatus);
    }
}
