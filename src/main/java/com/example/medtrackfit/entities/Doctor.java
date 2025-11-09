package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.medtrackfit.helper.AppConstants;

import java.util.*;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctor implements UserDetails {

    @Id
    @Column(name = "doctor_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String doctorId;

    public String getDoctorId() {
        return doctorId;
    }

    @Column(name = "name", nullable = false)
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Column(unique = true, nullable = false)
    private String email;

    public void setEmail(String email) {
        this.email = email;
    }

    @Getter(AccessLevel.NONE)
    private String password;

    @Column(length = 1000)
    private String about;

    public void setAbout(String about) {
        this.about = about;
    }

    @Column(length = 1000)
    private String phoneNumber;

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Column(name = "profile_picture")
    private String profilePicture;

    public String getProfilePicture() {
        return profilePicture;
    }

    @Column(name = "license_number")
    private String licenseNumber;

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @Column(name = "specialization")
    private String specialization;

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    @Column(name = "qualification")
    private String qualification;

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    @Column(name = "school")
    private String school;

    @Column(name = "blog", length = 2000)
    private String blog;

    @Column(name = "patient_connected_id")
    private String patientConnectedId;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<BlogPost> blogPosts = new ArrayList<>();

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

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Contact> contacts = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "doctor")
    private DoctorPerformance doctorPerformance;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AppConstants.ROLE_DOCTOR));
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
