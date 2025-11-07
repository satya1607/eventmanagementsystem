package com.example.eventmanagementsystem.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.eventmanagementsystem.entity.Event;
import com.example.eventmanagementsystem.entity.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByEmail(String email);
}
