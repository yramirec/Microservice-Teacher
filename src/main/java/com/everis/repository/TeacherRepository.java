package com.everis.repository;

import com.everis.model.Teacher;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;

public interface TeacherRepository extends ReactiveMongoRepository<Teacher, String> {

  Flux<Teacher> findByName(String name);
  
  Flux<Teacher> findByNumberDocument(int numberDocument);

}
