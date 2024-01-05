package dev.aziz.grocerystore.mappers;

import dev.aziz.grocerystore.dtos.SignUpDto;
import dev.aziz.grocerystore.dtos.UserDto;
import dev.aziz.grocerystore.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);
}
