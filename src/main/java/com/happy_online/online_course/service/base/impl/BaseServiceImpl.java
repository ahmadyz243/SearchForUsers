package com.happy_online.online_course.service.base.impl;

import com.happy_online.online_course.exception.NotFoundException;
import com.happy_online.online_course.models.base.BaseDomain;
import com.happy_online.online_course.service.base.BaseService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Transactional
public class BaseServiceImpl<E extends BaseDomain<ID>,
        ID extends Serializable, R extends JpaRepository<E, ID>> implements BaseService<E, ID> {
    protected final R repository;


    @Override
    public E findById(ID id) {
        Optional<E> eOptional = repository.findById(id);
        // TODO: 11/8/2022 change the exception
        return eOptional.orElseThrow(() -> new NotFoundException("no result found to complete your request"));
    }

    @Override
    public List<E> findAll() {
        return repository.findAll();
    }

    @Override
    public void delete(E e) {
        repository.delete(e);
    }


}
