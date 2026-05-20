package com.ims.fullstack.service;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ResumeParserService {

    private final Tika tika = new Tika();

    public String extractTextFromFile(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "";
        }

        try (InputStream inputStream = new FileInputStream(filePath)) {
            return tika.parseToString(inputStream);
        } catch (IOException | TikaException e) {
            System.err.println("Failed to parse resume file: " + e.getMessage());
            return "";
        }
    }
}