package com.example.backend.controller;

import com.example.backend.dto.CustomUser;
import com.example.backend.service.OracleStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class ObjectStorageController {
    private final OracleStorageService oracleStorageService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam String filterInfo,
            @AuthenticationPrincipal CustomUser user) throws IOException {
        if (user == null) {
            throw new IllegalArgumentException("❌ 인증된 사용자 정보가 없습니다.");
        }

        String path = "test/" + file.getOriginalFilename();
        oracleStorageService.uploadFileWithCustomPath(file, path);
        return ResponseEntity.ok("업로드 완료: " + path);
    }

    @GetMapping("/list")
    public ResponseEntity<List<String>> listObjects() {
        return ResponseEntity.ok(oracleStorageService.listObjects());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam String objectName) {
        oracleStorageService.deleteObject(objectName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/par")
    public ResponseEntity<String> getPar(@RequestParam String objectName) {
        return ResponseEntity.ok(oracleStorageService.generateParUrl(objectName));
    }

}
