package com.example.todo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class FirebaseOperations {
    private FirebaseAuth mAuth;
    private DatabaseReference mUserInfoRef;
    private FirebaseUser firebaseUser;
    private Context getContext;
    private ProgressDialog progressDialog;
    private FragmentManager fm;
    private User user;
    private static Map<String, User> usersDetails = new HashMap<>();

    public FirebaseOperations(Context context, FragmentManager fm){
        this.getContext = context;
        this.fm = fm;
        this.mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        mUserInfoRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid());
        user = new User();
    }

    public void registerUserInfo(final String fullname, final String username, final String mail, final String passcode) {
        progressBar("Registration", "Registering user. Please wait...");
        user.setFullname(fullname);
        user.setUsername(username);
        user.setEmail(mail);
        user.setPasscode(passcode);
        mAuth.createUserWithEmailAndPassword(mail, passcode).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //Getting reference to database and setting user details to the path users...
                    mUserInfoRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                progressDialog.dismiss();
                                Toast.makeText(getContext, "Registration successful.", Toast.LENGTH_LONG).show();
                                getContext.startActivity(new Intent(getContext, LoginAccess.class));
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(getContext, "Something went wrong: "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(getContext, "ERROR: "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void loginUser(TextInputLayout emailText, String email, TextInputLayout passwordText, String password){
        progressBar("Checking...", "Checking credentials. Please wait...");
        if (email.isEmpty()){
            progressDialog.dismiss();
            emailText.setError("E-mail is required");
            passwordText.setError(null);
        }else if (password.isEmpty()){
            progressDialog.dismiss();
            passwordText.setError("Password is required");
            emailText.setError(null);
        }else{
            emailText.setError(null);
            passwordText.setError(null);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        FirebaseUser user = mAuth.getCurrentUser();
                        getContext.startActivity(new Intent(getContext, MainActivity.class));
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getContext, "Invalid Credential", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void logoutUser(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext);
        alertDialog.setTitle("Logout...");
        alertDialog.setMessage("Confirm user logout");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext, "You are signed out", Toast.LENGTH_SHORT).show();
                getContext.startActivity(new Intent(getContext, LoginAccess.class));
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.create().dismiss();
            }
        });
        alertDialog.show();
    }

    public void deleteUser(){
        if (firebaseUser != null){
            progressBar("Deleting...", "User is being deleted. Please wait...");
            firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        getContext.startActivity(new Intent(getContext, LoginAccess.class));
                    }
                }
            });
        }
    }

    private void progressBar(String title, String message){
        progressDialog = new ProgressDialog(getContext);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static Map<String, User> getDetails(){
        return usersDetails;
    }
}








