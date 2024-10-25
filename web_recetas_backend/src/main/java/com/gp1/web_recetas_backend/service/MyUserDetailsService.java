package com.gp1.web_recetas_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gp1.web_recetas_backend.repository.UserRepository;
import com.gp1.web_recetas_backend.model.User;

@Service
public class MyUserDetailsService implements UserDetailsService {
    
    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
    
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Intentando cargar usuario: {}", username);
        
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.error("Usuario no encontrado: {}", username);
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }
        
        logger.info("Usuario encontrado: {}", username);
        return user;
    }
}