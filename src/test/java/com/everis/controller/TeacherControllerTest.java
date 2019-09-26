package com.everis.controller;

import static org.mockito.Mockito.when;

import com.everis.model.Teacher;
import com.everis.service.TeacherService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
class TeacherControllerTest {

  @Mock
  private TeacherService teacherService;
  private WebTestClient client;
  private List<Teacher> expectedTeachers;

  @BeforeEach
   void setUp() {
    client = WebTestClient.bindToController(new TeacherController(teacherService)).configureClient()
    .baseUrl("/teachers").build();

    expectedTeachers = Arrays.asList(
     Teacher.builder().id("1").name("Juan").gender("Masculino").dateOfBirth(new Date())
     .typeDocument("dni").numberDocument(109999).idFamily("Suarez").build(),
    Teacher.builder().id("2").name("Mariana").gender("Femenino").dateOfBirth(new Date())
    .typeDocument("dni").numberDocument(259999).idFamily("Suarez").build(),
    Teacher.builder().id("3").name("Adrian").gender("Masculino").dateOfBirth(new Date())
    .typeDocument("Carnet").numberDocument(79999).idFamily("Suarez").build());
  }

  @Test
   void getAllTeachers() {
    when(teacherService.findAll()).thenReturn(Flux.fromIterable(expectedTeachers));

    client.get().uri("/").exchange().expectStatus().isOk().expectBodyList(Teacher.class)
    .isEqualTo(expectedTeachers);
  }

  @Test
   void getTeacherById_whenTeacherExists_returnCorrectTeacher() {
    Teacher expectedTeacher = expectedTeachers.get(0);

    when(teacherService.findById(expectedTeacher.getId())).thenReturn(Mono.just(expectedTeacher));

    client.get().uri("/{id}", expectedTeacher.getId())
    .exchange().expectStatus().isOk().expectBody(Teacher.class)
    .isEqualTo(expectedTeacher);
  }

  @Test
   void getTeacherById_whenTeacherNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    when(teacherService.findById(id)).thenReturn(Mono.empty());

    client.get().uri("/{id}", id).exchange()
    .expectStatus().isNotFound();
  }

  @Test
   void addTeacher() {
    Teacher expectedTeacher = expectedTeachers.get(0);
    when(teacherService.create(expectedTeacher)).thenReturn(Mono.just(expectedTeacher));

    client.post().uri("/").body(Mono.just(expectedTeacher), Teacher.class).exchange()
    .expectStatus().isCreated()
    .expectBody(Teacher.class).isEqualTo(expectedTeacher);
  }

  @Test
   void updateTeacher_whenTeacherExists_performUpdate() {
    Teacher expectedTeacher = expectedTeachers.get(0);
    when(teacherService.update(expectedTeacher.getId(), expectedTeacher))
      .thenReturn(Mono.just(expectedTeacher));

    client.put().uri("/{id}", expectedTeacher.getId())
    .body(Mono.just(expectedTeacher), Teacher.class).exchange()
    .expectStatus().isOk()
    .expectBody(Teacher.class).isEqualTo(expectedTeacher);
  }

  @Test
   void updateTeacher_whenTeacherNotExist_returnNotFound() {
    String id = "NOT_EXIST_ID";
    Teacher expectedTeacher = expectedTeachers.get(0);
    when(teacherService.update(id, expectedTeacher))
      .thenReturn(Mono.empty());

    client.put().uri("/{id}", id).body(Mono.just(expectedTeacher), Teacher.class)
    .exchange()
    .expectStatus().isNotFound();
  }

  @Test
   void deleteTeacher_whenTeacherExists_performDeletion() {
    Teacher teacherToDelete = expectedTeachers.get(0);
    when(teacherService.deleteById(teacherToDelete.getId()))
      .thenReturn(Mono.just(teacherToDelete));

    client.delete().uri("/{id}", teacherToDelete.getId()).exchange()
    .expectStatus().isOk();
  }
  
  @Test
   void deleteTeacher_whenIdNotExist_returnNotFound() {
    Teacher teacherToDelete = expectedTeachers.get(0);
    when(teacherService.deleteById(teacherToDelete.getId()))
      .thenReturn(Mono.empty());

    client.delete().uri("/{id}", teacherToDelete.getId()).exchange()
    .expectStatus().isNotFound();
  }

  @Test
   void searchByName() {
    String name = "Mariana";
    List<Teacher> expectedFilteredTeachers = Arrays.asList(expectedTeachers.get(1), 
        expectedTeachers.get(1));
    when(teacherService.findByName(name))
      .thenReturn(Flux.fromIterable(expectedFilteredTeachers));
    
    client.get().uri("/searchByName/{name}", name).exchange()
    .expectStatus().isOk()
    .expectBodyList(Teacher.class).isEqualTo(expectedFilteredTeachers);
  }

  @Test
   void searchByDocument() {
    int numberDocument = 109999;
    List<Teacher> expectedFilteredTeachers = Arrays.asList(expectedTeachers.get(0), 
        expectedTeachers.get(1));
    when(teacherService.findByNumberDocument(numberDocument))
      .thenReturn(Flux.fromIterable(expectedFilteredTeachers));
    
    client.get().uri("/searchByDocument/{numberDocument}", numberDocument).exchange()
    .expectStatus().isOk()
    .expectBodyList(Teacher.class).isEqualTo(expectedFilteredTeachers);
  }


}
