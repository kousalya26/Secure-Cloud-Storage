package com.cahcet.FinalProject.service;

import com.cahcet.FinalProject.web.dto.UserKeyDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.cahcet.FinalProject.model.User;
import com.cahcet.FinalProject.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService{
	User save(UserRegistrationDto registrationDto);

}
