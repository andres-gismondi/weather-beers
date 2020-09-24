package com.api.beer.beers.users.mapper;

import com.api.beer.beers.users.dto.UserDto;
import com.api.beer.beers.users.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setAge(user.getAge());
        userDto.setSurName(user.getSurName());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public User mapToModel(UserDto userDto) {
        User user = new User();
        user.setAge(userDto.getAge());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setSurName(userDto.getSurName());
        user.setPassword(userDto.getPassword());
        return user;
    }

}
