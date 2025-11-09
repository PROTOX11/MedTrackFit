package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "suffering_patients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SufferingPatient implements UserDetails {

    @Id
    @Column(name = "patient_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String patientId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Getter(AccessLevel.NONE)
    private String password;

    @Column(length = 1000)
    private String about;

    @Column(length = 1000)
    private String phoneNumber;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "medical_condition")
    private String medicalCondition;

    @Column(name = "current_symptoms", length = 2000)
    private String currentSymptoms;

    @Column(name = "treatment_duration")
    private String treatmentDuration;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "preferred_mentor_type")
    private String preferredMentorType;

    @Column(name = "doctor_id")
    private String doctorId;

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    @Builder.Default
    private boolean enabled = true;
    
    @Builder.Default
    private boolean emailVerified = false;
    
    @Builder.Default
    private boolean phoneVerified = false;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private Providers provider = Providers.SELF;

    private String providerUserId;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roleList = new ArrayList<>();

    @OneToMany(mappedBy = "sufferingPatient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Contact> contacts = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "sufferingPatient")
    private PatientPerformance patientPerformance;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_SUFFERING_PATIENT"));
        for (String role : roleList) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public String getPassword() {
        return this.password;
    }
}
