package u.nus.edu.marketscanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by PANDA on 31/5/2017.
 */

public class Cart extends AppCompatActivity {

    ListView list_view;
    List<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;

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
                     startActivity(i);
                     return true;
                 }

                 return super.onOptionsItemSelected(item);
             }

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart);
        list_view = (ListView) findViewById(R.id.list_view);
        list.add("Covfefe");
        list.add("Avocado");
        list.add("Covfefe");
        list.add("Avocado");
        list.add("Covfefe");
        list.add("Avocado");
        adapter = new ArrayAdapter<String> (this, android.R.layout.simple_expandable_list_item_1, list);
        list_view.setAdapter(adapter);
            registerForContextMenu(list_view);
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
        switch(item.getItemId())
        {
            case R.id.delete_id:
                list.remove(info.position);
            adapter.notifyDataSetChanged();
            return true;

        }
        return super.onContextItemSelected(item);
    }

    @Override
     public void onBackPressed()
     {
                super.onBackPressed();
                startActivity(new Intent(Cart.this, MainActivity.class));
                finish();
            }


}