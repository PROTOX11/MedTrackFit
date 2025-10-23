# Fix Sidebar Redirection Issue

## Problem
Sometimes clicking on sidebar options redirects to `/user/dashboard` instead of role-specific routes (e.g., `/doctor/dashboard` for doctors).

## Root Cause Analysis
- Controllers like `SufferingPatientController` and `RecoveredPatientController` do not have the `@ModelAttribute` method to set `userRole` in the model.
- When `userRole` is not set, `base.html` falls back to `user/sidebar`, which has `/user/*` links.
- Role checks in controllers redirect unauthorized users to `/user/dashboard`.

## Plan
1. Add `@ModelAttribute` method to `SufferingPatientController` to set `userRole` and `loggedInUser`.
2. Add `@ModelAttribute` method to `RecoveredPatientController` to set `userRole` and `loggedInUser`.
3. Verify that all role-based controllers have consistent `@ModelAttribute` setup.
4. Test the fix by ensuring sidebar links point to correct role-specific routes.

## Files to Edit
- `src/main/java/com/example/medtrackfit/controllers/SufferingPatientController.java`
- `src/main/java/com/example/medtrackfit/controllers/RecoveredPatientController.java`

## Followup Steps
- Run the application and test sidebar navigation for different user roles.
- Ensure no unauthorized redirections occur.
