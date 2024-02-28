package com.madeg.logistics.repository;

import com.madeg.logistics.entity.User;
import com.madeg.logistics.enums.Role;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);

  Integer countByRole(Role role);

  List<User> findAll();
}
