package u.nus.edu.marketscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by PANDA on 31/5/2017.
 */

public class Check_Out extends AppCompatActivity {
    private int no_of_item = 0;
    private TextView ui_no = null;
    private ListView total = null;
    String cartID;
    String cartItemID;
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final ArrayList<Double> price = new ArrayList<Double>();
        final ArrayList<String> totalPrice = new ArrayList<String>();
        adapter = new ArrayAdapter(this, R.layout.listview_checkout, totalPrice);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out);
        total = (ListView) findViewById(R.id.textView2);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cartID = extras.getString("cartID");
            cartItemID = extras.getString("cartItemID");
            no_of_item = extras.getInt("no_of_item");
            Log.d("QUERY", cartID + " " + cartItemID + " " + no_of_item);

        }
        if (cartItemID != null) {
            final DatabaseReference itemID = FirebaseDatabase.getInstance().getReference("cart_item").child(cartItemID).child("item_ID");
            Button mConfirm = (Button) findViewById(R.id.button2);
            mConfirm.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click

                    DatabaseReference mCart = FirebaseDatabase.getInstance().getReference("carts");
                    mCart.child(cartID).child("cart_Status").setValue("false");

                    itemID.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Iterable<DataSnapshot> itemChildren = dataSnapshot.getChildren();
                            for (DataSnapshot itemID : itemChildren) {
                                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("items");
                                mDatabase.child(itemID.getKey()).child("item_Status").setValue("false");
                                Toast.makeText(getBaseContext(), "thank you!", Toast.LENGTH_SHORT).show();
                                no_of_item = 0;
                                updateItemCount(0);


                            }
                            cartItemID = "";
                            cartID ="";
                            total.setAdapter(null);
                            totalPrice.clear();
                            totalPrice.add("$0.0");
                            total.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    for (int i = 1; i <= no_of_item; i++) {
//                        Query queryITEMID = itemID.child(cartItemID).child("item_ID").child(i + "");
//                        queryITEMID.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                final String scanned = dataSnapshot.getValue(String.class);
//                                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("items");
//                                mDatabase.child(scanned).child("item_Status").setValue("false");
//                                Toast.makeText(getBaseContext(), "thank you!", Toast.LENGTH_SHORT).show();
//                                no_of_item = 0;
//                                updateItemCount(0);
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//                    }

                }
            });

            total.setAdapter(adapter);
            itemID.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Iterable<DataSnapshot> itemChildren = dataSnapshot.getChildren();
                    for (DataSnapshot itemID : itemChildren) {
                        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("items");
                        Query query = mDatabase.child(itemID.getKey());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                price.add(dataSnapshot.child("item_Price").getValue(Double.class));
                                Log.d("Price", dataSnapshot.child("item_Price").getValue(Double.class).toString());
                                if (price.size() == no_of_item) {
                                    Double compute = 0.0;
                                    for (Double elem : price) {
                                        compute = elem + compute;
                                        Log.d("Price", elem + " TOTAL");
                                    }
                                    totalPrice.add("$" + compute.toString());
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
//            for (int i = 1; i <= no_of_item; i++) {
//                Query queryITEMID = itemID.child(cartItemID).child("item_ID").child(i + "");
//                queryITEMID.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        final String scanned = dataSnapshot.getValue(String.class);
//
//                        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("items");
//                        Query query = mDatabase.child(scanned);
//                        query.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                price.add(dataSnapshot.child("item_Price").getValue(Double.class));
//                                Log.d("Price", dataSnapshot.child("item_Price").getValue(Double.class).toString());
//
//                                if (price.size() == no_of_item) {
//                                    Double compute = 0.0;
//                                    for (Double elem : price) {
//                                        compute = elem + compute;
//                                        Log.d("Price", elem + " TOTAL");
//                                    }
//                                    totalPrice.add(compute);
//                                    adapter.notifyDataSetChanged();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }


            //Log.d("Price",  " HELLO");
            //Log.d("Price", "WHY " + getPrice(no_of_item, cartItemID));
        }
    }
    public Double getPrice(int no_of_item, String cartItemID) {
        final ArrayList<Double> price = new ArrayList<Double>();
        final DatabaseReference itemID = FirebaseDatabase.getInstance().getReference("cart_item");
        for (int i = 1; i <= no_of_item; i++) {
            Query queryITEMID = itemID.child(cartItemID).child("item_ID").child(i + "");
            queryITEMID.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String scanned = dataSnapshot.getValue(String.class);

                    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("items");
                    Query query = mDatabase.child(scanned);
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            price.add(dataSnapshot.child("item_Price").getValue(Double.class));
                            Log.d("Price", dataSnapshot.child("item_Price").getValue(Double.class).toString());

                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        Double total = 0.0;

        return total;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.usercart_menu, menu);

        final Menu m = menu;
        final MenuItem item = menu.findItem(R.id.cartButton);
        final MenuItem userBtn = menu.findItem(R.id.userButton);
        final View menu_cart = item.getActionView();
        final View menu_User = userBtn.getActionView();
        ui_no = (TextView) menu_cart.findViewById(R.id.no_of_item);
        updateItemCount(no_of_item);
        menu_cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Check_Out.this, Cart.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                startActivity(i);
            }
        });
        menu_User.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Check_Out.this, Login.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                startActivity(i);
            }
        });
        return true;
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Check_Out.this, MainActivity.class);
        i.putExtra("cartID", cartID);
        i.putExtra("cartItemID", cartItemID);
        i.putExtra("no_of_item", no_of_item);
        startActivity(i);
        finish();
    }
}
