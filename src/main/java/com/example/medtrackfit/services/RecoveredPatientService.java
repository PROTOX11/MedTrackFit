package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.RecoveredPatient;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface RecoveredPatientService extends UserDetailsService {
    RecoveredPatient saveRecoveredPatient(RecoveredPatient recoveredPatient);
    RecoveredPatient getRecoveredPatientByEmail(String email);
    boolean isRecoveredPatientExistByEmail(String email);
}
