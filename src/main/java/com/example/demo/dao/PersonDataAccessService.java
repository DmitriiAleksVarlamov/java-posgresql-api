package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Person insertPerson(UUID id, Person person) {
        final String name = person.getName();
        final String sql = "INSERT INTO person (id, name) VALUES (?, ?)";

        jdbcTemplate.update(sql, id, name);

        return new Person(id, name);
    }

    @Override
    public List<Person> selectAll() {
        final String sql = "SELECT id, name FROM person";

        return jdbcTemplate.query(sql, (resultSet, index) -> {
            var id = UUID.fromString(resultSet.getString("id"));
            var name = resultSet.getString("name");

            return new Person(id, name);
        });
    }

    @Override
    public Optional<Person> selectById(UUID id) {
        final String sql = "SELECT id, name FROM person WHERE id = ?";

        Person person = jdbcTemplate.queryForObject(sql, new Object[]{id}, (resultSet, index) -> {
            var personId = UUID.fromString(resultSet.getString("id"));
            var personName = resultSet.getString("name");

            return new Person(personId, personName);
        });

        return Optional.ofNullable(person);
    }

    @Override
    public int deleteById(UUID id) {
        final String sql = "DELETE FROM person WHERE id = ?";

        jdbcTemplate.update(sql, id);

        return 1;
    }

    @Override
    public Integer updateById(UUID id, Person person) {
        final String name = person.getName();
        final String sql = "UPDATE person SET name = ? WHERE id = ?";

        jdbcTemplate.update(sql, name, id);

        return 1;
    }
}
