package com.jmunoz.springboot.onetoone.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table( name = "clients_details")
public class ClientDetails {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private boolean isPremium;
  private Integer points;
  
  public ClientDetails() {
  }

  public ClientDetails(boolean isPremium, Integer points) {
    this.isPremium = isPremium;
    this.points = points;
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public boolean isPremium() {
    return isPremium;
  }
  public void setPremium(boolean isPremium) {
    this.isPremium = isPremium;
  }
  public Integer getPoints() {
    return points;
  }
  public void setPoints(Integer points) {
    this.points = points;
  }

  @Override
  public String toString() {
    return "{id=" + id +
           ", isPremium=" + isPremium +
           ", points=" + points +
           "}";
  }
}
