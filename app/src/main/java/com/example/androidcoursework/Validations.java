package com.example.androidcoursework;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.util.Map;
import java.util.regex.Pattern;


public class Validations {
    Boolean valid;
    public boolean setSpinnerError(Spinner spinner){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null   && selectedView instanceof TextView) {spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;

            if(((TextView) selectedView).getText().equals("Select the Category")){
                valid = false ;
                selectedTextView.setError("Selected valid Details !");
            }
            else {
                valid = true;
            }
        }
        else {
            valid = false;
        }
        return valid;
    }
    public boolean aliphaticValidation (EditText textField){

        if(textField.getText().toString().isEmpty()){
            textField.setError("Enter valid Details !");
            valid = false;
        }else {
            valid = true;
        }

        return valid;
    }
    public boolean isValidEmail(EditText textField){

        if(!textField.getText().toString().isEmpty() && Patterns.EMAIL_ADDRESS.matcher(textField.getText()).matches()){
            valid = true;
        }else {
            textField.setError("Enter valid Email address !");
            valid = false;
        }

        return valid;
    }
    public boolean isValidPassword(EditText textField){
        final Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        //"(?=.*[0-9])" +         //at least 1 digit
                        //"(?=.*[a-z])" +         //at least 1 lower case letter
                        //"(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=.*[a-zA-Z])" +      //any letter
                        "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$");
        if(!textField.getText().toString().isEmpty() && PASSWORD_PATTERN.matcher(textField.getText().toString()).matches()){
            valid = true;
        }else {
            textField.setError("At leaste 1 upercase, 1 special character , minum 4 characters");

            valid = false;

        }
        return valid;
    }
   public boolean isValidImage(Uri uri, TextView textView){
       if(uri == null){
           textView.setError("Image Not Selected!");
           textView.setVisibility(View.VISIBLE);
           valid = false;
       }else {
           valid = true;
       }

       return valid;
   }




}
