package org.example.springsecurityoauthjwt.repository;

import org.example.springsecurityoauthjwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
