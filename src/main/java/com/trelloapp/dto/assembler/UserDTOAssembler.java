package com.trelloapp.dto.assembler;

import com.trelloapp.domain.User;
import com.trelloapp.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserDTOAssembler {
    //TODO quitar esto y hacer que sea manejado por Spring
    UserDTOAssembler INSTANCE = Mappers.getMapper(UserDTOAssembler.class);

    @Mapping(source = "user.id", target = "user.id")
    @Mapping(source = "user.username", target = "user.email")
    @Mapping(source = "user.name", target = "user.name")
    @Mapping(source = "user.password", target = "user.password")
    @Mapping(source = "isMyself", target = "isMyself")
    UserDTO toDTO(User user, boolean isMyself);
}
