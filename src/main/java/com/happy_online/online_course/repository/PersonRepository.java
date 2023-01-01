package com.happy_online.online_course.repository;

import com.happy_online.online_course.domain.Person;
import com.happy_online.online_course.dto.CountPeopleByAge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
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
    @Query(value = "select p from Person as p where p.age = (select max(p.age) from Person p)")
    List<Person> getOldest();
    int countPeopleByAgeGreaterThan(int age);
    @Query(value = "select count(p.id) as count, p.age as age FROM Person as p group by p.age")
    List<Tuple> countPeopleGroupByAge();
}
