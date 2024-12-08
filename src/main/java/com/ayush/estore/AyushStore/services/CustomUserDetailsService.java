package com.ayush.estore.AyushStore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ayush.estore.AyushStore.Repository.UserRepo;
import com.ayush.estore.AyushStore.exceptions.ResourceNotFoundException;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        System.out.println("AALA loadByUsername" + username);
        return userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("user not found"));
    }

}
