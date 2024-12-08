package com.ayush.estore.AyushStore;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ayush.estore.AyushStore.Repository.RoleRepo;
import com.ayush.estore.AyushStore.Repository.UserRepo;
import com.ayush.estore.AyushStore.config.AppConstants;
import com.ayush.estore.AyushStore.entities.Role;
import com.ayush.estore.AyushStore.entities.User;

@SpringBootApplication
@EnableWebMvc
public class AyushStoreApplication implements CommandLineRunner {

	@Autowired
	private RoleRepo roleRepository;

	@Autowired
	private UserRepo userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(AyushStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		Role roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);

		if (roleAdmin == null) {

			Role role1 = new Role();
			role1.setRoleId(UUID.randomUUID().toString());
			role1.setName("ROLE_" + AppConstants.ROLE_ADMIN);
			roleRepository.save(role1);
			roleAdmin = roleRepository.findByName("ROLE_ADMIN").orElse(null);
		}

		Role roleNormal = roleRepository.findByName("ROLE_NORMAL").orElse(null);

		if (roleNormal == null) {

			Role role2 = new Role();
			role2.setRoleId(UUID.randomUUID().toString());
			role2.setName("ROLE_" + AppConstants.ROLE_NORMAL);
			roleRepository.save(role2);
		}

		// ek admin user banaunga:
		User user = userRepository.findByEmail("ayush@gmail.com").orElse(null);
		if (user == null) {
			user = new User();
			user.setName("ayush");
			user.setEmail("ayush@gmail.com");
			user.setPassword(passwordEncoder.encode("ayush"));
			user.setRoles(List.of(roleAdmin));
			user.setUserId(UUID.randomUUID().toString());

			userRepository.save(user);

		}

	}

}
