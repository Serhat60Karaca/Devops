package com.example.project1.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.project1.entity.Person;
@Repository
public interface PersonRepository extends JpaRepository<Person,Integer> {

}
