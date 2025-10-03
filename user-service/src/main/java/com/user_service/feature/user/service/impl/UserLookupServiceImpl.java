package com.user_service.feature.user.service.impl;

import com.user_service.common.exception.ResourceNotFoundException;
import com.user_service.feature.user.entity.User;
import com.user_service.feature.user.repository.IUserRepository;
import com.user_service.feature.user.service.IUserLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.user_service.common.constants.GeneralConstants.EMAIL_FIELD;

@RequiredArgsConstructor
@Service
public class UserLookupServiceImpl implements IUserLookupService {

    private final IUserRepository userRepository;

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), EMAIL_FIELD, email));
    }
}
