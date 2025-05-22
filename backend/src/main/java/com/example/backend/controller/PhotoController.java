package com.example.backend.controller;

import com.example.backend.dto.CustomUser;
import com.example.backend.dto.PhotoResponse;
import com.example.backend.entity.Photo;
import com.example.backend.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
@PreAuthorize("isAuthenticated()")
public class PhotoController {

    private final PhotoService photoService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadPhoto(
            @RequestParam MultipartFile file,
            @RequestParam String filterInfo,
            @AuthenticationPrincipal CustomUser user) throws IOException {

        Long userId = user.getId();

        System.out.println("PhotoController.uploadPhoto filterInfo = " + filterInfo);

        photoService.saveUserPhoto(file, userId, filterInfo);
        return ResponseEntity.ok(Map.of("message", "Upload success"));

    }

    @GetMapping("/gallery")
    public ResponseEntity<List<PhotoResponse>> getGallery(@AuthenticationPrincipal CustomUser user) {
        Long userId = user.getId();
        return ResponseEntity.ok(photoService.getUserGallery(userId));

    }
}
