package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.HealthMentor;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface HealthMentorService extends UserDetailsService {
    HealthMentor saveHealthMentor(HealthMentor healthMentor);
    HealthMentor getHealthMentorByEmail(String email);
    boolean isHealthMentorExistByEmail(String email);
}
