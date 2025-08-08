package com.microservice_auth.feature.user.service.impl;

import com.microservice_auth.feature.user.dto.UserDto;
import com.microservice_auth.feature.user.entity.Role;
import com.microservice_auth.feature.user.entity.User;
import com.microservice_auth.feature.user.enums.AccountStatus;
import com.microservice_auth.feature.user.enums.AuthProvider;
import com.microservice_auth.feature.user.enums.RoleName;

import com.microservice_auth.feature.user.exception.EmailAlreadyRegisteredException;
import com.microservice_auth.common.exception.ResourceNotFoundException;
import com.microservice_auth.feature.user.mapper.UserMapper;
import com.microservice_auth.feature.user.repository.IRoleRepository;
import com.microservice_auth.feature.user.repository.IUserRepository;
import com.microservice_auth.feature.user.dto.CreateUserRequest;
import com.microservice_auth.feature.user.dto.UpdateUserRequest;
import com.microservice_auth.feature.user.service.IUserService;
import com.microservice_auth.feature.user.service.helper.UserAvatarService;
import com.microservice_auth.feature.verificationToken.service.helper.TokenEmailHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.microservice_auth.common.constants.CacheConstants.GET_USER_BY_ID;
import static com.microservice_auth.common.constants.ErrorMessageConstants.EMAIL_ALREADY_REGISTERED;
import static com.microservice_auth.common.constants.ErrorMessageConstants.EMAIL_REGISTERED_BUT_INACTIVE;
import static com.microservice_auth.common.constants.GeneralConstants.*;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private final IRoleRepository roleRepository;


    private final TokenEmailHelper tokenEmailHelper;
    private final UserAvatarService userAvatarService;


    @Override
    @Cacheable(GET_USER_BY_ID)
    public UserDto getUserById(Long userId) {
        return userMapper.toDto(findUserById(userId));
    }



    @Override
    @Transactional
    public UserDto createUser(CreateUserRequest userRequest) {
        validateEmailNotRegistered(userRequest.getEmail());

        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User user = userMapper.toEntity(userRequest);
        user.setRole(getDefaultUserRole());
        user.setAccountStatus(AccountStatus.PENDING_VERIFICATION);
        user.setProvider(AuthProvider.LOCAL);

        User savedUser = userRepository.save(user);

        String avatarUrl = userAvatarService.handleAvatarChange(
                userRequest.getAvatarFile(), false, null, savedUser.getId()
        );
        savedUser.setAvatarUrl(avatarUrl);

        tokenEmailHelper.createAndSendToken(savedUser);
        return userMapper.toDto(userRepository.save(savedUser));
    }


    @Override
    @Transactional
    @CacheEvict(value = GET_USER_BY_ID, key = "#userId")
    public UserDto updateUser(Long userId, UpdateUserRequest userRequest) {
        User existingUser = findUserById(userId);

        String newAvatarUrl = userAvatarService.handleAvatarChange(
                userRequest.getAvatarFile(),
                userRequest.isRemoveAvatar(),
                existingUser.getAvatarUrl(),
                userId
        );

        existingUser.setAvatarUrl(newAvatarUrl);
        existingUser.setFullName(userRequest.getFullName());
        existingUser.setPhoneNumber(userRequest.getPhoneNumber());

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }


    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), EMAIL_FIELD, email));
    }

    @Override
    @CacheEvict(value = GET_USER_BY_ID, key = "#userId")
    public void updateUserAccountStatus(Long userId, AccountStatus accountStatus){
        userRepository.updateAccountStatus(userId, accountStatus);
    }

    @Override
    public Role getDefaultUserRole() {
        return roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException(Role.class.getSimpleName(), NAME_FIELD,
                        RoleName.ROLE_USER.name()));
    }

    private User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), ID_FIELD, id));
    }

    private void validateEmailNotRegistered(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            if (user.getAccountStatus().equals(AccountStatus.ACTIVE)) {
                throw new EmailAlreadyRegisteredException(EMAIL_ALREADY_REGISTERED);
            }
            throw new EmailAlreadyRegisteredException(EMAIL_REGISTERED_BUT_INACTIVE);
        });
    }

}
