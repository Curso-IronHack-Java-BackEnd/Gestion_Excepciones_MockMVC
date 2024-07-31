package com.miguelprojects;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Gestion_Excepciones_MockMVC {

    public static void main(String[] args) {
        SpringApplication.run(Gestion_Excepciones_MockMVC.class, args);
    }

//    @Bean
//    CommandLineRunner runner(GradeRepository repository) {
//        return args -> {
//
//            repository.findScoreGreaterThan50();
//
//        };
//    }
}
