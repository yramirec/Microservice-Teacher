package com.everis.service;

import com.everis.model.Teacher;

import com.everis.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TeacherService {

  private final TeacherRepository teacherRepository;

  public TeacherService(TeacherRepository teacherRepository) {
    this.teacherRepository = teacherRepository;
  }

  public Mono<Teacher> create(Teacher teacher) {
    return teacherRepository.save(teacher);
  }

  public Mono<Teacher> findById(String id) {
    return teacherRepository.findById(id);
  }

  public Flux<Teacher> findAll() {
    return teacherRepository.findAll();
  }

  /**
   * Method for Update Teacher By Id.
   */
  public Mono<Teacher> update(String id, Teacher updateTeacher) {
    return teacherRepository.findById(id)
        .map(existingTeacher -> existingTeacher.toBuilder()
        .name(updateTeacher.getName())
        .gender(updateTeacher.getGender())
        .dateOfBirth(updateTeacher.getDateOfBirth())
        .typeDocument(updateTeacher.getTypeDocument())
        .numberDocument(updateTeacher.getNumberDocument())
        .idFamily(updateTeacher.getIdFamily())
        .build())
        .flatMap(teacherRepository::save);
  }


  public Mono<Teacher> deleteById(String id) {
    return teacherRepository.findById(id)
        .flatMap(teacher -> teacherRepository.delete(teacher).then(Mono.just(teacher)));
  }

  public Flux<Teacher> findByName(String name) {
    return teacherRepository.findByName(name);
  }

  public Flux<Teacher> findByNumberDocument(int numberDocument) {
    return teacherRepository.findByNumberDocument(numberDocument);
  }

}
