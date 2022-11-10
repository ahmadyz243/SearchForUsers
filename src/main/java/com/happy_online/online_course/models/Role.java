package com.happy_online.online_course.models;

import com.happy_online.online_course.models.base.BaseDomain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
public class Role extends BaseDomain<Long> {
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;


    public Role( ERole name) {
        this.name = name;
    }

    public Role() {

    }
}