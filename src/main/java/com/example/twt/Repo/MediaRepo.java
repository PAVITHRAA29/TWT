package com.example.twt.Repo;

import com.example.twt.Model.Media;
import com.example.twt.Model.twtUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepo extends JpaRepository<Media, Integer> {

    List<Media> findByTwtUser(twtUser twtUser);


}
