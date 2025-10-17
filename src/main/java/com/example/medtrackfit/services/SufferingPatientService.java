package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.SufferingPatient;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface SufferingPatientService extends UserDetailsService {
    SufferingPatient saveSufferingPatient(SufferingPatient sufferingPatient);
    SufferingPatient getSufferingPatientByEmail(String email);
    boolean isSufferingPatientExistByEmail(String email);
    List<SufferingPatient> getAllSufferingPatients();
}
