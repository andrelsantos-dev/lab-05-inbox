package com.alssant.asclepio;

import org.springframework.boot.SpringApplication;

public class TestLab05InboxApplication {

	public static void main(String[] args) {
		SpringApplication.from(Lab05InboxApplication::main)
				.with(TestcontainersConfiguration.class)
				.run(args);
	}

}
