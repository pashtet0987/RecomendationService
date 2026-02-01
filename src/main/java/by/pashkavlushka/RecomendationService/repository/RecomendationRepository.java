/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.RecomendationService.repository;

import by.pashkavlushka.RecomendationService.entity.RecomendationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface RecomendationRepository extends JpaRepository<RecomendationEntity, Long> {
    
    @Query("select e from RecomendationEntity e where e.userId = :userId order by e.userId desc")
    List<RecomendationEntity> findAllByUserIdSortById(@Param("userId") long userId);
}
