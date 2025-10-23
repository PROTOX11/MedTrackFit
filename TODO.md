# Remove User Routes Completely

## Tasks
- [ ] Delete UserController.java
- [ ] Delete ContactController.java
- [ ] Remove /user/** authentication from SecurityConfig.java
- [ ] Update redirects in DoctorController.java from /user/dashboard to /
- [ ] Update redirects in MentorController.java from /user/dashboard to /
- [ ] Update CustomAuthenticationSuccessHandler.java redirect from /user/dashboard to /
- [ ] Update OAuthAuthenticationSuccessHandler.java redirect from /user/dashboard to /
- [ ] Delete src/main/resources/templates/user/ directory
- [ ] Update suff-pat_base.html to remove user navbar/footer includes
- [ ] Update recoveredpatient_base.html to remove user navbar/footer includes
- [ ] Update mentor templates to remove user navbar/footer includes
- [ ] Update dashboard HTML files to remove JS fetches to /user/ endpoints
