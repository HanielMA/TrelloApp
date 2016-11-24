package com.trelloapp.dto.assembler;

import mierda.Ejemplo;
import mierda.EjemploDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EjemploDTOMapper {
    EjemploDTOMapper INSTANCE = Mappers.getMapper(EjemploDTOMapper.class);

    @Mapping(source = "a1", target = "a1")
    @Mapping(source = "a2", target = "a2")
    EjemploDTO toDTO(Ejemplo ej);
}
