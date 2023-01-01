package com.happy_online.online_course.mapper;

import com.happy_online.online_course.dto.CountPeopleByAge;
import org.springframework.stereotype.Component;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;

@Component
public class MyMapperImpl implements MyMapper{

    @Override
    public List<CountPeopleByAge> mapTuplesToCountPeopleByAgeEntities(List<Tuple> tuples) {

        List<CountPeopleByAge> countPeopleByAgeEntities = new ArrayList<>();
        tuples.forEach(
                tuple -> countPeopleByAgeEntities.add(
                        new CountPeopleByAge(
                                tuple.get("count"),tuple.get("age")
                        )
                )
        );
        return countPeopleByAgeEntities;
    }
}
