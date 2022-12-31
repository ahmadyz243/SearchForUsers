package com.happy_online.online_course.repository;

import com.happy_online.online_course.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    List<Person> findAllByFirstnameStartsWith(String letter);
    List<Person> findAllByLastnameEndsWithIgnoreCase(String letter);
    List<Person> findAllByFirstnameContainsIgnoreCaseOrLastnameContainsIgnoreCaseAndAgeGreaterThan(String f, String l, int age);
    @Query(value = "select avg(age) from Person")
    int getAvgAge();
    @Query(value = "select p from Person as p where p.age = (select min(p.age) from Person p)")
    List<Person> getYoungest();

}
