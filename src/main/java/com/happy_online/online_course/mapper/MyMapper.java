package com.happy_online.online_course.mapper;

import com.happy_online.online_course.dto.CountPeopleByAge;

import javax.persistence.Tuple;
import java.util.List;

public interface MyMapper {

    List<CountPeopleByAge> mapTuplesToCountPeopleByAgeEntities(List<Tuple> tuples);

}
