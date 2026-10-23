package com.demo.webtuyendung.dto;

import lombok.Data;
import java.util.List;

@Data
public class CVAnalysisResult {
    private Integer score;
    private String name;
    private String email;
    private String phone;
    private List<String> strengths;
    private List<String> weaknesses;
    private String summary;
}