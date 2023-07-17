package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao {
    private static final List<Person> db = new ArrayList<>();
    @Override
    public Person insertPerson(UUID id, Person person) {
        var newPerson = new Person(id, person.getName());
        db.add(newPerson);
        return newPerson;
    }

    @Override
    public List<Person> selectAll() {
        return db;
    }

    @Override
    public Optional<Person> selectById(UUID id) {
        return db
                .stream()
                .filter(person -> person.getId().equals(id))
                .findFirst();
    }

    @Override
    public int deleteById(UUID id) {
        Optional<Person> isPersonExist = selectById(id);

        if (isPersonExist.isEmpty()) {
            return 0;
        }

        db.remove(isPersonExist.get());

        return 1;
    }

    @Override
    public Integer updateById(UUID id, Person newPerson) {
        return selectById(id)
                .map(person -> {
                    int indexToUpdate = db.indexOf(person);

                    if (indexToUpdate > -1) {
                        db.set(indexToUpdate, new Person(id, newPerson.getName()));
                        return 1;
                    }

                    return 0;
                })
                .orElse(0);
    }
}
