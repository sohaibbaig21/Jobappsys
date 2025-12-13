package com.Jobapplicantsystem.Jobappsys.controller;

import com.Jobapplicantsystem.Jobappsys.exception.ResourceNotFoundException;
import com.Jobapplicantsystem.Jobappsys.util.SupabaseStorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileStorageController {

    // This FileStorageController.java is the "File Librarian" of your application.
    //
    //Its job is to find a specific resume when a user asks for it for Downloading Resume
    // and send the file back to the browser safely. It has a smart "Failover System"
    private final SupabaseStorageClient supabaseStorageClient;

    @GetMapping("/resume/{filename}")
    public ResponseEntity<Resource> downloadResume(@PathVariable String filename) {
        try {
            Resource resource = supabaseStorageClient.downloadFile(filename);

            String contentType = "application/octet-stream"; // Default content type
            // You might want to infer content type based on filename extension
            // For now, a generic content type is fine for downloads

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            // Fallback to local storage: uploads/pdfs/{filename}
            try {
                // Only allow filename, strip any path to prevent traversal
                String safeName = Paths.get(filename).getFileName().toString();
                Path baseDir = Paths.get("uploads", "pdfs").toAbsolutePath().normalize();
                Path filePath = baseDir.resolve(safeName).normalize();

                if (!filePath.startsWith(baseDir)) {
                    throw new ResourceNotFoundException("Invalid filename");
                }

                if (!Files.exists(filePath) || !Files.isRegularFile(filePath)) {
                    throw new ResourceNotFoundException("Resume not found: " + safeName);
                }

                FileSystemResource fsResource = new FileSystemResource(filePath.toFile());
                String detected = Files.probeContentType(filePath);
                if (detected == null || detected.isBlank()) {
                    // Heuristic: if ends with .pdf use application/pdf
                    //octet-stream forces the download of the file
                    detected = safeName.toLowerCase().endsWith(".pdf") ? "application/pdf" : "application/octet-stream";
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(detected))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fsResource.getFilename() + "\"")
                        .body(fsResource);
            } catch (Exception ex) {
                throw new ResourceNotFoundException("Resume not found or could not be downloaded: " + filename);
            }
        }
    }
}
