package com.example.medtrackfit.services.impl;

import com.example.medtrackfit.entities.PatientPerformance;
import com.example.medtrackfit.entities.SufferingPatient;
import com.medtrackfit.helper.AppConstants;
import com.example.medtrackfit.repositories.SufferingPatientRepository;
import com.example.medtrackfit.services.SufferingPatientService;
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
public class SufferingPatientServiceImpl implements SufferingPatientService {

    @Autowired
    private SufferingPatientRepository sufferingPatientRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Transactional
    public SufferingPatient saveSufferingPatient(SufferingPatient sufferingPatient) {
        if (sufferingPatientRepository.existsByEmail(sufferingPatient.getEmail())) {
            throw new IllegalStateException("Suffering Patient with email " + sufferingPatient.getEmail() + " already exists");
        }
        sufferingPatient.setPassword(passwordEncoder.encode(sufferingPatient.getPassword()));
        sufferingPatient.setRoleList(List.of(AppConstants.ROLE_USER));
        logger.info("Encoded password for suffering patient: {}", sufferingPatient.getEmail());

        if (sufferingPatient.getPatientPerformance() == null) {
            PatientPerformance performance = PatientPerformance.builder()
                    .sufferingPatient(sufferingPatient)
                    .healthScore(0)
                    .treatmentProgress(0)
                    .sessionsAttended(0)
                    .medicationAdherence(0.0)
                    .overallImprovement(0.0)
                    .build();
            sufferingPatient.setPatientPerformance(performance);
        }
        SufferingPatient savedPatient = sufferingPatientRepository.save(sufferingPatient);
        logger.info("Suffering Patient saved with patientId: {}, has performance: {}",
                savedPatient.getPatientId(), savedPatient.getPatientPerformance() != null);
        return savedPatient;
    }

    @Override
    public SufferingPatient getSufferingPatientByEmail(String email) {
        return sufferingPatientRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Suffering Patient not found with email: " + email));
    }

    @Override
    public boolean isSufferingPatientExistByEmail(String email) {
        return sufferingPatientRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return getSufferingPatientByEmail(email);
    }
}
