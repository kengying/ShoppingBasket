package u.nus.edu.marketscanner;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Created by PANDA on 31/5/2017.
 */

public class Cart extends AppCompatActivity {
    String cartID = null;
    String cartItemID = null;
    int no_of_item = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            cartID = extras.getString("cartID");
            cartItemID = extras.getString("cartItemID");
            no_of_item = extras.getInt("no_of_item");
        }
        init(cartItemID, no_of_item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the cart_menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.checkout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.checkOutButton) {
            Intent i = new Intent(Cart.this, Check_Out.class);
            i.putExtra("cartID", cartID);
            i.putExtra("cartItemID", cartItemID);
            i.putExtra("no_of_item", no_of_item);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void init(String cartItemID, int no_of_item) {
        if(cartItemID != null) {
            final DatabaseReference itemID = FirebaseDatabase.getInstance().getReference("cart_item");

            TableLayout stk = (TableLayout) findViewById(R.id.table_main);
            TableRow tbrow0 = new TableRow(this);
            TextView tv0 = new TextView(this);
            tv0.setText("      Item      ");
            tv0.setTextColor(Color.BLACK);
            tbrow0.addView(tv0);
            TextView tv1 = new TextView(this);
            tv1.setText("      Price      ");
            tv1.setTextColor(Color.BLACK);
            tbrow0.addView(tv1);
            TextView tv2 = new TextView(this);
            tv2.setText("      ✔      ");
            tv2.setTextColor(Color.BLACK);
            tbrow0.addView(tv2);
            TextView tv3 = new TextView(this);
            tv3.setText("      ✘      ");
            tv3.setTextColor(Color.BLACK);
            tbrow0.addView(tv3);
            stk.addView(tbrow0);

            //this is for getting the details
            for (int i = 1; i <= no_of_item; i++) {
                TableRow tbrow = new TableRow(this);
                final TextView t1v = new TextView(this);
                final TextView t2v = new TextView(this);

                Query queryITEMID = itemID.child(cartItemID).child("item_ID").child(i+"");
                queryITEMID.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String scanned = dataSnapshot.getValue(String.class);

                        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("items");
                        Query query = mDatabase.child(scanned);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String priceScanned = "$" + dataSnapshot.child("item_Price").getValue(Double.class);
                                t1v.setText(dataSnapshot.child("item_Name").getValue(String.class));
                                t2v.setText(priceScanned);

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


                t1v.setTextColor(Color.BLACK);
                t1v.setGravity(Gravity.CENTER);
                tbrow.addView(t1v);

                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);
                TextView t3v = new TextView(this);
                t3v.setText("✔");
                t3v.setTextColor(Color.BLACK);
                t3v.setGravity(Gravity.CENTER);
                tbrow.addView(t3v);
                TextView t4v = new TextView(this);
                t4v.setText("✘");
                t4v.setTextColor(Color.BLACK);
                t4v.setGravity(Gravity.CENTER);
                tbrow.addView(t4v);
                stk.addView(tbrow);
            }
        }
    }

    /* trying to edit */

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent i = new Intent(Cart.this, MainActivity.class);
        i.putExtra("cartID", cartID);
        i.putExtra("cartItemID", cartItemID);
        i.putExtra("no_of_item", no_of_item);
        startActivity(i);
        finish();
    }

}
