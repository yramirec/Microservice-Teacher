package com.everis.service;

import static org.mockito.Mockito.when;

import com.everis.model.Teacher;
import com.everis.repository.TeacherRepository;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.reactivestreams.Publisher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class TeacherServiceTest {

  private final Teacher teacher1 = Teacher.builder().name("Juan").gender("Masculino")
      .dateOfBirth(new Date()).typeDocument("dni").numberDocument(109999).idFamily("Suarez")
      .build();
  private final Teacher teacher2 = Teacher.builder().name("Mariana").gender("Femenino")
      .dateOfBirth(new Date()).typeDocument("dni").numberDocument(259999).idFamily("Suarez")
      .build();
  private final Teacher teacher3 = Teacher.builder().name("Adrian").gender("Masculino")
      .dateOfBirth(new Date()).typeDocument("Carnet").numberDocument(79999).idFamily("Suarez")
      .build();

  @Mock
   private TeacherRepository teacherRepository;
  private TeacherService teacherService;

  @BeforeEach
   void setUp() {
    teacherService = new TeacherService(teacherRepository);
  }

  @Test
   void getAll() {
    when(teacherRepository.findAll()).thenReturn(Flux.just(teacher1, teacher2, teacher3));

    Flux<Teacher> actual = teacherService.findAll();

    assertResults(actual, teacher1, teacher2, teacher3);
  }

  @Test
   void getById_whenIdExists_returnCorrectTeacher() {
    when(teacherRepository.findById(teacher1.getId())).thenReturn(Mono.just(teacher1));

    Mono<Teacher> actual = teacherService.findById(teacher1.getId());

    assertResults(actual, teacher1);
  }

  @Test
   void getById_whenIdNotExist_returnEmptyMono() {
    when(teacherRepository.findById(teacher1.getId())).thenReturn(Mono.empty());

    Mono<Teacher> actual = teacherService.findById(teacher1.getId());

    assertResults(actual);
  }

  @Test
   void create() {
    when(teacherRepository.save(teacher1)).thenReturn(Mono.just(teacher1));

    Mono<Teacher> actual = teacherService.create(teacher1);

    assertResults(actual, teacher1);
  }

  @Test
   void update_whenIdExists_returnUpdatedTeacher() {
    when(teacherRepository.findById(teacher1.getId())).thenReturn(Mono.just(teacher1));
    when(teacherRepository.save(teacher1)).thenReturn(Mono.just(teacher1));

    Mono<Teacher> actual = teacherService.update(teacher1.getId(), teacher1);

    assertResults(actual, teacher1);
  }

  @Test
   void update_whenIdNotExist_returnEmptyMono() {
    when(teacherRepository.findById(teacher1.getId())).thenReturn(Mono.empty());

    Mono<Teacher> actual = teacherService.update(teacher1.getId(), teacher1);

    assertResults(actual);
  }

  @Test
   void delete_whenTeacherExists_performDeletion() {
    when(teacherRepository.findById(teacher1.getId())).thenReturn(Mono.just(teacher1));
    when(teacherRepository.delete(teacher1)).thenReturn(Mono.empty());

    Mono<Teacher> actual = teacherService.deleteById(teacher1.getId());

    assertResults(actual, teacher1);
  }

  @Test
   void delete_whenIdNotExist_returnEmptyMono() {
    when(teacherRepository.findById(teacher1.getId())).thenReturn(Mono.empty());

    Mono<Teacher> actual = teacherService.deleteById(teacher1.getId());

    assertResults(actual);
  }

  private void assertResults(Publisher<Teacher> publisher, Teacher... expectedTeachers) {
    StepVerifier
    .create(publisher)
    .expectNext(expectedTeachers)
      .verifyComplete();
  }

}
