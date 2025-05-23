package com.example.backend.controller;

import com.example.backend.dto.CustomUser;
import com.example.backend.dto.PhotoResponse;
import com.example.backend.entity.Photo;
import com.example.backend.repository.PhotoRepository;
import com.example.backend.service.OracleStorageService;
import com.example.backend.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/photos")
@PreAuthorize("isAuthenticated()")
public class PhotoController {

    private final PhotoService photoService;
    private final OracleStorageService oracleStorageService;
    private final PhotoRepository photoRepository;

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


    /**
     * 유저별 갤러리 호출
     */
    @GetMapping("/gallery")
    public ResponseEntity<List<PhotoResponse>> getGallery(@AuthenticationPrincipal CustomUser user) {
        Long userId = user.getId();
        return ResponseEntity.ok(photoService.getUserGallery(userId));

    }

    /**
     * 사진 삭제
     */
    @DeleteMapping
    public ResponseEntity<?> deletePhoto(@RequestParam("path") String encodedPath, @AuthenticationPrincipal CustomUser user) {
        System.out.println("🔐 로그인 유저 ID: " + user.getId());

        // decode URL path if encoded
        String filePath = URLDecoder.decode(encodedPath, StandardCharsets.UTF_8);

        Optional<Photo> photoOpt = photoRepository.findByPhotoUrlAndUser_Id(filePath, user.getId());
//        Optional<Photo> photoOpt = photoRepository.findByPhotoUrl(filePath);

        if (photoOpt.isEmpty()) return ResponseEntity.notFound().build();

        Photo photo = photoOpt.get();

        // 1. 오브젝트 스토리지에서 삭제
        oracleStorageService.deleteObject(photo.getPhotoUrl());

        // 2. DB에서 삭제
        photoRepository.delete(photo);

        return ResponseEntity.ok().build();
    }
}
