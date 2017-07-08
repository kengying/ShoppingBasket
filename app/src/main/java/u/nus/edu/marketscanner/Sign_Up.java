package u.nus.edu.marketscanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ithsirslawragga on 6/7/17.
 */

public class Sign_Up extends AppCompatActivity {

    private EditText userName, firstName, lastName, password, cfmPassword;
    private Button signUpBtn, loginBtn;
    ProgressDialog progressDialog;
    boolean valid = true;
    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        userName = (EditText) findViewById(R.id.usernameET);
        firstName = (EditText) findViewById(R.id.firstNameET);
        lastName = (EditText) findViewById(R.id.lastNameET);
        password = (EditText) findViewById(R.id.passwordET);
        cfmPassword = (EditText) findViewById(R.id.cfmPWET);

        signUpBtn = (Button) findViewById(R.id.signupBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valid = validate(userName.getText().toString(), firstName.getText().toString(), lastName.getText().toString(),
                        password.getText().toString(), cfmPassword.getText().toString());

                //TODO: add ability to add user to Firebase
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Sign_Up.this, Login.class);
                startActivity(i);
                finish();
            }
        });
    }

    public boolean validate(final String SuserName, final String SfirstName, final String SlastName, final String Spassword,
                         final String ScfmPassword) {

        if (SuserName.isEmpty() || (SuserName.length() < 3)) {
            Toast.makeText(getApplicationContext(), "Username must be at least 3 characters", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (SfirstName.isEmpty() || SlastName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill in all fields", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (Spassword.isEmpty() || (Spassword.length() < 7)) {
            //ERROR: 7 characters is not allowed, only 8 or more is allowed
            Toast.makeText(getApplicationContext(), "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            valid = false;
        } else if (ScfmPassword.isEmpty() || (!Spassword.matches(ScfmPassword))) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        //TODO: add if username is already being used - set valid to false

            userName.setError(null);
            firstName.setError(null);
            lastName.setError(null);
            password.setError(null);
            cfmPassword.setError(null);

        return valid;
    }
/*
    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }



        signupBtn.setEnabled(false);

        progressDialog = new ProgressDialog(Sign_Up.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();


        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);


    public void onSignupSuccess() {
        signupBtn.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }
    */

}
