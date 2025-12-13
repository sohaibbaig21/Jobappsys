// ============================================
// FILE 3: SupabaseConfig.java
// Location: src/main/java/com/Jobapplicantsystem/config/SupabaseConfig.java
// ============================================

package com.Jobapplicantsystem.Jobappsys.config;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import com.Jobapplicantsystem.Jobappsys.util.SupabaseStorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Supabase Configuration
 *
 * PURPOSE: Configure connection to Supabase services
 * - Connection settings and authentication
 *
 * PROPERTIES REQUIRED IN application.properties:
 *  supabase.url=YOUR_PROJECT_URL
 *  supabase.anon.key=YOUR_ANON_KEY
 *  supabase.service.key=YOUR_SERVICE_ROLE_KEY
 *  supabase.storage.bucket.resumes=resumes
 */
@Configuration
public class SupabaseConfig {


    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.anon.key}")
    private String supabaseAnonKey;

    @Value("${supabase.service.key}")
    private String supabaseServiceKey;

    @Value("${supabase.storage.bucket.resumes}")
    private String resumesBucketName;

    @Bean
    public OkHttpClient supabaseHttpClient() {

        // Interceptor → automatically attaches API Key to every request
        Interceptor authInterceptor = chain -> {
            Request original = chain.request();

            Request request = original.newBuilder()
                    .header("apikey", supabaseAnonKey)
                    .header("Authorization", "Bearer " + supabaseServiceKey)
                    .method(original.method(), original.body())
                    .build();

            return chain.proceed(request);
        };

        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(authInterceptor)
                .build();
    }


    @Bean
    public SupabaseStorageClient supabaseStorageClient() {
        return new SupabaseStorageClient(
                supabaseUrl,
                supabaseServiceKey,
                supabaseHttpClient(),
                resumesBucketName
        );
    }


    @Bean
    public String supabaseUrl() {
        return supabaseUrl;
    }

    @Bean
    public String resumesBucket() {
        return resumesBucketName;
    }
}
