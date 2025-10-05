package com.example.medtrackfit.services.impl;

import com.example.medtrackfit.entities.HealthMentor;
import com.example.medtrackfit.entities.MentorPerformance;
import com.medtrackfit.helper.AppConstants;
import com.example.medtrackfit.repositories.HealthMentorRepository;
import com.example.medtrackfit.services.HealthMentorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HealthMentorServiceImpl implements HealthMentorService {

    @Autowired
    private HealthMentorRepository healthMentorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public HealthMentor saveHealthMentor(HealthMentor healthMentor) {
        if (healthMentorRepository.existsByEmail(healthMentor.getEmail())) {
            throw new IllegalStateException("Health Mentor with email " + healthMentor.getEmail() + " already exists");
        }
        healthMentor.setPassword(passwordEncoder.encode(healthMentor.getPassword()));
        healthMentor.setRoleList(List.of(AppConstants.ROLE_USER));
        logger.info("Encoded password for health mentor: {}", healthMentor.getEmail());

        if (healthMentor.getMentorPerformance() == null) {
            MentorPerformance performance = MentorPerformance.builder()
                    .mentor(healthMentor)
                    .menteesHelped(0)
                    .sessionsConducted(0)
                    .successRate(0.0)
                    .mentorshipRating(0.0)
                    .recoveryStoriesShared(0)
                    .build();
            healthMentor.setMentorPerformance(performance);
        }
        HealthMentor savedMentor = healthMentorRepository.save(healthMentor);
        logger.info("Health Mentor saved with mentorId: {}, has performance: {}",
                savedMentor.getMentorId(), savedMentor.getMentorPerformance() != null);
        return savedMentor;
    }

    @Override
    public HealthMentor getHealthMentorByEmail(String email) {
        return healthMentorRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Health Mentor not found with email: " + email));
    }

    @Override
    public boolean isHealthMentorExistByEmail(String email) {
        return healthMentorRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getHealthMentorByEmail(email);
    }
}
