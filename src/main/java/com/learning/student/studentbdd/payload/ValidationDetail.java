package com.learning.student.studentbdd.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ValidationDetail {
    private String studentId;
    private String errorName;
    private String errorDescription;
}
