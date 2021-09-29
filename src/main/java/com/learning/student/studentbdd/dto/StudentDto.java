package com.learning.student.studentbdd.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDto {
    private String id;
    private String firstName;
    private String lastName;
    private String cnp;
    private LocalDate dateOfBirth;
    private AddressDto address;
    private List<GradeDto> grades;
    private boolean isValid;
}
