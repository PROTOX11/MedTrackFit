package com.example.medtrackfit.services.impl;

import com.example.medtrackfit.entities.*;
import com.example.medtrackfit.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class RoleBasedUserServiceImpl implements RoleBasedUserService {

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
    public UserDetails saveUserByRole(String role, String name, String email, String password, 
                                     String phoneNumber, String about, String profilePicture) {
        switch (role.toLowerCase()) {
            case "doctor":
                Doctor doctor = Doctor.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .phoneNumber(phoneNumber)
                        .about(about)
                        .profilePicture(profilePicture)
                        .build();
                return doctorService.saveDoctor(doctor);

            case "healthmentor":
                HealthMentor healthMentor = HealthMentor.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .phoneNumber(phoneNumber)
                        .about(about)
                        .profilePicture(profilePicture)
                        .build();
                return healthMentorService.saveHealthMentor(healthMentor);

            case "sufferingpatient":
                SufferingPatient sufferingPatient = SufferingPatient.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .phoneNumber(phoneNumber)
                        .about(about)
                        .profilePicture(profilePicture)
                        .build();
                return sufferingPatientService.saveSufferingPatient(sufferingPatient);

            case "recoveredpatient":
                RecoveredPatient recoveredPatient = RecoveredPatient.builder()
                        .name(name)
                        .email(email)
                        .password(password)
                        .phoneNumber(phoneNumber)
                        .about(about)
                        .profilePicture(profilePicture)
                        .build();
                return recoveredPatientService.saveRecoveredPatient(recoveredPatient);

            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    @Override
    public UserDetails getUserByRoleAndEmail(String role, String email) {
        switch (role.toLowerCase()) {
            case "doctor":
                return doctorService.getDoctorByEmail(email);
            case "healthmentor":
                return healthMentorService.getHealthMentorByEmail(email);
            case "sufferingpatient":
                return sufferingPatientService.getSufferingPatientByEmail(email);
            case "recoveredpatient":
                return recoveredPatientService.getRecoveredPatientByEmail(email);
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    @Override
    public boolean isUserExistByRoleAndEmail(String role, String email) {
        switch (role.toLowerCase()) {
            case "doctor":
                return doctorService.isDoctorExistByEmail(email);
            case "healthmentor":
                return healthMentorService.isHealthMentorExistByEmail(email);
            case "sufferingpatient":
                return sufferingPatientService.isSufferingPatientExistByEmail(email);
            case "recoveredpatient":
                return recoveredPatientService.isRecoveredPatientExistByEmail(email);
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
