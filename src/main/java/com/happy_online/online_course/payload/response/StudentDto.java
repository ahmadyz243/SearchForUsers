package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class StudentDto {

    private Long id;
    private String name;
    private String lastname;

}
