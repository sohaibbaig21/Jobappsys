package com.Jobapplicantsystem.Jobappsys.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
// Every time you open a website, your web browser
// automatically sends a secret request in the background:
// Hey Server, do you have a tiny icon I can show in the browser tab?" Request: GET /favicon.ico
//
//If your backend doesn't have an icon file (or a controller to handle this), Spring Boot gets confused:
//
//It searches for the route /favicon.ico.
//
//It finds nothing.
//
//It throws a 404 Not Found or a 500 Error in your IntelliJ console.
//
//This clutters your logs with red error text that isn't actually a real problem.
@RestController
public class FaviconController {

    // Some browsers request /favicon.ico automatically. If no static icon is present,
    // return 204 No Content to avoid noisy 500/404 errors in the console.
    @GetMapping("/favicon.ico")
    public ResponseEntity<Void> favicon() {
        return ResponseEntity.noContent().build();
    }
}

