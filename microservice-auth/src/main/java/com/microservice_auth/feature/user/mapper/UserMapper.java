package com.microservice_auth.feature.user.mapper;

import com.microservice_auth.feature.user.dto.UserDto;
import com.microservice_auth.feature.user.entity.User;
import com.microservice_auth.feature.user.dto.CreateUserRequest;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

   // Convierte de Request a entidad
    User toEntity(CreateUserRequest userRequest);

}
