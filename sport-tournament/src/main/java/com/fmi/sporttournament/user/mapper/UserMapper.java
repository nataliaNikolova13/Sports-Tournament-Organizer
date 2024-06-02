package com.fmi.sporttournament.user.mapper;

import com.fmi.sporttournament.user.dto.response.UserDto;
import com.fmi.sporttournament.user.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToDto(User user);
    List<UserDto> usersToUserDtos(List<User> user);
}
