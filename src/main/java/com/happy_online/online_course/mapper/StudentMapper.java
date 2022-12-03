package com.happy_online.online_course.mapper;

import com.happy_online.online_course.models.Student;
import com.happy_online.online_course.payload.response.StudentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentResponse mapStudentToStudentResponse(Student student);
}
