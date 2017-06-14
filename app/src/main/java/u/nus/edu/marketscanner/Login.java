package u.nus.edu.marketscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by PANDA on 12/6/2017.
 */

public class Login extends AppCompatActivity {

    private Button signUpBtn = null;
    private Button loginBtn = null;
    private TextView ui_no = null;
    private int no_of_item = 0;
    String cartID;
    String cartItemID;


    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.user);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cartID = extras.getString("cartID");
            cartItemID = extras.getString("cartItemID");
            no_of_item = extras.getInt("no_of_item");
        }
        signUpBtn = (Button)findViewById(R.id.signupBtn);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Sign_Up.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                startActivity(i);
            }
        });
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
