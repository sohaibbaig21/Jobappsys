// ============================================
// FILE: SupabaseStorageClient.java
// Location: src/main/java/com/Jobapplicantsystem/util/SupabaseStorageClient.java
// ============================================

package com.Jobapplicantsystem.Jobappsys.util;

import okhttp3.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

/**
 * Supabase Storage Client Utility
 *
 * PURPOSE: Handles file operations with Supabase Storage
 *
 * FEATURES:
 * 1. Upload file
 * 2. Delete file
 * 3. Get public URL
 * 4. Generate signed URL
 * 5. Download file
 */
public class SupabaseStorageClient {

    private final String supabaseUrl;
    private final String apiKey;
    private final OkHttpClient httpClient;
    private final String bucketName;

    // ============================================================
    // CONSTRUCTOR
    // ============================================================
    public SupabaseStorageClient(String supabaseUrl,
                                 String apiKey,
                                 OkHttpClient httpClient,
                                 String bucketName) {
        this.supabaseUrl = supabaseUrl;
        this.apiKey = apiKey;
        this.httpClient = httpClient;
        this.bucketName = bucketName;
    }

    // ============================================================
    // UPLOAD FILE
    // ============================================================
    public String uploadFile(MultipartFile file, String userId) throws IOException {

        // 1️⃣ Generate a unique filename with the correct extension
        String originalName = file.getOriginalFilename();
        String extension = originalName != null ? originalName.substring(originalName.lastIndexOf(".")) : "";
        String uniqueName = UUID.randomUUID() + extension;

        // 2️⃣ Create structured path: resumes/{userId}/{filename}
        String filePath = "resumes/" + userId + "/" + uniqueName;

        // 3️⃣ Build multipart request body
        RequestBody fileBody = RequestBody.create(file.getBytes(),
                MediaType.parse(file.getContentType()));

        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", uniqueName, fileBody)
                .build();

        // 4️⃣ Supabase Upload Endpoint
        String uploadUrl = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + filePath;

        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(multipartBody)
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        // 5️⃣ Execute request
        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Upload failed: " + response.message());
            }
        }

        // 6️⃣ Return public file URL
        return getFileUrl(filePath);
    }

    // ============================================================
    // DOWNLOAD FILE
    // ============================================================
    public Resource downloadFile(String filename) throws IOException {
        // Assuming 'filename' here might include the path (e.g., resumes/{userId}/uniqueName.pdf)
        // Or it might just be the uniqueName and we need to reconstruct the path.
        // For now, let's assume `filename` is the full `filePath` as stored in Supabase.
        String downloadUrl = supabaseUrl + "/storage/v1/object/public/" + bucketName + "/" + filename;

        Request request = new Request.Builder()
                .url(downloadUrl)
                .get()
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Download failed: " + response.message());
            }
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new IOException("Empty response body for download");
            }
            byte[] fileContent = responseBody.bytes();
            return new ByteArrayResource(fileContent) {
                @Override
                public String getFilename() {
                    // Extract filename from the path if it contains directories
                    int lastSlashIndex = filename.lastIndexOf('/');
                    if (lastSlashIndex != -1) {
                        return filename.substring(lastSlashIndex + 1);
                    }
                    return filename;
                }
            };
        }
    }

    // ============================================================
    // DELETE FILE
    // ============================================================
    public boolean deleteFile(String filePath) throws IOException {

        String deleteUrl = supabaseUrl + "/storage/v1/object/" + bucketName + "/" + filePath;

        Request request = new Request.Builder()
                .url(deleteUrl)
                .delete()
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return response.isSuccessful();
        }
    }

    // ============================================================
    // GET PUBLIC URL
    // ============================================================
    public String getFileUrl(String filePath) {
        return supabaseUrl +
                "/storage/v1/object/public/" +
                bucketName + "/" + filePath;
    }

    // ============================================================
    // GENERATE SIGNED URL
    // ============================================================
    public String generateSignedUrl(String filePath, int expiresInSeconds) throws IOException {

        String signUrl = supabaseUrl +
                "/storage/v1/object/sign/" +
                bucketName + "/" + filePath;

        String jsonBody = "{ \"expiresIn\": " + expiresInSeconds + " }";

        RequestBody body = RequestBody.create(
                jsonBody,
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(signUrl)
                .post(body)
                .addHeader("apikey", apiKey)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("Signed URL generation failed: " + response.message());
            }

            return response.body().string(); // Supabase returns JSON containing the signed URL
        }
    }
}
