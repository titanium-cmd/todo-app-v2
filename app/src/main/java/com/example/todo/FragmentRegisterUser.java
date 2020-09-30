package com.example.todo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentRegisterUser extends Fragment {
    private TextInputLayout fullNameTextInput, userNameTextInput, emailTextInput, passcodeTextInput, confirmPasscodeTextInput;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_user, container, false);
        final FirebaseOperations operations = new FirebaseOperations(getContext(), getActivity().getSupportFragmentManager());

        fullNameTextInput = view.findViewById(R.id.fullnameTextInput);
        userNameTextInput = view.findViewById(R.id.usernameTextInput);
        emailTextInput = view.findViewById(R.id.emailTextInput);
        passcodeTextInput = view.findViewById(R.id.passcodeTextInput);
        confirmPasscodeTextInput = view.findViewById(R.id.confirmPasscodeTextInput);
        Button registerUser = view.findViewById(R.id.registerUserBtn);
        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = fullNameTextInput.getEditText().getText().toString();
                String userName = userNameTextInput.getEditText().getText().toString();
                String email = emailTextInput.getEditText().getText().toString();
                String passcode = passcodeTextInput.getEditText().getText().toString();
                String confirmPasscode = confirmPasscodeTextInput.getEditText().getText().toString();

                boolean isValid  = validateInputs(fullName, userName, email, passcode, confirmPasscode);
                if (isValid){
                    operations.registerUserInfo(fullName, userName, email, passcode);
                }
            }
        });
        return view;
    }

    private void clearPrevErrors(){
        fullNameTextInput.setError(null);
        userNameTextInput.setError(null);
        emailTextInput.setError(null);
        passcodeTextInput.setError(null);
        confirmPasscodeTextInput.setError(null);
    }

    private boolean validateInputs (String fullName, String userName, String email, String passcode, String confirmPasscode){
        boolean isValid = false;
        if (fullName.isEmpty()){
            clearPrevErrors();
            fullNameTextInput.setError("FullName required");
        }else if (userName.isEmpty()){
            clearPrevErrors();
            userNameTextInput.setError("Username required");
        }else if (email.isEmpty()){
            clearPrevErrors();
            emailTextInput.setError("Email required");
        }else if (passcode.isEmpty()){
            clearPrevErrors();
            passcodeTextInput.setError("Passcode required");
        }else if(confirmPasscode.isEmpty()){
            clearPrevErrors();
            confirmPasscodeTextInput.setError("Confirm Passcode");
        }else if (!passcode.equals(confirmPasscode)){
            clearPrevErrors();
            passcodeTextInput.setError("Doesn't match");
            confirmPasscodeTextInput.setError("Doesn't match");
        }else {
            clearPrevErrors();
            isValid = true;
        }
        return isValid;
    }
}
