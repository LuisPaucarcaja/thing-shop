package com.user_service.feature.user.service;

import com.user_service.feature.user.entity.User;

public interface IUserLookupService{
    User getUserByEmail(String email);
}
