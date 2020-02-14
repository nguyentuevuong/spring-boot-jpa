package com.nittsu.kinjirou.update.services;

import java.util.List;
import java.util.Optional;

import com.nittsu.kinjirou.update.model.Person;
import com.nittsu.kinjirou.update.repository.PersonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PersonService {
	@Autowired
	PersonRepository<Person> personRepository;

	@Transactional
	public List<Person> getAllPersons() {
		if (personRepository.count() == 0) {
			for (int i = 0; i < 10; i++) {
				personRepository.save(new Person(29, "FirstName " + i, "LastName " + i));
			}
		}

		return (List<Person>) personRepository.findAll();
	}

	@Transactional
	public List<Person> findByName(String name) {
		return personRepository.findByFirstName(name);
	}

	@Transactional
	public Optional<Person> getById(Long id) {
		return personRepository.findById(id);
	}

	@Transactional
	public void deletePerson(Long personId) {
		personRepository.deleteById(personId);
	}

	@Transactional
	public boolean addPerson(Person person) {
		return personRepository.save(person) != null;
	}

	@Transactional
	public boolean updatePerson(Person person) {
		return personRepository.save(person) != null;
	}
}
