package com.boorisoogeo.EatSmart.service;
import lombok.Data;

import java.time.LocalDate;
@Data
public class MemberUpdateDto {
        private Long id;
        private String loginId;
        private String name;
        private String password;
        private String email;
        private String gender;
        private LocalDate date;
        private Double height;
        private Double weight;
}
