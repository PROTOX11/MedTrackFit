# TODO: Implement Change Photo Functionality for Doctor Profile

## Information Gathered
- Doctor profile page has a "Change Photo" button in the sidebar but it's not functional
- Edit-profile page already has profile picture upload functionality
- UserService has updateProfilePicture method using CloudinaryService
- DoctorService lacks updateProfilePicture method
- DoctorController lacks /update-profile-picture endpoint

## Plan
1. ✅ Add updateProfilePicture method to DoctorService interface
2. ✅ Implement updateProfilePicture in DoctorServiceImpl (similar to UserServiceImpl)
3. ✅ Add /update-profile-picture POST endpoint to DoctorController
4. ✅ Update doctor/profile.html to make "Change Photo" button functional with form and file input

## Dependent Files to be edited
- ✅ src/main/java/com/example/medtrackfit/services/DoctorService.java
- ✅ src/main/java/com/example/medtrackfit/services/impl/DoctorServiceImpl.java
- ✅ src/main/java/com/example/medtrackfit/controllers/DoctorController.java
- ✅ src/main/resources/templates/doctor/profile.html

## Followup steps
- Test the profile picture upload functionality
- Verify image validation and error handling
- Ensure proper redirect after successful upload
