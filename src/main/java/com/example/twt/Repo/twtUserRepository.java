package com.example.twt.Repo;

import com.example.twt.Model.twtUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface twtUserRepository extends JpaRepository <twtUser, Integer> {

    Optional<twtUser> findByEmail(String email);

    Optional<twtUser> findByUsername(String username);
}
