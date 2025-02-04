package com.historia.zetu.entity;


import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Map;


@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class HttpResponse {
    protected String timesStamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String message;
    protected String DeveloperMessage;
    protected String path;
    protected String requestMethod;
    protected Map<?, ?> data;
    private int totalPages;
    private long totalElements;
}
