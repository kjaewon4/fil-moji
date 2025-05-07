package com.example.backend.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class PythonServerLauncher implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        try {
            // 8001 포트 점유 여부 확인
            Process checkPort = new ProcessBuilder("cmd", "/c", "netstat -ano | findstr :8001").start();
            checkPort.waitFor();
            if (checkPort.exitValue() == 0) {
                System.out.println("⚠️ 포트 8001 사용 중 → FastAPI 실행 생략");
                return;
            }

            // Python 모듈 방식으로 실행
            ProcessBuilder pb = new ProcessBuilder("python", "-m", "landmark_server.main");

            // 현재 실행 디렉토리: backend-spring → 루트로 이동
            File projectRoot = new File(System.getProperty("user.dir")).getParentFile();
            pb.directory(projectRoot);  // 여기서 landmark_server가 있는 위치여야 함
            pb.inheritIO();  // 콘솔 로그 Spring 서버와 같이 출력

            pb.start();
            System.out.println("✅ MediaPipe 얼굴 서버 실행됨 (localhost:8001)");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
