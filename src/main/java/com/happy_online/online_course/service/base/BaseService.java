package com.happy_online.online_course.service.base;

import com.happy_online.online_course.models.base.BaseDomain;
import com.happy_online.online_course.payload.response.CourseInfoResponse;

import java.io.Serializable;
import java.util.List;

public interface BaseService<E extends BaseDomain<ID>, ID extends Serializable> {
    E findById(ID id);

    List<E> findAll();

}
