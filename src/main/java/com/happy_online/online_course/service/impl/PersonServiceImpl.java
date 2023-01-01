package com.happy_online.online_course.service.impl;

import com.happy_online.online_course.domain.Person;
import com.happy_online.online_course.dto.CountPeopleByAge;
import com.happy_online.online_course.mapper.MyMapper;
import com.happy_online.online_course.repository.PersonRepository;
import com.happy_online.online_course.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository repository;
    private final MyMapper myMapper;

    @Override
    public void save(Person person) {
        repository.save(person);
    }

    @Override
    public List<Person> peopleTheirNameStartsWithA() {
        return repository.findAllByFirstnameStartsWith("a");
    }

    @Override
    public List<Person> peopleTheirLastNameEndsWithC() {
        return repository.findAllByLastnameEndsWithIgnoreCase("c");
    }

    @Override
    public List<Person> firstnameOrLastnameContainsBAndAgeGreaterThanEighteen() {
        return repository.findAllByFirstnameContainsIgnoreCaseOrLastnameContainsIgnoreCaseAndAgeGreaterThan("b","b", 18);
    }

    @Override
    public List<Person> findAll() {
        return repository.findAll();
    }

    @Override
    public int getAvgAge() {
        return repository.getAvgAge();
    }

    @Override
    public List<Person> getYoungest() {
        return repository.getYoungest();
    }

    @Override
    public List<Person> getOldest() {
        return repository.getOldest();
    }

    @Override
    public int countPeopleByAgeGreaterThanEighteen() {
        return repository.countPeopleByAgeGreaterThan(18);
    }

    @Override
    public List<CountPeopleByAge> countPeopleGroupByAge() {
        return myMapper.mapTuplesToCountPeopleByAgeEntities(
                repository.countPeopleGroupByAge()
        );
    }
}
