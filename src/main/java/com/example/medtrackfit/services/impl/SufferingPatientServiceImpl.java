package com.example.medtrackfit.services.impl;

import com.example.medtrackfit.entities.DoctorPatientConnection;
import com.example.medtrackfit.entities.PatientPerformance;
import com.example.medtrackfit.entities.SufferingPatient;
import com.medtrackfit.helper.AppConstants;
import com.example.medtrackfit.repositories.DoctorPatientConnectionRepository;
import com.example.medtrackfit.repositories.SufferingPatientRepository;
import com.example.medtrackfit.services.SufferingPatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SufferingPatientServiceImpl implements SufferingPatientService {

    @Autowired
    private SufferingPatientRepository sufferingPatientRepository;

    @Autowired
    private DoctorPatientConnectionRepository doctorPatientConnectionRepository;

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

    @Override
    public List<SufferingPatient> getAllSufferingPatients() {
        return sufferingPatientRepository.findAll();
    }

    @Override
    public Page<SufferingPatient> getAllSufferingPatients(Pageable pageable) {
        return sufferingPatientRepository.findAll(pageable);
    }

    @Override
    public List<SufferingPatient> searchSufferingPatientsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return getAllSufferingPatients();
        }
        return sufferingPatientRepository.findByNameContainingIgnoreCase(name.trim());
    }

    @Override
    public Page<SufferingPatient> searchSufferingPatientsByName(String name, Pageable pageable) {
        if (name == null || name.trim().isEmpty()) {
            return getAllSufferingPatients(pageable);
        }
        return sufferingPatientRepository.findByNameContainingIgnoreCase(name.trim(), pageable);
    }

    @Override
    public SufferingPatient getSufferingPatientById(String patientId) {
        return sufferingPatientRepository.findByPatientId(patientId)
                .orElseThrow(() -> new UsernameNotFoundException("Suffering Patient not found with id: " + patientId));
    }

    @Override
    public List<SufferingPatient> getConnectedPatients(String doctorId) {
        Optional<DoctorPatientConnection> connectionOpt = doctorPatientConnectionRepository.findByDoctorId(doctorId);
        if (connectionOpt.isPresent()) {
            DoctorPatientConnection connection = connectionOpt.get();
            return connection.getPatientIds().stream()
                    .map(this::getSufferingPatientById)
                    .collect(Collectors.toList());
        }
        return new java.util.ArrayList<>();
    }

    @Override
    @Transactional
    public void connectPatientToDoctor(String patientId, String doctorId) {
        SufferingPatient patient = getSufferingPatientById(patientId);
        if (patient == null) {
            throw new IllegalArgumentException("Patient not found with ID: " + patientId);
        }

        // Find or create connection
        DoctorPatientConnection connection = doctorPatientConnectionRepository.findByDoctorId(doctorId)
                .orElse(DoctorPatientConnection.builder()
                        .doctorId(doctorId)
                        .patientIds(new java.util.ArrayList<>())
                        .build());

        // Add patient ID only if not already present
        if (!connection.getPatientIds().contains(patientId)) {
            List<String> updatedIds = new ArrayList<>(connection.getPatientIds());
            updatedIds.add(patientId);
            connection.setPatientIds(updatedIds);
            doctorPatientConnectionRepository.save(connection);

            // Update patient's doctorId
            patient.setDoctorId(doctorId);
            sufferingPatientRepository.save(patient);

            logger.info("Patient {} connected to doctor {}", patientId, doctorId);
        } else {
            // Already connected → do nothing, but DON'T throw error
            logger.info("Patient {} is already connected to doctor {}. Skipping duplicate.", patientId, doctorId);
        }
    }

    @Override
    public boolean isPatientConnectedToDoctor(String patientId, String doctorId) {
        return doctorPatientConnectionRepository.existsByDoctorIdAndPatientId(doctorId, patientId);
    }
}
