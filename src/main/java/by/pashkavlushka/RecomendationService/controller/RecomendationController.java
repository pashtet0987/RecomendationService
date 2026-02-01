/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.RecomendationService.controller;

import by.pashkavlushka.RecomendationService.dto.RecomendationDTO;
import by.pashkavlushka.RecomendationService.service.RecomendationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recomendations")
public class RecomendationController {
    
    private RecomendationService recomendationService;
    
    @Autowired
    public RecomendationController(RecomendationService recomendationService) {
        this.recomendationService = recomendationService;
    }
    
    @GetMapping("/")
    public List<RecomendationDTO> recomendations(@RequestParam("userId") Long userId){
        return recomendationService.findByUserIdSortById(userId);
    }
}
