package com.Jobapplicantsystem.Jobappsys.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController {

    // Some browsers request /favicon.ico automatically. If no static icon is present,
    // return 204 No Content to avoid noisy 500/404 errors in the console.
    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.noContent().build();
    }
}

