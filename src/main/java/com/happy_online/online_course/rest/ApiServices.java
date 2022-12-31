package com.happy_online.online_course.rest;

import com.happy_online.online_course.domain.Person;
import com.happy_online.online_course.dto.MessageResponse;
import com.happy_online.online_course.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class ApiServices {

    private final PersonService service;

    @PutMapping("/add-person")
    public ResponseEntity<MessageResponse> addPerson(@RequestBody Person person){
        service.save(person);
        return new ResponseEntity<>(new MessageResponse("success"),HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-avg-age")
    public ResponseEntity<Integer> getAgesAverage(){
        return new ResponseEntity<>(service.getAvgAge(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-youngest")
    public ResponseEntity<List<Person>> getYoungest(){
        return new ResponseEntity<>(service.getYoungest(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/find-all-people")
    public ResponseEntity<List<Person>> findPeople(){
        return new ResponseEntity<>(service.findAll(), HttpStatus.ACCEPTED);
    }

    @GetMapping("/people-with-a")
    public ResponseEntity<List<Person>> findPeopleWithA(){
        return new ResponseEntity<>(service.peopleTheirNameStartsWithA(),
                HttpStatus.ACCEPTED);
    }

    @GetMapping("/people-with-c")
    public ResponseEntity<List<Person>> findPeopleWithC(){
        return new ResponseEntity<>(service.peopleTheirLastNameEndsWithC(), HttpStatus.ACCEPTED);
    }
    @GetMapping("/name-contains-b-and-age-greater-eighteen")
    public ResponseEntity<List<Person>> firstOrLastContainsBAndAgeMoreThanEighteen(){
        return new ResponseEntity<>(service.firstnameOrLastnameContainsBAndAgeGreaterThanEighteen(), HttpStatus.ACCEPTED);
    }

}
