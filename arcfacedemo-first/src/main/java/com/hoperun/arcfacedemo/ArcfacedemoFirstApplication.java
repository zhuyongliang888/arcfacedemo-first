package com.hoperun.arcfacedemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@SpringBootApplication
public class ArcfacedemoFirstApplication {

	@RequestMapping(value="/*")
	public String printHello() {
		return "hello world";
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ArcfacedemoFirstApplication.class, args);
	}
}
