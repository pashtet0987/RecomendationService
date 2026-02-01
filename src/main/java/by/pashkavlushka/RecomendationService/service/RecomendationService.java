/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package by.pashkavlushka.RecomendationService.service;

import by.pashkavlushka.RecomendationService.dto.RecomendationDTO;
import java.util.List;

/**
 *
 * @author User
 */
public interface RecomendationService {
    List<RecomendationDTO> findByUserIdSortById(long userId);
    void updateRecomendationsForUser(RecomendationDTO recomendationDTO);
}
