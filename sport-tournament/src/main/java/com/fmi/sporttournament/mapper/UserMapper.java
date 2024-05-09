package com.fmi.sporttournament.mapper;

import com.fmi.sporttournament.Dto.UserDto;
import com.fmi.sporttournament.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userToDto(User user);
    List<UserDto> usersToUserDtos(List<User> user);
}
