package com.user_service.feature.user.mapper;

import com.user_service.feature.user.dto.UserDto;
import com.user_service.feature.user.entity.User;
import com.user_service.feature.user.dto.CreateUserRequest;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

   // Convierte de Request a entidad
    User toEntity(CreateUserRequest userRequest);

}
