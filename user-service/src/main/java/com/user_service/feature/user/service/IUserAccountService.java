package com.user_service.feature.user.service;

import com.user_service.feature.user.enums.AccountStatus;

public interface IUserAccountService {
    void updateUserAccountStatus(Long userId, AccountStatus accountStatus);

}
