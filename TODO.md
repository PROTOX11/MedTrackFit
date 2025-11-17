# TODO: Create Profile Page for Suffering Patients

## Tasks to Complete:
- [x] Create `src/main/resources/templates/suff-pat/profile.html` by copying and modifying `user/profile.html`, adapting links to suff-pat routes (e.g., edit-profile to `/suff-pat/edit-profile`).
- [x] Add a new `@GetMapping("/profile")` method in `SufferingPatientController.java` to return "suff-pat/profile".
- [x] Update `src/main/resources/templates/suff-pat/suff-pat_sidebar.html` to add a "Profile" link in the menu, positioned after the Dashboard link.
- [x] Fix Thymeleaf template parsing error by correcting the fragment signature to match suff-pat_base.
- [x] Fix SpringEL expression error by replacing `loggedInUser?.role` with `loggedInUser?.medicalCondition` to match SufferingPatient entity.
- [x] Fix Thymeleaf template parsing error by removing extra attributes in html tag to match other suff-pat templates.
- [x] Remove health goals and medical history sections from profile page.
- [x] Add patient body level input section with height, weight, BMI calculation, and blood pressure fields.
- [ ] Test the profile page by navigating to `/suff-pat/profile` and verify the sidebar link works.
- [ ] Ensure any edit-profile functionality points to the correct suff-pat route.
