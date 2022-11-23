package com.happy_online.online_course.mapper;

import com.happy_online.online_course.models.Course;
import com.happy_online.online_course.payload.CourseInfoResponseForStudent;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ExamMapper.class})
public interface CourseMapper {
    List<CourseInfoResponseForStudent> mapCoursesToCourseInfoResponseForStudentList(List<Course> courses);
}
