package com.example.backend.service;

import com.example.backend.dto.PhotoResponse;
import com.example.backend.entity.Member;
import com.example.backend.entity.Photo;
import com.example.backend.repository.MemberRepository;
import com.example.backend.repository.PhotoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;
    private final OracleStorageService storageService;
    private final MemberRepository memberRepository;

    public void saveUserPhoto(MultipartFile file, Long userId, String filterInfo) throws IOException {
        Member user = memberRepository.findById(userId).orElseThrow(() -> new RuntimeException("Member not found"));
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String objectPath = String.format("users/%d/%s", userId, fileName);

        storageService.uploadFileWithCustomPath(file, objectPath);

        String parUrl = storageService.generateParUrl(objectPath);
        log.info("🔗 PAR URL 생성 완료: {}", parUrl);

        Photo photo = Photo.builder()
                .user(user)
                .photoUrl(objectPath)
                .filterInfo(filterInfo)
                .build();

        log.info("📷 업로드 시작 - userId: {}, path: {}, filter: {}", userId, objectPath, filterInfo);

        try {
            photoRepository.save(photo);
            log.info("✅ Photo 저장 성공!");
        } catch (Exception e) {
            log.error("❌ Photo 저장 실패", e);
        }

    }

    public List<PhotoResponse> getUserGallery(Long userId) {
        List<Photo> photos = photoRepository.findAllByUserId(userId);
        return photos.stream()
                .map(photo -> PhotoResponse.of(
                        photo.getPhotoUrl(),
                        storageService.generateParUrl(photo.getPhotoUrl()),
                        photo.getFilterInfo()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void backfillParUrls() {
        List<Photo> photos = photoRepository.findAll();

        for (Photo photo : photos) {
            if (photo.getParUrl() == null || photo.getParUrl().isEmpty()) {
                String newParUrl = storageService.generateParUrl(photo.getPhotoUrl());
                photo.setParUrl(newParUrl);
            }
        }

        photoRepository.saveAll(photos);
    }


}
