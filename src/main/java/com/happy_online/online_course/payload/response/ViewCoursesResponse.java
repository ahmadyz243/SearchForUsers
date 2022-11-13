package com.happy_online.online_course.payload.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class ViewCoursesResponse {

    private Long id;
    private Date startDate;
    private Date endDate;
    private String title;

}
