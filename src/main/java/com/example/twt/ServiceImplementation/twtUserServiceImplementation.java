package com.example.twt.ServiceImplementation;

import com.example.twt.Model.twtUser;
import com.example.twt.Repo.twtUserRepository;
import com.example.twt.Service.twtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class twtUserServiceImplementation implements twtUserService {

    private final twtUserRepository userRepository;

    @Autowired
    public twtUserServiceImplementation(twtUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public twtUser loadUserByUsername(String username) throws UsernameNotFoundException {
        twtUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return user.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
