package com.two.tumbler;

//import com.two.tumbler.repository.MemberRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
//import com.two.tumbler.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TumblerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TumblerApplication.class, args);
	}
}
