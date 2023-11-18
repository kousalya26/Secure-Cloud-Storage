package com.cahcet.FinalProject.web;

import com.cahcet.FinalProject.service.Encrypt;
import com.cahcet.FinalProject.service.UserService;
import com.cahcet.FinalProject.web.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

	private UserService userService;

	public UserRegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }
	
	@GetMapping
	public String showRegistrationForm() {
		return "registration";
	}
	
	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) throws NoSuchAlgorithmException {
		// generating new key for new user
		String key =Encrypt.convertSecretKeyToString(Encrypt.generateKey(256));
		registrationDto.setKey(key);
		userService.save(registrationDto);
		return "redirect:/registration?success";
	}
}
