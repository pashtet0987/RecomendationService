/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.RecomendationService.kafka;

import by.pashkavlushka.RecomendationService.dto.RecomendationDTO;
import by.pashkavlushka.RecomendationService.service.RecomendationService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.message.ShareAcknowledgeRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

@Service
@KafkaListener(topics = {"recomendations-topic"})
public class KafkaRecomendationListener {
    
    private RecomendationService recomendationService;

    @Autowired
    public KafkaRecomendationListener(RecomendationService recomendationService) {
        this.recomendationService = recomendationService;
    }
    
    @KafkaHandler
    public void updateRecomendations(RecomendationDTO record, Acknowledgment ack){
        recomendationService.updateRecomendationsForUser(record);
        
    }
}
