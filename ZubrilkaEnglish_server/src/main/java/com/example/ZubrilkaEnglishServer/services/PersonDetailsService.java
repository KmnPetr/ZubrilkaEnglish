package com.example.ZubrilkaEnglishServer.services;

import com.example.ZubrilkaEnglishServer.models.Person;
import com.example.ZubrilkaEnglishServer.repositories.PeopleRepository;
import com.example.ZubrilkaEnglishServer.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonDetailsService implements UserDetailsService{
    private final PeopleRepository peopleRepository;
    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {this.peopleRepository = peopleRepository;}

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Person> person=peopleRepository.findByEmail(email);
        if (person.isEmpty())throw new UsernameNotFoundException("User not found!");
        return new PersonDetails(person.get());
    }
}
