package u.nus.edu.marketscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by PANDA on 12/6/2017.
 */

public class Login extends AppCompatActivity {

    private Button signUpBtn = null;
    private Button loginBtn = null;
    private TextView ui_no = null;
    private int no_of_item = 0;
    private EditText mUsername = null;
    private EditText mPassword = null;

    private String username = null;
    private String password = null;
    private String cartID;
    private String cartItemID;


    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cartID = extras.getString("cartID");
            cartItemID = extras.getString("cartItemID");
            no_of_item = extras.getInt("no_of_item");
            Log.d("TEST login", cartID + " " + cartItemID);
        }

        if(user != null){
            Intent i = new Intent(Login.this, Profile.class);
            i.putExtra("cartID", cartID);
            i.putExtra("cartItemID", cartItemID);
            i.putExtra("no_of_item", no_of_item);
            i.putExtra("user", user);
            Log.d("TEST login", cartID + " " + cartItemID);
            startActivity(i);
        } else{
        setContentView(R.layout.login);

        signUpBtn = (Button)findViewById(R.id.signupBtn);
        loginBtn = (Button)findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mUsername = (EditText)findViewById(R.id.usernameET);
                username = mUsername.getText().toString();
                mPassword = (EditText)findViewById(R.id.passwordET);
                password = mPassword.getText().toString();

                Query query = FirebaseDatabase.getInstance().getReference("users").child(username);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null) {
                            User user = dataSnapshot.getValue(User.class);
                            Log.d("QUERY for user", user.toString());
                            if(username.equals(user.getUsername()) && password.equals(user.getPassword())){
                                Intent i = new Intent(Login.this, MainActivity.class);
                                i.putExtra("cartID", cartID);
                                i.putExtra("cartItemID", cartItemID);
                                i.putExtra("no_of_item", no_of_item);
                                i.putExtra("user", user);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "Password is wrong", Toast.LENGTH_LONG).show();
                            }

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Username is not in database.", Toast.LENGTH_LONG).show();
                        }

                        mUsername.setText("");
                        mPassword.setText("");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Sign_Up.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                startActivity(i);
            }
        });
        }
        super.onCreate(savedInstanceState);
    }


    public void updateItemCount(final int new_hot_number) {
        no_of_item = new_hot_number;
        if (ui_no == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_hot_number == 0)
                    ui_no.setVisibility(View.INVISIBLE);
                else {
                    ui_no.setVisibility(View.VISIBLE);
                    ui_no.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the usercart_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checkoutcart_menu, menu);

        final Menu m = menu;
        final MenuItem item = menu.findItem(R.id.cartButton);
        final MenuItem checkOutBtn = menu.findItem(R.id.checkOutButton);
        final View menu_cart = item.getActionView();
        final View menu_Checkout = checkOutBtn.getActionView();
        ui_no = (TextView)  menu_cart.findViewById(R.id.no_of_item);
        updateItemCount(no_of_item);
        menu_cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Cart.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                Log.d("TEST login", cartID + " " + cartItemID);
                startActivity(i);
            }
        });
        menu_Checkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Check_Out.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                Log.d("TEST login", cartID + " " + cartItemID);
                startActivity(i);
            }
        });
        return true;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Login.this, MainActivity.class);
        i.putExtra("cartID", cartID);
        i.putExtra("cartItemID", cartItemID);
        i.putExtra("no_of_item", no_of_item);
        startActivity(i);
        finish();
    }
}
