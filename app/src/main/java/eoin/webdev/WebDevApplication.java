package eoin.webdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebDevApplication {

	public static void main(String[] args) {
		System.out.println("First");
		SpringApplication.run(WebDevApplication.class, args);
		System.out.println("Testing");
	}
}
