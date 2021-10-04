package com.learning.student.studentbdd.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class FileGenerator {

    @Value("${student.file.path}")
    private String path;

    public void createFile(String xml) {
        List<String> xmlLines = Arrays.asList(xml);
        Path file = Paths.get(path + "/student-test.xml");
        try {
            Files.write(file, xmlLines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("Exception while creating the test xml file: ", e);
        }
    }
}
