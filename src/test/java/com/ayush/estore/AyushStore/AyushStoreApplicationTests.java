package com.ayush.estore.AyushStore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ayush.estore.AyushStore.Repository.UserRepo;
import com.ayush.estore.AyushStore.entities.User;
import com.ayush.estore.AyushStore.security.JWTHelper;

@SpringBootTest
class AyushStoreApplicationTests {

	@Autowired
	private UserRepo userRepository;

	@Autowired
	private JWTHelper jwtHelper;

	@Test
	void contextLoads() {
	}

	@Test
	void testToken() {
		User user = userRepository.findByEmail("ayush@gmail.com").get();
		String token = jwtHelper.generateToken(user);
		System.out.println("TOKEN" + token);
		System.out.println("Getting username from token " + jwtHelper.getUsernameFromToken(token));
		System.out.println(jwtHelper.isTokenExpired(token));
	}
}
