package com.example.coffee.serviceImpl;


import com.example.coffee.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
//
//    @Value("${file.upload.directory}")
//    private String fileUploadDirectory;

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {

        // Get the original file name
        String originalFileName = file.getOriginalFilename();

        // Generate a random UUID
        String randomID = UUID.randomUUID().toString();

        // Create a unique file name by combining UUID and the original file extension
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        String uniqueFileName = randomID + fileExtension;
        String filePath = path + File.separator + uniqueFileName;


        // Create any required directories
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Copy the file to the specified path
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // Return the original file name
        return originalFileName;
    }

    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {

        // Construct the full file path
        String filePath =  path + File.separator + fileName;

        // Check if the file exists
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        try {
            // Open and return the file as an InputStream
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // Handle the exception if the file is not found
            throw e;
        }
    }



}

