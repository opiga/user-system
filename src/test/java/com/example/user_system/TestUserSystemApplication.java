package com.example.user_system;

import org.springframework.boot.SpringApplication;

public class TestUserSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(UserSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
