package u.nus.edu.marketscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by PANDA on 14/6/2017.
 */

public class Sign_Up extends AppCompatActivity {

    private Button signUpBtn = null;
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.signup);
        super.onCreate(savedInstanceState);
    }
}
