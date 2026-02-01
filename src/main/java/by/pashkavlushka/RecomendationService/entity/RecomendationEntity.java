/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package by.pashkavlushka.RecomendationService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.Instant;

//a user will have at most 3 recomended categories
@Entity
@Table(name = "recomendation", schema = "online_shop")
public class RecomendationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recomendation_generator")
    @SequenceGenerator(name = "recomendation_generator", sequenceName = "recomendation_generator_seq", schema = "online_shop", initialValue = 1, allocationSize = 1)
    private long id;
    
    private long userId;
    
    private String category;

    public RecomendationEntity() {
    }

    public RecomendationEntity(long userId, String category) {
        this.userId = userId;
        this.category = category;
    }
    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
