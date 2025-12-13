package com.Jobapplicantsystem.Jobappsys.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    // Constructor: Creates the 'uploads' folder automatically
    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file, String subDirectory) {
        // Clean the file path
        String originalFileName = file.getOriginalFilename();
        // Generate unique name to prevent duplicates (e.g., uuid_resume.pdf)
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(subDirectory).resolve(fileName);
            Files.createDirectories(targetLocation.getParent()); // Ensure subdirectory exists
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return subDirectory + "/" + fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        return storeFile(file, "default"); // Default subdirectory
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found " + fileName, ex);
        }
    }

    public void deleteFile(String fileName, String subDirectory) {
        try {
            Path filePath = this.fileStorageLocation.resolve(subDirectory).resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new RuntimeException("Could not delete file " + fileName, ex);
        }
    }
}