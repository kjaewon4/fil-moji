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
            Process checkPort = new ProcessBuilder("cmd", "/c", "netstat -ano | findstr :8000").start();
            checkPort.waitFor();
            if (checkPort.exitValue() == 0) {
                System.out.println("⚠️ 포트 8000 사용 중 → FastAPI 실행 생략");
                return;
            }

            // landmark_server 디렉토리 설정
            File projectRoot = new File(System.getProperty("user.dir")).getParentFile(); // backend-spring → deepLearning/
            File landmarkDir = new File(projectRoot, "landmark_server");

            // FastAPI 실행 (python -m app.main)
//            ProcessBuilder pb = new ProcessBuilder("python", "-m", "app.main");
//            pb.directory(landmarkDir);        // 작업 디렉토리 landmark_server/
//            pb.inheritIO();  // 콘솔 로그 Spring 서버와 같이 출력
//
//            pb.start();
            System.out.println("✅ MediaPipe 얼굴 서버 실행됨 (localhost:8000)");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
