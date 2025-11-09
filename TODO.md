# GuidancePro: Smart Guidance Office Management System - Implementation Plan

## Step 1: Create Supporting Data Model Classes
- [x] Create Student.java: Define Student class with fields like id, name, email, etc.
- [x] Create Appointment.java: Define Appointment class with fields like id, studentId, counselor, date, time, status.
- [x] Create Case.java: Define Case class with fields like id, studentId, description, status, wellnessMetrics.
- [x] Create Workshop.java: Define Workshop class with fields like id, title, date, time, description, participants.

## Step 2: Create Module Panels
- [x] Create AppointmentManager.java: JPanel for managing appointments (JTable for listing, buttons for add/edit/delete, JDialog for forms).
- [x] Create CaseTracker.java: JPanel for tracking student cases (JTable for cases, fields for wellness, buttons for add/update).
- [x] Create WorkshopOrganizer.java: JPanel for organizing workshops (JTable for events, scheduling features, participant management).
- [x] Create ReportGenerator.java: JPanel for generating reports (buttons to generate reports, JTextArea for display, option to export).

## Step 3: Create Main Dashboard
- [x] Create MainDashboard.java: JFrame with JTabbedPane containing the four module panels (Appointments, Cases, Workshops, Reports).

## Step 4: Integrate Authentication
- [x] Modify LoginForm.java: On successful login, dispose the login window and open MainDashboard.

## Step 5: Implement Role-Based Dashboards and Logout
- [x] Create User.java: Define User class with fields like id, username, password, role, name, email.
- [x] Create Role.java: Enum for user roles (ADMIN, COUNSELOR, STUDENT).
- [x] Modify LoginForm.java: Add list of users, handle login for different roles, open appropriate dashboard.
- [x] Create AdminDashboard.java: Full access dashboard with user management tab (integrate registration).
- [x] Create CounselorDashboard.java: Dashboard with Appointments, Cases, Workshops, Reports tabs.
- [x] Create StudentDashboard.java: Limited dashboard with My Appointments, My Cases, Workshops tabs.
- [x] Add logout functionality to all dashboards (dispose dashboard, open LoginForm).
- [x] Remove registration access from LoginForm.

## Step 6: Testing and Validation
- [x] Compile all Java files.
- [x] Run the application: Test login for admin, counselor, student roles.
- [x] Verify dashboards: Admin has full access + user management, Counselor has subset, Student has limited view.
- [x] Test logout functionality.
- [x] Verify registration is only accessible to admin.

## Step 7: Complete User Add in Admin with Random 7-Digit ID
- [x] Add "Name" text field to RegistrationForm.java form layout (before email).
- [x] Update actionPerformed method to retrieve name from the new field.
- [x] Implement random 7-digit ID generation with uniqueness check.
- [x] Create and add new User object with random ID and provided inputs.

## Step 8: Ensure Only Registered Accounts Can Be Appointed for Appointments and Cases
- [x] Add studentId field to User.java and getter/setter.
- [x] Update UserDatabase.java to include studentId for the student user.
- [x] Modify CounselorDashboard.java to populate students list from UserDatabase, filtering users with role STUDENT, and creating Student objects from them.
- [x] Modify StudentDashboard.java to populate students list with only the logged-in student.
- [x] Disable add/edit/delete buttons in AppointmentManager, CaseTracker, and WorkshopOrganizer for students.
- [x] Compile the Java files.
- [x] Test the application to verify only registered students appear in appointment and case selections, and students cannot add appointments/cases.

## Step 9: Implement Real-Time Updates and Notifications
- [x] Add Timer-based automatic refresh to AppointmentManager.java table every 5 seconds.
- [x] Add Timer-based automatic refresh to CaseTracker.java table every 5 seconds.
- [x] Add Timer-based automatic refresh to WorkshopOrganizer.java table every 5 seconds.
- [x] Add success notifications in AppointmentManager.java for add/edit/delete actions.
- [x] Add success notifications in CaseTracker.java for add/edit/delete actions.
- [x] Add success notifications in WorkshopOrganizer.java for add/edit/delete actions.
- [x] Compile the Java files.
- [x] Test the real-time updates by opening multiple dashboards and verifying table refreshes and notifications.
