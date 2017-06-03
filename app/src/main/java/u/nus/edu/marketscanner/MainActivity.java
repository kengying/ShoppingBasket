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

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private int no_of_item = 9;
    private TextView ui_no = null;
    public static  int REQUEST_CODE = 100;
    public static  int PERMISSION_REQUEST = 200;

    SurfaceView cameraView;
    BarcodeDetector barcode;
    CameraSource cameraSource;
    SurfaceHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            /*TODO: Camera error pressing back*/
            int stop = 0;
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {

                final SparseArray<Barcode> barcodes =  detections.getDetectedItems();
                if(barcodes.size() > 0 && stop == 0){
                    stop = 1;
//                    Intent intent = new Intent();
//                    intent.putExtra("barcode", barcodes.valueAt(0));
//                    final Barcode barcode = barcodes.valueAt(0).displayValue;
//                    Toast.makeText(getApplicationContext(), barcodes.valueAt(0).displayValue, Toast.LENGTH_LONG).show();
                     String value = barcodes.valueAt(0).displayValue;
                    Log.d("MainActivity", barcodes.valueAt(0).displayValue);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                            View mView = getLayoutInflater().inflate(R.layout.confirm_popup, null);
                            ImageView image = (ImageView) mView.findViewById(R.id.image);
                            image.setImageResource(R.drawable.img_crystal);
                            final TextView mEmail = (TextView) mView.findViewById(R.id.text);
                            mEmail.setText(barcodes.valueAt(0).displayValue);
                            Button mLogin = (Button) mView.findViewById(R.id.dialogButtonOK);
                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();
                            dialog.show();
                            mLogin.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
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
        // Inflate the cart_menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart_menu, menu);

        final Menu m = menu;
        final MenuItem item = menu.findItem(R.id.cartButton);
        final View menu_cart = item.getActionView();
        ui_no = (TextView)  menu_cart.findViewById(R.id.no_of_item);
        updateItemCount(no_of_item);
        menu_cart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cameraSource.stop();
                Intent i = new Intent(MainActivity.this, Cart.class);
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


}
