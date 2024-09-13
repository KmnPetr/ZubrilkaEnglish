package com.zubrilka.VideoManager.repositories;

import com.zubrilka.VideoManager.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Person findByEmail(String email);

    @Query("SELECT p FROM Person p WHERE p.short_name = :username")
    Person findByName(@Param("username") String username);
}
