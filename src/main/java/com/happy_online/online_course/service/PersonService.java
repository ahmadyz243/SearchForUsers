package com.happy_online.online_course.service;


import com.happy_online.online_course.domain.Person;
import com.happy_online.online_course.dto.CountPeopleByAge;

import java.util.List;

public interface PersonService {
    void save(Person person);
    List<Person> peopleTheirNameStartsWithA();
    List<Person> peopleTheirLastNameEndsWithC();
    List<Person> firstnameOrLastnameContainsBAndAgeGreaterThanEighteen();
    List<Person> findAll();
    int getAvgAge();
    List<Person> getYoungest();
    List<Person> getOldest();
    int countPeopleByAgeGreaterThanEighteen();
    List<CountPeopleByAge> countPeopleGroupByAge();
}
