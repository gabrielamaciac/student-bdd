package com.learning.student.studentbdd.util;

import com.learning.student.studentbdd.payload.Address;
import com.learning.student.studentbdd.payload.Grade;
import com.learning.student.studentbdd.payload.Mark;
import com.learning.student.studentbdd.payload.Student;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class TestData {

    public static final String TEST_FIRST_NAME = "BDD";
    public static final String TEST_LAST_NAME = "Test";
    public static final String DATE_OF_BIRTH = "1987-12-10";
    public static final String TEST_CITY = "City";
    public static final String TEST_COUNTRY = "Country";
    public static final String TEST_NUMBER = "Number";
    public static final String TEST_STREET = "Street";
    public static final String TEST_SUBJECT = "Subject";
    public static final String DATE_RECEIVED = "2020-03-12";
    public static final String TEST_CNP = UUID.randomUUID().toString().replace("-", "");

    private TestData() {
    }

    public static Student getStudent() {
        Address address = new Address(TEST_CITY, TEST_COUNTRY, TEST_NUMBER, TEST_STREET);
        Mark mark = new Mark(DATE_RECEIVED, 10.0);
        Grade grade = new Grade(TEST_SUBJECT, Collections.singletonList(mark));
        return new Student(TEST_FIRST_NAME, TEST_LAST_NAME, TEST_CNP, DATE_OF_BIRTH, address, Collections.singletonList(grade));
    }

    public static Student getInvalidStudent(String uniqueCnp) {
        Address address = new Address(TEST_CITY, TEST_COUNTRY, TEST_NUMBER, TEST_STREET);
        Mark firstMark = new Mark(DATE_RECEIVED, 4.0);
        Mark secondMark = new Mark(DATE_RECEIVED, 3.0);
        Grade grade = new Grade(TEST_SUBJECT, Arrays.asList(firstMark, secondMark));
        return new Student(TEST_FIRST_NAME, TEST_LAST_NAME, uniqueCnp, DATE_OF_BIRTH, address, Collections.singletonList(grade));
    }

    public static String getStudentXml(String cnp) {
        return "<student>\n" +
                "    <firstName>BDD</firstName>\n" +
                "    <lastName>Test</lastName>\n" +
                "    <cnp>" + cnp + "</cnp>\n" +
                "    <dateOfBirth>1987-12-10</dateOfBirth>\n" +
                "    <address>\n" +
                "        <street>City</street>\n" +
                "        <number>Country</number>\n" +
                "        <city>Number</city>\n" +
                "        <country>Street</country>\n" +
                "    </address>\n" +
                "    \n" +
                "    <grades>\n" +
                "        <subject>Subject</subject>\n" +
                "        \n" +
                "        <marks>\n" +
                "            <dateReceived>2020-03-12</dateReceived>\n" +
                "            <mark>10.0</mark>\n" +
                "        </marks>\n" +
                "    </grades>\n" +
                "</student>";
    }
}
