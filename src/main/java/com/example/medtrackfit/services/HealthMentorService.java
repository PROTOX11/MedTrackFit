package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.HealthMentor;
import com.example.medtrackfit.entities.Connections;
import com.example.medtrackfit.entities.SufferingPatient;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface HealthMentorService extends UserDetailsService {
    HealthMentor saveHealthMentor(HealthMentor healthMentor);
    HealthMentor getHealthMentorByEmail(String email);
    boolean isHealthMentorExistByEmail(String email);
    List<HealthMentor> getAllHealthMentors();

    // Connection management methods
    List<Connections> getPendingPatientRequests(String mentorId);
    List<Connections> getApprovedPatientConnections(String mentorId);
    List<Connections> getConnectedDoctors(String mentorId);
    Connections approvePatientRequest(String mentorId, String patientId);
    Connections rejectPatientRequest(String mentorId, String patientId);
    Connections connectPatientToDoctor(String mentorId, String patientId, String doctorId);

    // Dashboard statistics
    long getActivePatientsCount(String mentorId);
    long getPendingRequestsCount(String mentorId);
    long getConnectedDoctorsCount(String mentorId);
    long getMessagesTodayCount(String mentorId);
    List<SufferingPatient> getTopPatients(String mentorId, int limit);

    // Health metrics methods
    Object getMentorPerformance(String mentorId);
    void updateMeditationScore(String mentorId, int sessionSeconds);
    void updateBreatheScore(String mentorId, Integer breatheScore);
    void updateHydrationScore(String mentorId, int hydrationAmount);
    void updateNutritionScore(String mentorId, int nutritionScore);
    int calculateNutritionScore(List<Object> foodItems);

    // Connection management
    boolean connectMentorToPatient(String mentorId, String patientId);
    boolean isMentorConnectedToPatient(String mentorId, String patientId);
}
