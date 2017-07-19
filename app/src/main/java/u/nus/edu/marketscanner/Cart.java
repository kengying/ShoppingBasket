package u.nus.edu.marketscanner;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.vision.CameraSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by PANDA on 31/5/2017.
 */

public class Cart extends AppCompatActivity {

    ListView list_view;
    List<String> list = new ArrayList<String>();
    List<Map<String, String>> itemName;
    List<Item> rowItems = new ArrayList<Item>();

    private int no_of_item = 0;
    String cartID = "";
    String cartItemID = "";
    DatabaseReference itemID;
    ItemAdapter adapter;
    TextView header;
    private double totalCost = 0;
    private User user = new User();
    private String tmpHistory = "";
    private String tmpCartItemID = "";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the usercart_menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.usercheckout_menu, menu);
        header = (TextView) findViewById(R.id.header);
        final MenuItem checkOutBtn = menu.findItem(R.id.checkOutButton);
        final MenuItem userBtn = menu.findItem(R.id.userButton);
        final View menu_User = userBtn.getActionView();
        final View menu_CheckOut = checkOutBtn.getActionView();
        menu_User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tmpHistory != null && tmpHistory != "") {
                    cartItemID = tmpCartItemID;
                    tmpCartItemID = null;
                }
                Log.d("TEST cart", cartID + " " + cartItemID);
                Intent i = new Intent(Cart.this, Login.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                if (user != null)
                    i.putExtra("user", user);
                startActivity(i);
            }
        });
        menu_CheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tmpHistory != null && tmpHistory != "") {
                    cartItemID = tmpCartItemID;
                    tmpCartItemID = null;
                }
                Log.d("TEST cart", cartID + " " + cartItemID);
                Intent i = new Intent(Cart.this, Check_Out.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                if (user != null)
                    i.putExtra("user", user);
                startActivity(i);
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.checkOutButton) {
//            Intent i = new Intent(Cart.this, Check_Out.class);
//            i.putExtra("cartID", cartID);
//            i.putExtra("cartItemID", cartItemID);
//            i.putExtra("no_of_item", no_of_item);
//            startActivity(i);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cartID = extras.getString("cartID");
            cartItemID = extras.getString("cartItemID");
            no_of_item = extras.getInt("no_of_item");
            tmpHistory = extras.getString("tmpHistory");


        }

        if (tmpHistory != null && tmpHistory != "" ) {
            tmpCartItemID = cartItemID;
            cartItemID = tmpHistory;

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        list_view = (ListView) findViewById(R.id.main_list_view);

        Log.d("TEST cart", cartID + " " + cartItemID + " " + tmpHistory);
        // final ArrayList<String> itemName = new ArrayList<String>();
        itemName = new ArrayList<Map<String, String>>();

        if (cartItemID == null) {

            Toast.makeText(getApplicationContext(), "Cart is empty", Toast.LENGTH_SHORT).show();

        } else {
            itemID = FirebaseDatabase.getInstance().getReference("cart_item").child(cartItemID).child("item_ID");
            itemID.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Log.d("SOMETHIING ", dataSnapshot.getChildrenCount() + "");
                    Iterable<DataSnapshot> itemChildren = dataSnapshot.getChildren();
                    for (DataSnapshot itemID : itemChildren) {
                        Log.d("SOMETHIING ", itemID.getKey());
                        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("items");
                        Query query = mDatabase.child(itemID.getKey());
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                Map<String, String> data = new HashMap<String, String>(3);
                                data.put("name", dataSnapshot.child("item_Name").getValue(String.class));
                                data.put("price", "$" + String.valueOf(dataSnapshot.child("item_Price").getValue(Double.class)));
                                data.put("image", String.valueOf(dataSnapshot.child("item_Image").getValue(String.class)));
                                itemName.add(data);

                                Item cartItem = dataSnapshot.getValue(Item.class);
                                Log.d("QUERY", cartItem.toString());
                                rowItems.add(cartItem);
                                // itemName.add(dataSnapshot.child("item_Name").getValue(String.class));
                                // itemName.add(String.valueOf(dataSnapshot.child("item_Price").getValue(Double.class)));

                                totalCost += dataSnapshot.child("item_Price").getValue(Double.class);

                                Log.d("QUERY", dataSnapshot.child("item_Name").getValue(String.class));
                                Log.d("QUERY", "$" + String.valueOf(dataSnapshot.child("item_Price").getValue(Double.class)));
                                adapter = new ItemAdapter(getApplicationContext(), R.layout.row_layout, rowItems);
                                header.setText("Total Cost: $" + String.valueOf(totalCost));
                                // adapter = new ItemAdapter(getApplicationContext(),R.layout.row_layout);
                                list.add(dataSnapshot.child("item_Id").getValue(Long.class) + "");

                                list_view.setAdapter(adapter);
                                if(tmpHistory == null)
                                registerForContextMenu(list_view);
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
        }


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_id:
                Log.d("Cart: ", cartItemID);
                Log.d("Cart: ", info.position + "");
                Item price = rowItems.get(info.position);
                totalCost = totalCost - price.getItem_Price();
                header.setText("Total Cost: $" + String.valueOf(totalCost));
                itemID.child(list.get(info.position)).removeValue();
                list.remove(info.position);
                no_of_item = no_of_item - 1;
                adapter.remove(rowItems, info.position);
//                rowItems.remove(info.position);
//                itemName.remove(adapter.getItem(info.position));
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("TEST cart", cartID + " " + cartItemID);
        if (tmpHistory != null && tmpHistory != "") {
            cartItemID = tmpCartItemID;
            tmpCartItemID = null;
        }
        Intent i = new Intent(Cart.this, MainActivity.class);
        i.putExtra("cartID", cartID);
        i.putExtra("cartItemID", cartItemID);
        i.putExtra("no_of_item", no_of_item);
        if (user != null)
            i.putExtra("user", user);
        startActivity(i);
        finish();
    }


}