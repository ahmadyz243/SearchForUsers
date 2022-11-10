package com.happy_online.online_course.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


@Setter
@Getter
public class CreateCourseRequest {

    private String description;

    private String title;

    private Date startDate;

    private Date endDate;

}
