package com.example.backend.service;

import com.example.backend.dto.PhotoResponse;
import com.example.backend.entity.Member;
import com.example.backend.entity.Photo;
import com.example.backend.repository.MemberRepository;
import com.example.backend.repository.PhotoRepository;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails;
import com.oracle.bmc.objectstorage.model.ListObjects;
import com.oracle.bmc.objectstorage.model.ObjectSummary;
import com.oracle.bmc.objectstorage.requests.CreatePreauthenticatedRequestRequest;
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OracleStorageService {
    // Oracle Cloud의 Object Storage 서비스(버킷, 오브젝트 파일 등)에 접근하기 위한 클라이언트 객체
    private final ObjectStorage objectStorage;

    @Value("${oracle.config.namespace}")
    private String namespace;

    @Value("${oracle.config.bucket-name}")
    private String bucketName;

    @Value("${oracle.config.region}")
    private String region;

    public void uploadFileWithCustomPath(MultipartFile file, String objectPath) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucketName(bucketName)
                .namespaceName(namespace)
                .objectName(objectPath)
                .putObjectBody(file.getInputStream())
                .contentLength(file.getSize())
                .build();

        objectStorage.putObject(request);
    }

    /**
     * Oracle Object Storage의 Pre-Authenticated Request (PAR) URL 생성
     * 특정 오브젝트(파일)에 대해 임시로 접근할 수 있는 URL(PAR URL) 생성
     */
    public String generateParUrl(String objectName) {
        log.info("🔧 생성 요청 - objectName: {}", objectName);
        log.info("📦 bucket: {}, namespace: {}", bucketName, namespace);

        // PAR 객체의 세부사항을 구성
        CreatePreauthenticatedRequestDetails details =
                CreatePreauthenticatedRequestDetails.builder()
                        .name("par-" + UUID.randomUUID())  // 요청 이름
                        .objectName(objectName)  // 접근 허용할 대상 오브젝트 이름
                        .accessType(CreatePreauthenticatedRequestDetails.AccessType.ObjectRead)  // 접근 권한: 읽기 전용
                        .timeExpires(Date.from(Instant.now().plus(15, ChronoUnit.MINUTES))) // 만료 시간 설정: 15분
                        .build();

        // 앞에 구성한 details 기반 OCI에 보낼 요청 객체 생성
        // Oracle Cloud Infrastructure (OCI): Oracle이 제공하는 클라우드 컴퓨팅 플랫폼
        var request = CreatePreauthenticatedRequestRequest.builder()
                .bucketName(bucketName)
                .namespaceName(namespace)
                .createPreauthenticatedRequestDetails(details)
                .build();


        // OCI SDK를 통해 실제 PAR 요청 전송 후 받은 응답
        var response = objectStorage.createPreauthenticatedRequest(request);

        // accessUri는 보통 "/p/...." 형태만 포함되므로 직접 n/b/o 붙여야 정확
        String accessUri = response.getPreauthenticatedRequest().getAccessUri();  // "/p/abc.../n/.../b/.../o/..."
        log.info("📄 accessUri: {}", accessUri);

        String fullUrl = String.format("https://objectstorage.%s.oraclecloud.com%s", region, accessUri);



        log.info("📡 PAR URL = {}", fullUrl);
        // Oracle Object Storage에서 제공하는 형식에 맞춰 최종 PAR URL 생성 후 반환
        return fullUrl;
    }

    /**
     * Object Storage에 저장된 모든 오브젝트(파일) 이름 목록을 가져옴
     * 사용자의 이미지 목록을 조회해서 갤러리 UI에 표시할 때 사용
     */
    public List<String> listObjects() {
        // 대상 버킷 이름과 네임스페이스를 포함한 요청 생성
        ListObjectsRequest listRequest = ListObjectsRequest.builder()
                .bucketName(bucketName)
                .namespaceName(namespace)
                .build();

        // 객체 목록 요청을 보냄
        ListObjectsResponse listResponse = objectStorage.listObjects(listRequest);

        // 응답 받은 객체 목록에서 ObjectSummary 객체의 .getName()만 추출해서 List<String>으로 반환
        return listResponse.getListObjects().getObjects().stream()
                .map(ObjectSummary::getName)
                .collect(Collectors.toList());
    }

    /**
     * Object Storage에서 특정 파일(오브젝트) 삭제
     */
    public void deleteObject(String objectName) {
        DeleteObjectRequest request = DeleteObjectRequest.builder()
                .bucketName(bucketName)
                .namespaceName(namespace)
                .objectName(objectName)
                .build();

        objectStorage.deleteObject(request);
    }
}
