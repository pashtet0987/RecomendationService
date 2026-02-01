/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package by.pashkavlushka.RecomendationService.mapper;

import by.pashkavlushka.RecomendationService.dto.RecomendationDTO;
import by.pashkavlushka.RecomendationService.entity.RecomendationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RecomendationMapper {
    RecomendationEntity dtoToEntity(RecomendationDTO recomendationDTO);
    RecomendationDTO entityToDTO(RecomendationEntity recomendationEntity);
}
