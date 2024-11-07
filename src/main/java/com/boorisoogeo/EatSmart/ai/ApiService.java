/*
package com.boorisoogeo.EatSmart.ai;

import com.boorisoogeo.EatSmart.domain.Member;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    public void sendRequestToFlaskServer() {
        RestTemplate restTemplate = new RestTemplate();

        // 요청 데이터 생성

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HttpEntity 생성
        HttpEntity<Member> requestEntity = new HttpEntity<>(Member, headers);

        // 플라스크 서버로 POST 요청
        String url = "http://localhost:5000/api/send";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

        System.out.println("Response from Flask server: " + responseEntity.getBody());
    }
}
*/
