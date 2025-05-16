package com.example.backend.config;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimplePrivateKeySupplier;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.nio.file.Paths;

@Configuration
public class OracleStorageConfig {

    // Object Storage 클라이언트를 Bean으로 등록
    @Bean
    public ObjectStorage objectStorageClient() throws Exception {
        // ~/.oci/config 파일 기반 인증
        final String CONFIG_PATH = System.getProperty("user.home") + "/.oci/config";
        final String PROFILE = "DEFAULT"; // config 파일 내 프로파일 이름

        AuthenticationDetailsProvider provider =
                new ConfigFileAuthenticationDetailsProvider(CONFIG_PATH, PROFILE);

        return new ObjectStorageClient(provider);
    }
}
