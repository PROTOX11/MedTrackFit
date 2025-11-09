package com.example.medtrackfit.services.impl;
import com.example.medtrackfit.entities.HealthMentor;
import com.example.medtrackfit.entities.MentorPerformance;
import com.example.medtrackfit.entities.Connections;
import com.example.medtrackfit.entities.SufferingPatient;
import com.example.medtrackfit.entities.Doctor;
import com.example.medtrackfit.entities.ConnectionId;
import com.medtrackfit.helper.AppConstants;
import com.example.medtrackfit.repositories.HealthMentorRepository;
import com.example.medtrackfit.repo.ConnectionsRepo;
import com.example.medtrackfit.services.HealthMentorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Objects;

@Service
public class HealthMentorServiceImpl implements HealthMentorService {

    @Autowired
    private HealthMentorRepository healthMentorRepository;

    @Autowired
    private ConnectionsRepo connectionsRepo;

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

    @Override
    public List<HealthMentor> getAllHealthMentors() {
        return healthMentorRepository.findAll();
    }

    // Connection management methods implementation
    @Override
    public List<Connections> getPendingPatientRequests(String mentorId) {
        // For now, return empty list - will be implemented with proper connection status
        return connectionsRepo.findByConnectedUserUserId(mentorId)
                .stream()
                .filter(conn -> conn.getUser() != null && conn.getUser().getRole() != null && conn.getUser().getRole().equals("SufferingPatient"))
                .collect(Collectors.toList());
    }

    @Override
    public List<Connections> getApprovedPatientConnections(String mentorId) {
        return connectionsRepo.findByConnectedUserUserId(mentorId)
                .stream()
                .filter(conn -> conn.getUser() != null && conn.getUser().getRole() != null && conn.getUser().getRole().equals("SufferingPatient"))
                .collect(Collectors.toList());
    }

    @Override
    public List<Connections> getConnectedDoctors(String mentorId) {
        return connectionsRepo.findByUserUserId(mentorId)
                .stream()
                .filter(conn -> conn.getConnectedUser() != null && conn.getConnectedUser().getRole() != null && conn.getConnectedUser().getRole().equals("Doctor"))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Connections approvePatientRequest(String mentorId, String patientId) {
        Optional<Connections> connectionOpt = connectionsRepo.findByUserUserIdAndConnectedUserUserId(patientId, mentorId);
        if (connectionOpt.isPresent()) {
            Connections connection = connectionOpt.get();
            // Here we could add approval status if we extend the Connections entity
            logger.info("Approved patient request: mentor {} - patient {}", mentorId, patientId);
            return connection;
        }
        throw new RuntimeException("Connection request not found");
    }

    @Override
    @Transactional
    public Connections rejectPatientRequest(String mentorId, String patientId) {
        Optional<Connections> connectionOpt = connectionsRepo.findByUserUserIdAndConnectedUserUserId(patientId, mentorId);
        if (connectionOpt.isPresent()) {
            Connections connection = connectionOpt.get();
            connectionsRepo.delete(connection);
            logger.info("Rejected patient request: mentor {} - patient {}", mentorId, patientId);
            return connection;
        }
        throw new RuntimeException("Connection request not found");
    }

    @Override
    @Transactional
    public Connections connectPatientToDoctor(String mentorId, String patientId, String doctorId) {
        // Create connection between patient and doctor
        ConnectionId connectionId = new ConnectionId();
        connectionId.setUserId(patientId);
        connectionId.setConnectedId(doctorId);

        // Fetch actual entities from repositories
        SufferingPatient patient = null; // TODO: Inject SufferingPatientService and fetch by ID
        Doctor doctor = null; // TODO: Inject DoctorService and fetch by ID

        Connections connection = Connections.builder()
                .id(connectionId)
                .user(null)
                .connectedUser(null)
                .build();

        Connections savedConnection = connectionsRepo.save(connection);
        logger.info("Connected patient {} to doctor {} via mentor {}", patientId, doctorId, mentorId);
        return savedConnection;
    }

    // Dashboard statistics implementation
    @Override
    public long getActivePatientsCount(String mentorId) {
        return getApprovedPatientConnections(mentorId).size();
    }

    @Override
    public long getPendingRequestsCount(String mentorId) {
        return getPendingPatientRequests(mentorId).size();
    }

    @Override
    public long getConnectedDoctorsCount(String mentorId) {
        return getConnectedDoctors(mentorId).size();
    }

    @Override
    public long getMessagesTodayCount(String mentorId) {
        // Placeholder - would need message/chat system
        return 0;
    }

    @Override
    public List<SufferingPatient> getTopPatients(String mentorId, int limit) {
        // Placeholder - would need interaction tracking and proper entity relationships
        // For now, return empty list
        return List.of();
    }
}
