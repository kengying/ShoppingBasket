package u.nus.edu.marketscanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PANDA on 1/7/2017.
 */

public class Profile extends AppCompatActivity {

    ListView list_view;
    List<String> list = new ArrayList<String>();
    List<Map<String, String>> itemName;
    SimpleAdapter adapter;

    private TextView greetings = null;
    private TextView ui_no = null;
    private Button logoutBtn = null;

    private int no_of_item = 0;
    private String cartID;
    private String cartItemID;

    private User user = new User();


    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.profile);
        list_view = (ListView) findViewById(R.id.list_view);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        user = (User) intent.getParcelableExtra("user");
        if (extras != null) {
            cartID = extras.getString("cartID");
            cartItemID = extras.getString("cartItemID");
            no_of_item = extras.getInt("no_of_item");
            Log.d("TEST profile", cartID + " " + cartItemID);
        }
        Log.d("TEST profile", cartID + " " + cartItemID);

        greetings = (TextView) findViewById(R.id.greetingTV);
        greetings.setText("Welcome to DA!SO " + user.getFirstName() + " " + user.getLastName());

        logoutBtn = (Button) findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Profile.this.runOnUiThread(new Runnable() {
                    public void run() {

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                        View mView = getLayoutInflater().inflate(R.layout.logout_popup, null);
                        final Button mAdd = (Button) mView.findViewById(R.id.btnAdd);
                        final Button mCancel = (Button) mView.findViewById(R.id.btnCancel);
                        mBuilder.setView(mView);


                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();
                        mCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        mAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(Profile.this, Login.class);
                                i.putExtra("no_of_item", 0);
                                startActivity(i);
                                dialog.dismiss();
                            }
                        });
                    }
                });

            }
        });

        if (user.getNo_of_carts() != 0) {
            final DatabaseReference userDaata = FirebaseDatabase.getInstance().getReference("cart_users").child(user.getUsername());
            itemName = new ArrayList<Map<String, String>>();
            userDaata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterable<DataSnapshot> itemChildren = dataSnapshot.getChildren();
                    for (DataSnapshot cartID : itemChildren) {
                        Log.d("USER", cartID.getKey());


                        Map<String, String> data = new HashMap<String, String>(2);
                        data.put("name", cartID.child("dateTime").getValue(String.class));
                        data.put("price", cartID.child("amount").getValue(String.class));
                        itemName.add(data);

                        // itemName.add(dataSnapshot.child("item_Name").getValue(String.class));
                        // itemName.add(String.valueOf(dataSnapshot.child("item_Price").getValue(Double.class)));

                        adapter = new SimpleAdapter(Profile.this, itemName, android.R.layout.simple_list_item_2,
                                new String[]{"name", "price"}, new int[]{android.R.id.text1, android.R.id.text2});
                        list.add(cartID.getKey());
                        list_view.setAdapter(adapter);
                        registerForContextMenu(list_view);


                    }


                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fullcart_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.show_id:
                Intent i = new Intent(Profile.this, Cart.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                i.putExtra("tmpHistory", list.get(info.position));
                if (!user.getFirstName().equals("") && !user.getFirstName().equals(""))
                    i.putExtra("user", user);
                startActivity(i);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
        ui_no = (TextView) menu_cart.findViewById(R.id.no_of_item);
        updateItemCount(no_of_item);
        menu_cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, Cart.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                if (!user.getFirstName().equals("") && !user.getFirstName().equals(""))
                    i.putExtra("user", user);
                startActivity(i);
            }
        });
        menu_Checkout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, Check_Out.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                if (!user.getFirstName().equals("") && !user.getFirstName().equals(""))
                    i.putExtra("user", user);
                startActivity(i);
            }
        });
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Profile.this, MainActivity.class);
        i.putExtra("cartID", cartID);
        i.putExtra("cartItemID", cartItemID);
        i.putExtra("no_of_item", no_of_item);
        if (!user.getFirstName().equals("") && !user.getFirstName().equals(""))
            i.putExtra("user", user);
        startActivity(i);
        finish();
    }

}
