package app.msdeep14.tripcount;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddPlaceDetails extends AppCompatActivity {

    private static Button btn;
    private static Button b1;
    private static Button btnadd;
    public DatePickerDialog datepick = null;
    SQLiteDatabase db1=null;
    EditText e1,e2;
    Editable d1,d2=null;
    TextView edtDob=null;
    TextView edtxt;
    EditText f_add;
    EditText num;
    int check=0;
    ArrayList<String> fname= new ArrayList<String>();
    int size=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn = (Button) findViewById(R.id.button_SUBMIT);
        edtxt=(TextView) findViewById(R.id.daye);
        btnadd=(Button)findViewById(R.id.add_btn);
        b1= (Button) findViewById(R.id.daypickbut);
        f_add=(EditText)findViewById(R.id.friend_name);
        num=(EditText)findViewById(R.id.editno);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        OnClickpickDate();
        OnClickButtonSubmit();
        setTitle("New Trip");
        OnClickButtonAddFriend();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(AddPlaceDetails.this, AddPlace.class));
        finish();
    }

    public void OnClickpickDate(){
        try{
            b1.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){
                    datepick = new DatePickerDialog(v.getContext(), (DatePickerDialog.OnDateSetListener) new DatePickHandler(), Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                    datepick.show();
                }
            });
        }
        catch(Exception e){
            Toast.makeText(getApplicationContext(), "Invalid Date", Toast.LENGTH_SHORT).show();
        }
    }

    public class DatePickHandler implements DatePickerDialog.OnDateSetListener {
        public void onDateSet(DatePicker view, int year, int month, int day) {
            int months = month+1;
            if((months<10)&&(day<10))
                edtxt.setText(year + "-0" + (months) + "-0" + day);
            else if((months<10)&&(day>10))
                edtxt.setText(year + "-0" + (months) + "-" + day);
            else if((months>10)&&(day<10))
                edtxt.setText(year + "-" + (months) + "-0" + day);
            else
                edtxt.setText(year + "-" + (months) + "-" + day);
            datepick.hide();
        }
    }

    void OnClickButtonSubmit(){
        e1=(EditText)findViewById(R.id.Textplace);
        e2=(EditText)findViewById(R.id.editno);
        edtDob=(TextView) findViewById(R.id.daye);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db1 = openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);
                        d1 = e1.getText();
                        d2 = e2.getText();
                        String dd = edtDob.getText().toString();
                        String s1 = d1.toString().toLowerCase();
                        String s2 = d2.toString();
                        s1=s1.trim();
                        s2=s2.trim();

                        try {
                            db1.execSQL("CREATE TABLE IF NOT EXISTS Trip_details (id INTEGER PRIMARY KEY AUTOINCREMENT ,place_name TEXT NOT NULL, date_go DATE NOT NULL," + " friend_no INTEGER NOT NULL DEFAULT 0);");
                            String COL_2 = "place_name";
                            String COL_3 = "date_go";
                            String COL_4="friend_no";
                            String table = "Trip_details";
                            //check if any of the fields is not empty or friend no is not equal to zero
                            if (s1.matches("") || s2.matches("") || dd.matches("") || Integer.parseInt(s2)==0)
                                throw new ArithmeticException("Inadequate details..\nEnter Again");
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(COL_2, d1.toString().toLowerCase());
                            contentValues.put(COL_2, d1.toString().toLowerCase());
                            contentValues.put(COL_3, dd);
                            contentValues.put(COL_4,Integer.parseInt(num.getText().toString()));
                            int x=Integer.parseInt(num.getText().toString());

                            try {
                                Cursor c = db1.rawQuery("SELECT * FROM Trip_details ORDER BY date_go DESC;", null);
                                if (c != null) {
                                    int i = 0;
                                    if (c.moveToFirst()) {
                                        do {
                                            String compare = c.getString(c.getColumnIndex("place_name"));
                                            if (compare.matches(e1.getText().toString().toLowerCase()))
                                                throw new ArithmeticException("HELLO");

                                        } while (c.moveToNext());
                                    }
                                }

                                //EXCEPTION
                                try {
                                    if (check == Integer.parseInt(num.getText().toString())) {
                                        long result = db1.insert(table, null, contentValues);




                                        if (result != -1) {
                                            Toast.makeText(getApplicationContext(), "Trip Added", Toast.LENGTH_SHORT).show();
                                            Intent lis = new Intent(getApplicationContext(), AddPlace.class);
                                            startActivity(lis);
                                            finish();
                                        } else
                                            throw new ArithmeticException("Inadequate details..\nEnter Again");

                                    } else {
                                        Toast.makeText(getApplicationContext(), "ADD MORE FRIENDS", Toast.LENGTH_SHORT).show();

                                    }

                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Inadequate details..\n" +
                                            "Enter Again", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Trip name already added", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Inadequate details..\n" +
                                    "Enter Again", Toast.LENGTH_LONG).show();
                        }
                    }

                }
        );
    }

    public void OnClickButtonAddFriend(){
        btnadd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String z = num.getText().toString().trim();
                            if (z.matches(""))
                                throw new ArithmeticException("hello");

                            int x = Integer.parseInt(num.getText().toString());

                            if (check < x) {
                                db1 = openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);
                                String TABLE_NAME;
                                e1 = (EditText) findViewById(R.id.Textplace);
                                TABLE_NAME = "f" + e1.getText().toString().toLowerCase();
                                try {
                                    Cursor c = db1.rawQuery("SELECT * FROM Trip_details ORDER BY date_go DESC;", null);
                                    if (c != null) {
                                        int i = 0;
                                        if (c.moveToFirst()) {
                                            do {
                                                String compare = c.getString(c.getColumnIndex("place_name"));
                                                if (compare.matches(e1.getText().toString().toLowerCase()))
                                                    throw new ArithmeticException("HELLO");

                                            } while (c.moveToNext());
                                        }
                                    }
                                    db1.execSQL("create table if not exists " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,friend TEXT,amount INTEGER NOT NULL DEFAULT 0)");
                                    ContentValues contentValues = new ContentValues();
                                    String COL_2;
                                    COL_2 = "friend";
                                    //exception
                                    try {
                                        String y = f_add.getText().toString();
                                        y=y.toLowerCase();
                                        int flag1=0;
                                        if (y.matches(""))
                                            throw new ArithmeticException("Inadequate details..\nEnter Again");

                                        //check if friend name is alrealy included in the list
                                        for(int i=0;i<fname.size();i++){
                                            if(y.matches(fname.get(i))){
                                                Toast.makeText(getApplicationContext(),y+" already included",Toast.LENGTH_SHORT).show();
                                                flag1=1;
                                                break;
                                            }
                                        }
                                        if(flag1==0) {
                                            contentValues.put(COL_2, y);
                                            fname.add(y);
                                            size++;
                                            long result = db1.insert(TABLE_NAME, null, contentValues);
                                            check++;
                                        }
                                        f_add.setText("");
                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "enter friend name", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), "Trip already added", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "friend limit exceeded", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(), "Inadequate details..\n" +
                                    "Enter Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
