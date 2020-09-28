package com.example.springsocial.repository;

import com.example.springsocial.model.InstagramAccess;
import com.example.springsocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstagramAccessRepository extends JpaRepository<InstagramAccess, Long> {

    Optional<InstagramAccess> findByUser(User user);

}
