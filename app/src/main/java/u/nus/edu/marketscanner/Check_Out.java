package u.nus.edu.marketscanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * Created by PANDA on 31/5/2017.
 */

public class Check_Out extends AppCompatActivity {
    private int no_of_item = 9;
    private TextView ui_no = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.cart_menu, menu);

        final Menu m = menu;
        final MenuItem item = menu.findItem(R.id.cartButton);
        final View menu_cart = item.getActionView();
        ui_no = (TextView)  menu_cart.findViewById(R.id.no_of_item);
        updateItemCount(no_of_item);
        menu_cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Check_Out.this, Cart.class);
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
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(Check_Out.this, MainActivity.class));
        finish();
    }
}
