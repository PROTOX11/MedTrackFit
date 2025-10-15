# TODO: Implement Recovered Patients List on Connect Page

## Steps from Approved Plan:

- [ ] **Update Service Interface**: Edit `src/main/java/com/example/medtrackfit/services/RecoveredPatientService.java` to add `List<RecoveredPatient> getAllRecoveredPatients();`.

- [ ] **Update Service Implementation**: Edit `src/main/java/com/example/medtrackfit/services/impl/RecoveredPatientServiceImpl.java` to implement `getAllRecoveredPatients()` returning a hardcoded list of 3-4 dummy RecoveredPatient objects using the builder pattern.

- [ ] **Update Controller**: Edit `src/main/java/com/example/medtrackfit/controllers/SufferingPatientController.java` to autowire `RecoveredPatientService`, call `getAllRecoveredPatients()`, and add the list to the model in the `connectRecovered()` method.

- [ ] **Update Template**: Edit `src/main/resources/templates/suff-pat/connect_recovered.html` to iterate over `${recoveredPatients}` with Thymeleaf and display patient details in cards (name, previousCondition, recoveryStory).

- [ ] **Followup**: Rebuild and test the application by navigating to http://localhost:8080/suff-pat/connect_recovered to verify the dummy list displays correctly.
