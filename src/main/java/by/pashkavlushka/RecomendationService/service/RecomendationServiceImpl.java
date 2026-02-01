/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.RecomendationService.service;

import by.pashkavlushka.RecomendationService.mapper.RecomendationMapper;
import by.pashkavlushka.RecomendationService.dto.RecomendationDTO;
import by.pashkavlushka.RecomendationService.entity.RecomendationEntity;
import by.pashkavlushka.RecomendationService.repository.RecomendationRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RecomendationServiceImpl implements RecomendationService{
    
    private RecomendationRepository recomendationRepository;
    private RecomendationMapper recomendationMapper;
    private int amount;
    

    @Autowired
    public RecomendationServiceImpl(RecomendationRepository recomendationRepository, RecomendationMapper recomendationMapper, @Value("${recomendations.max.per.user}") int amount) {
        this.recomendationRepository = recomendationRepository;
        this.recomendationMapper = recomendationMapper;
        this.amount = amount;
    }
    
    @Override
    @Transactional
    public List<RecomendationDTO> findByUserIdSortById(long userId){
        return recomendationRepository.findAllByUserIdSortById(userId).stream().map(recomendationMapper::entityToDTO).toList();
    }

    @Override
    @Transactional
    public void updateRecomendationsForUser(RecomendationDTO recomendationDTO) {
        List<RecomendationEntity> recomendationsByUser = recomendationRepository.findAllByUserIdSortById(recomendationDTO.getUserId());
        if(recomendationsByUser.size() > amount){
            recomendationsByUser = recomendationsByUser.stream().limit(3).toList();
        }
        
        if(recomendationsByUser.size() == amount) {
            for(int i = amount - 2; i >= 0; i--) {
                RecomendationEntity entity = recomendationsByUser.get(i + 1);
                entity.setCategory(recomendationsByUser.get(i).getCategory());
            }
            recomendationsByUser.get(0).setCategory(recomendationDTO.getCategory());
        } else {
            recomendationsByUser.add(recomendationMapper.dtoToEntity(recomendationDTO));
        }
        
        recomendationRepository.saveAllAndFlush(recomendationsByUser);
    }
    
    
}
