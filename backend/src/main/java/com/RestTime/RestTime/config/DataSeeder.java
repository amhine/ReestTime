package com.RestTime.RestTime.config;

import com.RestTime.RestTime.model.entity.User;
import com.RestTime.RestTime.model.enumeration.Role;
import com.RestTime.RestTime.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String rhEmail = "rh@rh.com";
        String password = "rh@rh.com";

        if (!userRepository.existsByEmail(rhEmail)) {
            User rh = User.builder()
                    .nom("RH")
                    .prenom("RestTime")
                    .email(rhEmail)
                    .motDePasse(passwordEncoder.encode(password))
                    .role(Role.RH)
                    .soldeConges(25.0)
                    .build();

            userRepository.save(rh);
            System.out.println("RH créé avec succès ! Email: " + rhEmail + " / Password: " + password);
        } else {
            User rh = userRepository.findByEmail(rhEmail).orElseThrow();
            rh.setMotDePasse(passwordEncoder.encode(password));
            userRepository.save(rh);
            System.out.println("Le RH existe déjà, mot de passe mis à jour !");
        }
    }
}