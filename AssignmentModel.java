package com.example.fbauth.TeacherFiles;

/**
 * Model representing an Assignment.
 * Used for Firebase Firestore binding.
 */
@SuppressWarnings("unused")
public class AssignmentModel {
    private String assignmentId;
    private String title;
    private String description;

    // Required empty constructor for Firebase
    public AssignmentModel() {}

    public AssignmentModel(String assignmentId, String title, String description) {
        this.assignmentId = assignmentId;
        this.title = title;
        this.description = description;
    }

    public String getAssignmentId() {
        return assignmentId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
