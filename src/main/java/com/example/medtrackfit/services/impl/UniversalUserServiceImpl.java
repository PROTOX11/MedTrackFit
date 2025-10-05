package com.example.medtrackfit.services.impl;

import com.example.medtrackfit.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UniversalUserServiceImpl implements UniversalUserService {

    @Autowired
    @Lazy
    private DoctorService doctorService;

    @Autowired
    @Lazy
    private HealthMentorService healthMentorService;

    @Autowired
    @Lazy
    private SufferingPatientService sufferingPatientService;

    @Autowired
    @Lazy
    private RecoveredPatientService recoveredPatientService;

    @Override
    public UserDetails getUserByEmail(String email) {
        // Try to find user in each role-based collection
        try {
            if (doctorService.isDoctorExistByEmail(email)) {
                return doctorService.getDoctorByEmail(email);
            }
        } catch (Exception e) {
            // User not found in doctors collection
        }

        try {
            if (healthMentorService.isHealthMentorExistByEmail(email)) {
                return healthMentorService.getHealthMentorByEmail(email);
            }
        } catch (Exception e) {
            // User not found in health mentors collection
        }

        try {
            if (sufferingPatientService.isSufferingPatientExistByEmail(email)) {
                return sufferingPatientService.getSufferingPatientByEmail(email);
            }
        } catch (Exception e) {
            // User not found in suffering patients collection
        }

        try {
            if (recoveredPatientService.isRecoveredPatientExistByEmail(email)) {
                return recoveredPatientService.getRecoveredPatientByEmail(email);
            }
        } catch (Exception e) {
            // User not found in recovered patients collection
        }

        return null; // User not found in any collection
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        return doctorService.isDoctorExistByEmail(email) ||
               healthMentorService.isHealthMentorExistByEmail(email) ||
               sufferingPatientService.isSufferingPatientExistByEmail(email) ||
               recoveredPatientService.isRecoveredPatientExistByEmail(email);
    }

    @Override
    public String getUserRole(String email) {
        if (doctorService.isDoctorExistByEmail(email)) {
            return "Doctor";
        } else if (healthMentorService.isHealthMentorExistByEmail(email)) {
            return "HealthMentor";
        } else if (sufferingPatientService.isSufferingPatientExistByEmail(email)) {
            return "SufferingPatient";
        } else if (recoveredPatientService.isRecoveredPatientExistByEmail(email)) {
            return "RecoveredPatient";
        }
        return null;
    }

    @Override
    public String getUserId(String email) {
        UserDetails userDetails = getUserByEmail(email);
        if (userDetails != null) {
            // Extract ID based on the user type
            if (userDetails instanceof com.example.medtrackfit.entities.Doctor) {
                return ((com.example.medtrackfit.entities.Doctor) userDetails).getDoctorId();
            } else if (userDetails instanceof com.example.medtrackfit.entities.HealthMentor) {
                return ((com.example.medtrackfit.entities.HealthMentor) userDetails).getMentorId();
            } else if (userDetails instanceof com.example.medtrackfit.entities.SufferingPatient) {
                return ((com.example.medtrackfit.entities.SufferingPatient) userDetails).getPatientId();
            } else if (userDetails instanceof com.example.medtrackfit.entities.RecoveredPatient) {
                return ((com.example.medtrackfit.entities.RecoveredPatient) userDetails).getPatientId();
            }
        }
        return null;
    }

    @Override
    public String getUserName(String email) {
        UserDetails userDetails = getUserByEmail(email);
        if (userDetails != null) {
            // Extract name based on the user type
            if (userDetails instanceof com.example.medtrackfit.entities.Doctor) {
                return ((com.example.medtrackfit.entities.Doctor) userDetails).getName();
            } else if (userDetails instanceof com.example.medtrackfit.entities.HealthMentor) {
                return ((com.example.medtrackfit.entities.HealthMentor) userDetails).getName();
            } else if (userDetails instanceof com.example.medtrackfit.entities.SufferingPatient) {
                return ((com.example.medtrackfit.entities.SufferingPatient) userDetails).getName();
            } else if (userDetails instanceof com.example.medtrackfit.entities.RecoveredPatient) {
                return ((com.example.medtrackfit.entities.RecoveredPatient) userDetails).getName();
            }
        }
        return null;
    }
}
