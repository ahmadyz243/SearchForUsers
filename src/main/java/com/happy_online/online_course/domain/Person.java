package com.happy_online.online_course.domain;

import com.happy_online.online_course.base.BaseEntity;
import com.happy_online.online_course.enumiration.Gender;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@Entity
@ToString
public class Person extends BaseEntity {
    private String firstname;
    private String lastname;
    private int age;
    private float score;
    private String mobileNumber;
    @Enumerated(EnumType.STRING)
    private Gender gender;
}
