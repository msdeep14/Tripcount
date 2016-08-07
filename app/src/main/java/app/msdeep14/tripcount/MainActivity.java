package app.msdeep14.tripcount;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
* mainproject class
* */
public class MainActivity extends AppCompatActivity {

    FloatingActionButton ab;
    Button btnt;
    Button btnd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ab=(FloatingActionButton)findViewById(R.id.about);
        btnd=(Button)findViewById(R.id.btnday);
        btnt=(Button)findViewById(R.id.btntrip);
        setTitle("Tripcount");
        OnClickButtonListener();
        OnButtonTransClicked();
        OnaboutClicked();

    }
    //dialog box to display aboutus
    public void OnaboutClicked(){
        ab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                        adb.setMessage("developed by:\n\nmandeep singh\npawan sheoran\n\ncontact: mandeepsinghshekhawat95@gmail.com\npsheoran231@gmail.com\n\nTripcount is " +
                                "a app for managing your expenses while you are on group trips or sharing money between friends in day to day life." +
                                "\n\nThe app this divided into two modules:\n\n1.Trips\nIn this option you can add no. of friends while on trips and " +
                                "you just need the update the money paid by any friend and the app will automatically calculate who owes how much money." +
                                "\n\n2.Day to Day expenses\nIn this division you can add friends and amount paid or taken by them so that you can keep t" +
                                "rack of all your friends separately.\n\nDisclaimer:\nThanks to stackoverflow.com for sorting out my various errors\n\n" +
                                "android_guides\ndistributed by CodePath" +
                                " under The MIT License (MIT)");
                        adb.setTitle("Tripcount");
                        adb.show();
                    }
                }
        );
    }

    //activity to add new trip
    public void OnClickButtonListener(){
        btnt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent= new Intent(MainActivity.this,AddPlace.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    //function call for transactions with friends
    public void OnButtonTransClicked(){
        btnd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(),Display_friend.class);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    //when back pressed to confirm whether accidently pressed or not
    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            System.exit(0);
        } else {
            Toast.makeText(this, "Press Back again to Exit",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
