package com.example.medtrackfit.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Entity
@Table(name = "health_mentors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HealthMentor implements UserDetails {

    @Id
    @Column(name = "mentor_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String mentorId;

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

    @Column(name = "area_of_expertise")
    private String areaOfExpertise;

    @Column(name = "recovery_story", length = 2000)
    private String recoveryStory;

    @Column(name = "mentorship_experience")
    private Integer mentorshipExperience;

    @Column(name = "certifications")
    private String certifications;

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

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Contact> contacts = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "mentor")
    private MentorPerformance mentorPerformance;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_HEALTH_MENTOR"));
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
