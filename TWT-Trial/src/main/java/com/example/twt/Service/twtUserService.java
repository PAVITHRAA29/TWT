package com.example.twt.Service;

import com.example.twt.Model.twtUser;
import org.springframework.stereotype.Service;

@Service
public interface twtUserService {

    twtUser loadUserByUsername(String username);
}
