package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class TeacherCourseResponse {
    private Long id;
    private Date endDate;
    private Date startDate;
    private String description;
    private String title;
    private boolean enabled;
}

