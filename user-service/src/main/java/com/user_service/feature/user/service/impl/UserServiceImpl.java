package com.user_service.feature.user.service.impl;

import com.user_service.common.event.producer.EmailVerificationProducer;
import com.user_service.feature.user.dto.UserDto;
import com.user_service.feature.user.entity.Role;
import com.user_service.feature.user.entity.User;
import com.user_service.feature.user.enums.AccountStatus;
import com.user_service.feature.user.enums.AuthProvider;
import com.user_service.feature.user.enums.RoleName;

import com.user_service.feature.user.exception.EmailAlreadyRegisteredException;
import com.user_service.common.exception.ResourceNotFoundException;
import com.user_service.feature.user.mapper.UserMapper;
import com.user_service.feature.user.repository.IRoleRepository;
import com.user_service.feature.user.repository.IUserRepository;
import com.user_service.feature.user.dto.CreateUserRequest;
import com.user_service.feature.user.dto.UpdateUserRequest;
import com.user_service.feature.user.service.IUserService;
import com.user_service.feature.user.service.helper.UserAvatarService;
import com.user_service.feature.verificationToken.entity.VerificationToken;
import com.user_service.feature.verificationToken.service.IVerificationTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.user_service.common.constants.CacheConstants.GET_USER_BY_ID;
import static com.user_service.common.constants.ErrorMessageConstants.EMAIL_ALREADY_REGISTERED;
import static com.user_service.common.constants.ErrorMessageConstants.EMAIL_REGISTERED_BUT_INACTIVE;
import static com.user_service.common.constants.GeneralConstants.*;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    private final IRoleRepository roleRepository;


    private final EmailVerificationProducer emailVerificationProducer;
    private final UserAvatarService userAvatarService;
    private final IVerificationTokenService verificationTokenService;

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

        VerificationToken verificationToken = verificationTokenService.createToken(savedUser);
        emailVerificationProducer.sendVerificationTokenByEmail(verificationToken.getUser().getEmail(),
                verificationToken.getToken());
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
