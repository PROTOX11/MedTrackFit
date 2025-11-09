package com.example.medtrackfit.services;

import com.example.medtrackfit.entities.SufferingPatient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface SufferingPatientService extends UserDetailsService {
    SufferingPatient saveSufferingPatient(SufferingPatient sufferingPatient);
    SufferingPatient getSufferingPatientByEmail(String email);
    boolean isSufferingPatientExistByEmail(String email);
    List<SufferingPatient> getAllSufferingPatients();
    Page<SufferingPatient> getAllSufferingPatients(Pageable pageable);
    List<SufferingPatient> searchSufferingPatientsByName(String name);
    Page<SufferingPatient> searchSufferingPatientsByName(String name, Pageable pageable);
    SufferingPatient getSufferingPatientById(String patientId);
    List<SufferingPatient> getConnectedPatients(String doctorId);
    void connectPatientToDoctor(String patientId, String doctorId);
    boolean isPatientConnectedToDoctor(String patientId, String doctorId);
}
