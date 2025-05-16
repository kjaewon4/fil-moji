package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class PhotoResponse {
    private String photoUrl;  // 원래 파일 경로 (예: users/101/image.png)

    @JsonProperty("parUrl")
    private String parUrl;   // PAR 링크 (15분 접근 가능 URL)
    private String filterInfo;

}
