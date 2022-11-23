package com.happy_online.online_course.payload;

import com.happy_online.online_course.models.Teacher;
import com.happy_online.online_course.payload.response.ExamResponseForStudent;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class CourseInfoResponseForStudent {
    private Teacher teacher;
    private List<ExamResponseForStudent> examList;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Boolean isActive = true;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
