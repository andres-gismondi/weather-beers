package com.api.beer.beers.users.repository;

import com.api.beer.beers.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findBySurName(String surname);

    Optional<User> findBySurNameAndPassword(String surname, String pasword);

}
