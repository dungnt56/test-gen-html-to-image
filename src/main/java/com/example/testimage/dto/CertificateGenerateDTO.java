package com.example.testimage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateGenerateDTO {
    String logoSellerUrl;
    String studentName;
    String courseName;
    Instant certificateTime;
}
