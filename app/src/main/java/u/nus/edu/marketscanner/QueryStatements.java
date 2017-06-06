package u.nus.edu.marketscanner;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by PANDA on 6/6/2017.
 */

public class QueryStatements {

    public ArrayList<String> getCartItem(String cartItemID, int no_of_item) {
        final ArrayList<String> itemName = new ArrayList<String>();
        final DatabaseReference itemID = FirebaseDatabase.getInstance().getReference("cart_item");
        for (int i = 1; i <= no_of_item; i++) {
            Query queryITEMID = itemID.child(cartItemID).child("item_ID").child(i + "");
            queryITEMID.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final String scanned = dataSnapshot.getValue(String.class);

                    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("items");
                    Query query = mDatabase.child(scanned);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            itemName.add(dataSnapshot.child("item_Name").getValue(String.class));
                            Log.d("QUERY", dataSnapshot.child("item_Name").getValue(String.class));
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
        return itemName;
    }
}