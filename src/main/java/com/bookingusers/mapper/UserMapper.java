package com.bookingusers.mapper;

import com.bookingusers.model.dto.CreateUserDto;
import com.bookingusers.model.dto.ReadUserDto;
import com.bookingusers.model.dto.UpdateUserDto;
import com.bookingusers.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    ReadUserDto toDto(User user);

    User toEntity(CreateUserDto userDto);

    void updateEntityFromDto(UpdateUserDto userDto, @MappingTarget User user);

}
