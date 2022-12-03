package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponse {
    private Long id;
    private String name;
    private String lastname;
    private String nationalCode;
}
