package com.happy_online.online_course.payload.response;

import com.happy_online.online_course.models.Exam;
import com.happy_online.online_course.models.Student;
import com.happy_online.online_course.models.Teacher;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class CourseInfoResponse {
    private Date endDate;
    private Date startDate;
    private String description;
    private String title;
    private List<Exam> examList;
    private Teacher teacher;
    private List<Student> studentList;
    private Boolean isActive;
}
