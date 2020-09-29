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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register_user, container, false);
        final FirebaseOperations operations = new FirebaseOperations(getContext(), getActivity().getSupportFragmentManager());

        final TextInputLayout fullNameTextInput, userNameTextInput, emailTextInput, passcodeTextInput, confirmPasscodeTextInput;
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

                operations.registerUserInfo(fullName, userName, email, passcode);
            }
        });
        return view;
    }
}
