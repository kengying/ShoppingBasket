package u.nus.edu.marketscanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ithsirslawragga on 6/7/17.
 */

public class Sign_Up extends AppCompatActivity {

    private EditText userName, firstName, lastName, password, cfmPassword;
    private Button signUpBtn, loginBtn;
    ProgressDialog progressDialog;
    boolean valid = true;
    private User user;

    DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");

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

                if(valid) {
                    Log.d("adding ", "pending");
                    addUser();

                    Intent i = new Intent(Sign_Up.this, Login.class);
                    startActivity(i);
                    finish();
                }
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

    private void addUser() {
        String SuserName = userName.getText().toString();
        String SfirstName = firstName.getText().toString();
        String SlastName = lastName.getText().toString();
        String Spassword = password.getText().toString();

        String id = databaseReference.push().getKey();
        user = new User(SuserName, Spassword, SlastName, SfirstName, (long) 0);

        databaseReference.child(id).setValue(user);
        Toast.makeText(getApplicationContext(), "Sign up successful", Toast.LENGTH_SHORT).show();
    }

    public boolean validate(final String SuserName, final String SfirstName, final String SlastName, final String Spassword,
                         final String ScfmPassword) {

        if (SuserName.isEmpty() || (SuserName.length() < 3)) {
            Toast.makeText(getApplicationContext(), "Username must be at least 3 characters", Toast.LENGTH_SHORT).show();
            valid = false;
            userName.setError(null);
        } else if (SfirstName.isEmpty() || SlastName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Fill in all fields", Toast.LENGTH_SHORT).show();
            valid = false;
            firstName.setError(null);
            lastName.setError(null);
        } else if (Spassword.isEmpty() || (Spassword.length() < 7)) {
            //ERROR: 7 characters is not allowed, only 8 or more is allowed
            Toast.makeText(getApplicationContext(), "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            valid = false;
            password.setError(null);
            Log.d("password ", "pending");
        } else if (ScfmPassword.isEmpty() || (!Spassword.matches(ScfmPassword))) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            valid = false;
            cfmPassword.setError(null);
            Log.d("cfm password ", "pending");
        }

        //TODO: add if username is already being used - set valid to false

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
