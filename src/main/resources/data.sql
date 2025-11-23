-- Sample data for testing
-- Health Mentor
INSERT INTO users (user_id, name, email, password, phone_number, about, profile_picture, role, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES
('mentor_001', 'Dr. Sarah Johnson', 'mentor1@gmail.com', '$2a$10$8K2L0Hutwqk9H.4n6L7lOe6Q9cJcQ9cJcQ9cJcQ9cJcQ9cJcQ9cJc', '+1-555-0101', 'Experienced health mentor specializing in diabetes management', 'https://example.com/profile1.jpg', 'HealthMentor', true, true, true, true);

INSERT INTO health_mentors (mentor_id, name, email, password, phone_number, about, profile_picture, area_of_expertise, mentorship_experience, certifications, recovery_story, enabled, account_non_expired, account_non_locked, credentials_non_expired, role_list) VALUES
('mentor_001', 'Dr. Sarah Johnson', 'mentor1@gmail.com', '$2a$10$8K2L0Hutwqk9H.4n6L7lOe6Q9cJcQ9cJcQ9cJcQ9cJcQ9cJcQ9cJc', '+1-555-0101', 'Experienced health mentor specializing in diabetes management', 'https://example.com/profile1.jpg', 'Diabetes Management', 8, 'Certified Diabetes Educator, RD', 'Overcame type 2 diabetes through lifestyle changes', true, true, true, true, '["ROLE_USER"]');

INSERT INTO mentor_performance (mentor_id, mentees_helped, sessions_conducted, success_rate, mentorship_rating, recovery_stories_shared, meditation_score, breathe_score, hydration_score, nutrition_score) VALUES
('mentor_001', 15, 120, 4.8, 4.9, 5, NULL, NULL, NULL, NULL);

-- Suffering Patient
INSERT INTO users (user_id, name, email, password, phone_number, about, profile_picture, role, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES
('patient_001', 'John Smith', 'patient1@gmail.com', '$2a$10$8K2L0Hutwqk9H.4n6L7lOe6Q9cJcQ9cJcQ9cJcQ9cJcQ9cJcQ9cJc', '+1-555-0102', 'Managing type 2 diabetes', 'https://example.com/profile2.jpg', 'SufferingPatient', true, true, true, true);

INSERT INTO suffering_patients (patient_id, name, email, password, phone_number, about, profile_picture, medical_condition, diagnosis_date, current_medications, emergency_contact_name, emergency_contact_phone, enabled, account_non_expired, account_non_locked, credentials_non_expired, role_list) VALUES
('patient_001', 'John Smith', 'patient1@gmail.com', '$2a$10$8K2L0Hutwqk9H.4n6L7lOe6Q9cJcQ9cJcQ9cJcQ9cJcQ9cJcQ9cJc', '+1-555-0102', 'Managing type 2 diabetes', 'https://example.com/profile2.jpg', 'Type 2 Diabetes', '2023-01-15', 'Metformin 500mg twice daily', 'Jane Smith', '+1-555-0103', true, true, true, true, '["ROLE_USER"]');

-- Doctor
INSERT INTO users (user_id, name, email, password, phone_number, about, profile_picture, role, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES
('doctor_001', 'Dr. Michael Chen', 'doctor1@gmail.com', '$2a$10$8K2L0Hutwqk9H.4n6L7lOe6Q9cJcQ9cJcQ9cJcQ9cJcQ9cJcQ9cJc', '+1-555-0104', 'Endocrinologist specializing in diabetes care', 'https://example.com/profile3.jpg', 'Doctor', true, true, true, true);

INSERT INTO doctors (doctor_id, name, email, password, phone_number, about, profile_picture, specialization, license_number, hospital_affiliation, years_of_experience, emergency_available, enabled, account_non_expired, account_non_locked, credentials_non_expired, role_list) VALUES
('doctor_001', 'Dr. Michael Chen', 'doctor1@gmail.com', '$2a$10$8K2L0Hutwqk9H.4n6L7lOe6Q9cJcQ9cJcQ9cJcQ9cJcQ9cJcQ9cJc', '+1-555-0104', 'Endocrinologist specializing in diabetes care', 'https://example.com/profile3.jpg', 'Endocrinology', 'MD123456', 'City General Hospital', 12, true, true, true, true, true, '["ROLE_USER"]');

-- Recovered Patient
INSERT INTO users (user_id, name, email, password, phone_number, about, profile_picture, role, enabled, account_non_expired, account_non_locked, credentials_non_expired) VALUES
('recovered_001', 'Emily Davis', 'recovered1@gmail.com', '$2a$10$8K2L0Hutwqk9H.4n6L7lOe6Q9cJcQ9cJcQ9cJcQ9cJcQ9cJcQ9cJc', '+1-555-0105', 'Successfully managed diabetes through lifestyle changes', 'https://example.com/profile4.jpg', 'RecoveredPatient', true, true, true, true);

INSERT INTO recovered_patients (patient_id, name, email, password, phone_number, about, profile_picture, previous_condition, recovery_date, recovery_methods, willing_to_mentor, enabled, account_non_expired, account_non_locked, credentials_non_expired, role_list) VALUES
('recovered_001', 'Emily Davis', 'recovered1@gmail.com', '$2a$10$8K2L0Hutwqk9H.4n6L7lOe6Q9cJcQ9cJcQ9cJcQ9cJcQ9cJcQ9cJc', '+1-555-0105', 'Successfully managed diabetes through lifestyle changes', 'https://example.com/profile4.jpg', 'Type 2 Diabetes', '2023-06-01', 'Diet, exercise, meditation', true, true, true, true, true, '["ROLE_USER"]');
