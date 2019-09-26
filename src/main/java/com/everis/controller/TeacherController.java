package com.everis.controller;

import com.everis.model.Teacher;
import com.everis.service.TeacherService;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

  private final TeacherService teacherService;

  public TeacherController(TeacherService teacherService) {
    this.teacherService = teacherService;
  }

  @GetMapping
   public Flux<Teacher> list() {
    return teacherService.findAll();
  }

  /**
   * Method for Find By Id.
   */
  @GetMapping("{id}")
    public Mono<ResponseEntity<Teacher>> findById(@PathVariable String id) {
    return teacherService.findById(id)
       .map(ResponseEntity::ok)
       .defaultIfEmpty(ResponseEntity.notFound()
       .build());
  }

  /**
   * Method for Create Teacher.
   */
  @PostMapping
   public Mono<ResponseEntity<Teacher>> createTeacher(@RequestBody @Valid Teacher teacher) {
    Teacher teacherToCrete = teacher.toBuilder().id(null).build();
    return teacherService.create(teacherToCrete).map(
        newTeacher -> ResponseEntity.created(URI.create("/teachers/" + newTeacher.getId()))
        .body(newTeacher));
  }

  @PutMapping("{id}")
   public Mono<ResponseEntity<Teacher>> updateTeacher(@PathVariable String id, 
      @RequestBody @Valid Teacher teacher) {
    return teacherService.update(id, teacher).map(ResponseEntity::ok)
       .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @DeleteMapping("{id}")
   public Mono<ResponseEntity<Void>> deleteTeacher(@PathVariable String id) {
    return teacherService.deleteById(id).map(r -> ResponseEntity.ok().<Void>build())
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping("/searchByName/{name}")
   public Flux<Teacher> findByName(@PathVariable String name) {
    return teacherService.findByName(name);
  }

  @GetMapping("/searchByDocument/{numberDocument}")
   public Flux<Teacher> findByNumberDocument(@PathVariable int numberDocument) {
    return teacherService.findByNumberDocument(numberDocument);
  }

}
