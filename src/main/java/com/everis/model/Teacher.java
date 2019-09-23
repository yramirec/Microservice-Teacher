package com.everis.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "teachers")
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Teacher {

  @EqualsAndHashCode.Exclude
  @Id
private String id;

  @EqualsAndHashCode.Exclude
  @NotBlank(message = "'Name' is required")
private String Name;

  @EqualsAndHashCode.Exclude
  @NotBlank(message = "'Geneder' is required")
private String gender;

  @EqualsAndHashCode.Exclude
  @JsonFormat(pattern = "yyyy-MM-dd")
private Date dateOfBirth;

  @EqualsAndHashCode.Exclude
  @NotBlank(message = "'Type Document' is required")
private String typeDocument;

  @EqualsAndHashCode.Exclude
  @NotNull(message = "'Number Document' is required")
  @Positive(message = "'Number' must be greater than zero")
private int numberDocument;

  @NotBlank(message = "'Family' is required")
  private String idFamily;  

}
