package com.learning.student.studentbdd.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentSearch {
    private String id;
    private String firstName;
    private String lastName;
    private String cnp;
    private boolean isValid;
}
