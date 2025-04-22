package com.example.question_service.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ResponseAnswer {
    private Integer questionId;
    private String responseAnswer;
}
