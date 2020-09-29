package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentLoginKeypad extends Fragment {
    private ConstraintLayout errorMessage;
    private ConstraintLayout successMessage;
    private EditText keyOneText, keyTwoText, keyThreeText, keyFourText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login_keypad, container, false);
        Button btnOne, btnTwo, btnThree, btnFour, btnFive, btnSix, btnSeven, btnEight, btnNine, btnHash, btnStar, btnZero, sendBtn;
        TextView createAccountText = view.findViewById(R.id.newAccountText);
        errorMessage = view.findViewById(R.id.errorMessageCard);
        successMessage = view.findViewById(R.id.successMessageCard);
        hideMessage(errorMessage);
        hideMessage(successMessage);
        ImageView errorMessageCloseBtn = errorMessage.findViewById(R.id.errorClose);
        ImageView successMessageCloseBtn = successMessage.findViewById(R.id.successClose);

        //initializing the keypads
        keyOneText = view.findViewById(R.id.keyOneText);
        keyTwoText = view.findViewById(R.id.keyTwoText);
        keyThreeText = view.findViewById(R.id.keyThreeText);
        keyFourText = view.findViewById(R.id.keyFourText);
        keyOneText.requestFocus();

        //disable soft key when keys are focused
        keyOneText.setShowSoftInputOnFocus(false);
        keyTwoText.setShowSoftInputOnFocus(false);
        keyThreeText.setShowSoftInputOnFocus(false);
        keyFourText.setShowSoftInputOnFocus(false);

        //initializing the buttons
        btnOne = view.findViewById(R.id.btnOne);
        btnTwo = view.findViewById(R.id.btnTwo);
        btnThree = view.findViewById(R.id.btnThree);
        btnFour= view.findViewById(R.id.btnFour);
        btnFive = view.findViewById(R.id.btnFive);
        btnSix = view.findViewById(R.id.btnSix);
        btnSeven = view.findViewById(R.id.btnSeven);
        btnEight = view.findViewById(R.id.btnEight);
        btnNine = view.findViewById(R.id.btnNine);
        btnZero = view.findViewById(R.id.btnZero);
        btnHash = view.findViewById(R.id.btnHash);
        btnStar = view.findViewById(R.id.btnStar);

        btnOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnteredNum(1);
            }
        });
        btnTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnteredNum(2);
            }
        });
        btnThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnteredNum(3);
            }
        });
        btnFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnteredNum(4);
            }
        });
        btnFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnteredNum(5);
            }
        });
        btnSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnteredNum(6);
            }
        });
        btnSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnteredNum(7);
            }
        });
        btnEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnteredNum(8);
            }
        });
        btnNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnteredNum(9);
            }
        });
        btnZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnteredNum(0);
            }
        });

        sendBtn = view.findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyOne = keyOneText.getText().toString();
                String keyTwo = keyTwoText.getText().toString();
                String keyThree = keyThreeText.getText().toString();
                String keyFour = keyFourText.getText().toString();

                boolean isValid = checkKeysEntered(keyOne, keyTwo, keyThree, keyFour);
                if(!isValid) showMessage(errorMessage, "Incorrect keys entered!");
                else {
                    showMessage(successMessage, "Login Successful");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getContext(), MainActivity.class));
                        }
                    }, 1500);
                }
            }
        });
        errorMessageCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideMessage(errorMessage);
            }
        });
        successMessageCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideMessage(successMessage);
            }
        });
        return view;
    }

    private void setEnteredNum(Integer num){
        EditText[] keysList = {keyOneText, keyTwoText, keyThreeText, keyFourText};
        for (EditText key: keysList) {
            if (key.hasFocus()){
                key.setText(String.valueOf(num));
                switchKey(key);
                break;
            }
        }
    }

    private void switchKey(EditText key) {
        switch (key.getId()){
            case R.id.keyOneText:
                keyTwoText.requestFocus();
                break;
            case R.id.keyTwoText:
                keyThreeText.requestFocus();
                break;
            case R.id.keyThreeText:
                keyFourText.requestFocus();
                break;
        }
    }

    private boolean checkKeysEntered (String keyOne, String keyTwo, String keyThree, String keyFour){
        boolean isValid = false;
        String keysCombination = keyOne + keyTwo + keyThree + keyFour;
        if (keysCombination.equals("0000")) isValid = true;
        return isValid;
    }

    private void hideMessage(ConstraintLayout messageCard){
        messageCard.animate().translationX(600).alpha(0);
    }

    private void showMessage(ConstraintLayout messageCard, String message){
        TextView errorText = errorMessage.findViewById(R.id.errorText);
        TextView successText = successMessage.findViewById(R.id.successText);
        TextView messageText = messageCard.equals(successMessage) ? successText : errorText;
        messageText.setText(message);
        messageCard.animate().translationX(0).alpha(1).setDuration(300);
    }
}
