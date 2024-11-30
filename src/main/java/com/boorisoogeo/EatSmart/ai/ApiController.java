package com.boorisoogeo.EatSmart.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    @PostMapping("/receive")
    public ResponseEntity<String> receiveJson(@RequestBody FoodData foodData) {
        // JSON 데이터 처리 로직
        log.info("foodData = {}", foodData);
        // 응답 데이터 생성
        String responseMessage = "Data received successfully";
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
