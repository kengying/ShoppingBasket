package u.nus.edu.marketscanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private int no_of_item = 0;
    private TextView ui_no = null;
    public static  int REQUEST_CODE = 100;
    public static  int PERMISSION_REQUEST = 200;
    String cartID = null;
    String cartItemID = null;

    SurfaceView cameraView;
    BarcodeDetector barcode;
    CameraSource cameraSource;
    SurfaceHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            cartID = extras.getString("cartID");
            cartItemID = extras.getString("cartItemID");
            no_of_item = extras.getInt("no_of_item");
        }
        Log.d("BACKBACK", cartID + " " +cartItemID);
        //stops the app if the barcode reading function does not work
        //why did you place this here? it's already at line 60. if you put this here, the app will be crashing
//        if(!barcode.isOperational()){
//            Toast.makeText(getApplicationContext(), "Sorry, Couldn't setup the detector", Toast.LENGTH_LONG).show();
//            this.finish();
//        }

        REQUEST_CODE = 100;
        PERMISSION_REQUEST = 200;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraView = (SurfaceView) findViewById(R.id.cameraView);
        cameraView.setZOrderMediaOverlay(true);
        holder = cameraView.getHolder();
        barcode = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();
        if(!barcode.isOperational()){
            Toast.makeText(getApplicationContext(), "Sorry, Couldn't setup the detector", Toast.LENGTH_LONG).show();
            this.finish();
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }


        cameraSource = new CameraSource.Builder(this, barcode)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1920,1024)
                .build();
        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try{
                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(cameraView.getHolder());
                    }
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }

        });
        barcode.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            int stop = 0;
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes =  detections.getDetectedItems();
                if(barcodes.size() > 0 && stop == 0){

                    stop = 1;
                    final String scanned = barcodes.valueAt(0).displayValue;
                    Log.d("MainActivity", barcodes.valueAt(0).displayValue);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {

                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                            View mView = getLayoutInflater().inflate(R.layout.confirm_popup, null);
                            final ImageView image = (ImageView) mView.findViewById(R.id.image);
                            final TextView mName = (TextView) mView.findViewById(R.id.text);
                            final TextView mPrice = (TextView) mView.findViewById(R.id.price);
                            final Button mAdd = (Button) mView.findViewById(R.id.btnAdd);
                            final Button mCancel = (Button) mView.findViewById(R.id.btnCancel);
                            mAdd.setVisibility(View.GONE);
                            mCancel.setVisibility(View.GONE);
                            mBuilder.setView(mView);
                            /*TODO
                                - SHOW ADD AND CANCEL BTN AFTER DETAILS IS THERE //DONE
                                - IF BARCODE IS NOT VALID ERROR //DONE
                                - CANNOT ADD SAME SCANNED ITEM // A MUST */
                            final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("items");
                            Query query = mDatabase.child(scanned);
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.getValue() != null) {
                                        String imageScanned = dataSnapshot.child("item_Image").getValue(String.class);
                                        String nameScanned = dataSnapshot.child("item_Name").getValue(String.class);
                                        String priceScanned = "$" + dataSnapshot.child("item_Price").getValue(Double.class);
                                        Picasso.with(getApplicationContext())
                                                .load(imageScanned)
                                                .into(image);
                                        mName.setText(nameScanned);
                                        mPrice.setText(priceScanned);
                                        mAdd.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        mName.setText("ITEM IS NOT RECOGNISED!");
                                    }
                                    mCancel.setVisibility(View.VISIBLE);


                                    //Log.d("QUERY", dataSnapshot.child("item_Image").getValue(String.class));
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            final AlertDialog dialog = mBuilder.create();
                            dialog.show();
                            mCancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                    stop = 0;
                                }
                            });
                            mAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    DatabaseReference mCart;
                                    DatabaseReference mCartItem = null;
                                    if(cartID == null && cartItemID == null) {
                                        mCart = FirebaseDatabase.getInstance().getReference("carts").push();
                                        mCartItem = FirebaseDatabase.getInstance().getReference("cart_item").push();
                                        mCartItem.child("item_ID").child("1").setValue(scanned);
                                        mCartItem.child("cart_ID").setValue(mCart.getKey());
                                        mCart.child("cart_Status").setValue("true");
                                        cartID = mCart.getKey();
                                        cartItemID = mCartItem.getKey();
                                        no_of_item = 1;
                                    }
                                    else{
                                        no_of_item = no_of_item + 1;
                                        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("cart_item");
                                        mDatabase.child(cartItemID).child("item_ID").child(no_of_item+"").setValue(scanned);
                                    }
                                    updateItemCount(no_of_item);
                                    dialog.dismiss();
                                    stop = 0;
                                }
                            });
                        }
                    });

                }
            }


        });
    }

//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the usercart_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.usercart_menu, menu);

        final Menu m = menu;
        final MenuItem item = menu.findItem(R.id.cartButton);
        final MenuItem userBtn = menu.findItem(R.id.userButton);
        final View menu_cart = item.getActionView();
        final View menu_User = userBtn.getActionView();
        ui_no = (TextView)  menu_cart.findViewById(R.id.no_of_item);
        updateItemCount(no_of_item);
        menu_cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cameraSource.stop();
                Intent i = new Intent(MainActivity.this, Cart.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                startActivity(i);
            }
        });
        menu_User.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cameraSource.stop();
                Intent i = new Intent(MainActivity.this, Login.class);
                i.putExtra("cartID", cartID);
                i.putExtra("cartItemID", cartItemID);
                i.putExtra("no_of_item", no_of_item);
                startActivity(i);
            }
        });
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        Log.d("CHANGE VIEW", id +"");
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.userButton) {
//            cameraSource.stop();
//            Intent i = new Intent(MainActivity.this, Login.class);
//            i.putExtra("cartID", cartID);
//            i.putExtra("cartItemID", cartItemID);
//            i.putExtra("no_of_item", no_of_item);
//            startActivity(i);
//            // Do something
//            return true;
//        }
//        if (id == R.id.cartButton) {
//            cameraSource.stop();
//            Intent i = new Intent(MainActivity.this, Cart.class);
//            i.putExtra("cartID", cartID);
//            i.putExtra("cartItemID", cartItemID);
//            i.putExtra("no_of_item", no_of_item);
//            startActivity(i);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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


}
