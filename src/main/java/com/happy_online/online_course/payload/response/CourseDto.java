package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class CourseDto {

    private Long id;
    private TeacherDto teacherDto = new TeacherDto();
    private List<StudentDto> studentDtoList = new ArrayList<>();
    private List<StudentDto> studentsNotInCourse = new ArrayList<>();

}
