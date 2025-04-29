package com.example.medtrackfit.controllers;

import com.example.medtrackfit.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/upload")
    public String testUpload(@RequestParam("file") MultipartFile file) throws IOException {
        return cloudinaryService.uploadImage(file);
    }
}