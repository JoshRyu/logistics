package com.madeg.logistics.repository;

import com.madeg.logistics.entity.User;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);

  long countByRole(String role);

  List<User> findAll();
}
