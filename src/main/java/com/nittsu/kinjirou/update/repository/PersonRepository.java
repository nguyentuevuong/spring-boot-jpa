package com.nittsu.kinjirou.update.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import com.nittsu.kinjirou.update.model.Person;

public interface PersonRepository<P> extends CrudRepository<Person, Long> {
    List<Person> findByFirstName(String firstName);
}