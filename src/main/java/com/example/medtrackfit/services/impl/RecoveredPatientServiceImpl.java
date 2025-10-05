package com.example.medtrackfit.services.impl;

import com.example.medtrackfit.entities.RecoveredPatient;
import com.example.medtrackfit.entities.RecoveredPatientPerformance;
import com.medtrackfit.helper.AppConstants;
import com.example.medtrackfit.repositories.RecoveredPatientRepository;
import com.example.medtrackfit.services.RecoveredPatientService;
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
public class RecoveredPatientServiceImpl implements RecoveredPatientService {

    @Autowired
    private RecoveredPatientRepository recoveredPatientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public RecoveredPatient saveRecoveredPatient(RecoveredPatient recoveredPatient) {
        if (recoveredPatientRepository.existsByEmail(recoveredPatient.getEmail())) {
            throw new IllegalStateException("Recovered Patient with email " + recoveredPatient.getEmail() + " already exists");
        }
        recoveredPatient.setPassword(passwordEncoder.encode(recoveredPatient.getPassword()));
        recoveredPatient.setRoleList(List.of(AppConstants.ROLE_USER));
        logger.info("Encoded password for recovered patient: {}", recoveredPatient.getEmail());

        if (recoveredPatient.getRecoveredPatientPerformance() == null) {
            RecoveredPatientPerformance performance = RecoveredPatientPerformance.builder()
                    .recoveredPatient(recoveredPatient)
                    .recoveryScore(0)
                    .mentoringSessions(0)
                    .livesImpacted(0)
                    .mentorRating(0.0)
                    .storyViews(0)
                    .build();
            recoveredPatient.setRecoveredPatientPerformance(performance);
        }
        RecoveredPatient savedPatient = recoveredPatientRepository.save(recoveredPatient);
        logger.info("Recovered Patient saved with patientId: {}, has performance: {}",
                savedPatient.getPatientId(), savedPatient.getRecoveredPatientPerformance() != null);
        return savedPatient;
    }

    @Override
    public RecoveredPatient getRecoveredPatientByEmail(String email) {
        return recoveredPatientRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Recovered Patient not found with email: " + email));
    }

    @Override
    public boolean isRecoveredPatientExistByEmail(String email) {
        return recoveredPatientRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getRecoveredPatientByEmail(email);
    }
}
