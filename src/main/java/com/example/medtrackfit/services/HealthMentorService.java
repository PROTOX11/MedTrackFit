package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.HealthMentor;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface HealthMentorService extends UserDetailsService {
    HealthMentor saveHealthMentor(HealthMentor healthMentor);
    HealthMentor getHealthMentorByEmail(String email);
    boolean isHealthMentorExistByEmail(String email);
    List<HealthMentor> getAllHealthMentors();
}
