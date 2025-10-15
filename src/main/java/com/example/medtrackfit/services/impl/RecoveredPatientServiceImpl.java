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

import java.util.ArrayList;
import java.util.Arrays;
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
        recoveredPatient.setRoleList(Arrays.asList(AppConstants.ROLE_USER));
        logger.info("Encoded password for recovered patient: {}", recoveredPatient.getEmail());

        if (recoveredPatient.getRecoveredPatientPerformance() == null) {
            RecoveredPatientPerformance performance = new RecoveredPatientPerformance();
            performance.setRecoveredPatient(recoveredPatient);
            performance.setRecoveryScore(0);
            performance.setMentoringSessions(0);
            performance.setLivesImpacted(0);
            performance.setMentorRating(0.0);
            performance.setStoryViews(0);
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

    @Override
    public List<RecoveredPatient> getAllRecoveredPatients() {
        List<RecoveredPatient> patients = new ArrayList<>();

        // Dummy recovered patient 1
        RecoveredPatient patient1 = new RecoveredPatient();
        patient1.setName("John Doe");
        patient1.setEmail("john.doe@example.com");
        patient1.setPreviousCondition("Type 2 Diabetes");
        patient1.setRecoveryStory("I was diagnosed with Type 2 Diabetes five years ago. Through consistent exercise, a balanced diet, and guidance from my mentor, I managed to reverse my condition and now live a healthy life. Sharing my journey to inspire others.");
        patient1.setAbout("Experienced in diabetes management and motivational coaching.");
        patient1.setRecoveryDuration("5 years");
        patient1.setMentorHelp("My mentor provided personalized nutrition plans and emotional support.");
        patient1.setWillingToMentor(true);
        patients.add(patient1);

        // Dummy recovered patient 2
        RecoveredPatient patient2 = new RecoveredPatient();
        patient2.setName("Jane Smith");
        patient2.setEmail("jane.smith@example.com");
        patient2.setPreviousCondition("Hypertension");
        patient2.setRecoveryStory("Struggled with high blood pressure for years. Adopted yoga and stress management techniques recommended by my health mentor, leading to full recovery. Happy to guide others on this path.");
        patient2.setAbout("Yoga enthusiast and hypertension survivor.");
        patient2.setRecoveryDuration("3 years");
        patient2.setMentorHelp("Learned breathing exercises and lifestyle changes from my mentor.");
        patient2.setWillingToMentor(true);
        patients.add(patient2);

        // Dummy recovered patient 3
        RecoveredPatient patient3 = new RecoveredPatient();
        patient3.setName("Mike Johnson");
        patient3.setEmail("mike.johnson@example.com");
        patient3.setPreviousCondition("Obesity");
        patient3.setRecoveryStory("Lost 50kg through a structured fitness program and dietary overhaul. My recovered patient mentor shared real-life tips that made all the difference. Now, I'm fit and eager to help.");
        patient3.setAbout("Fitness trainer specializing in weight loss.");
        patient3.setRecoveryDuration("2 years");
        patient3.setMentorHelp("Received workout routines and accountability from my mentor.");
        patient3.setWillingToMentor(false);
        patients.add(patient3);

        return patients;
    }
}
