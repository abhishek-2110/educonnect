package com.example.fbauth;

import android.content.Context;
import android.content.Intent;

import com.example.fbauth.AuthenticationFiles.Loginpage;
import com.example.fbauth.ParentFiles.ParentDashboard;
import com.example.fbauth.Student.StudentDashboard;
import com.example.fbauth.TeacherFiles.TeacherDashboard;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class NavigationController {

    public static void goBack(Context context) {
        navigateByRole(context, true);
    }

    public static void goForward(Context context) {
        navigateByRole(context, false);
    }

    private static void navigateByRole(Context context, boolean goBack) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            context.startActivity(new Intent(context, Loginpage.class));
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();
        FirebaseFirestore.getInstance().collection("users").document(uid)
                .get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        String role = document.getString("role");
                        Class<?> targetActivity;

                        if ("Student".equalsIgnoreCase(role)) {
                            targetActivity = StudentDashboard.class;
                        } else if ("Teacher".equalsIgnoreCase(role)) {
                            targetActivity = TeacherDashboard.class;
                        } else if ("Parent".equalsIgnoreCase(role)) {
                            targetActivity = ParentDashboard.class;
                        } else {
                            targetActivity = Loginpage.class;
                        }

                        Intent intent = new Intent(context, targetActivity);
                        context.startActivity(intent);
                    } else {
                        context.startActivity(new Intent(context, Loginpage.class));
                    }
                })
                .addOnFailureListener(e -> {
                    context.startActivity(new Intent(context, Loginpage.class));
                });
    }
}
