package app.msdeep14.tripcount;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Display_friendDetails extends AppCompatActivity {

    String p;
    SQLiteDatabase db2=null;
    FloatingActionButton f;
    ArrayList<String> notes = new ArrayList<String>();
    ArrayList<String> list_item = new ArrayList<>();
    Vector<Integer> vector = new Vector<>();
    Vector<Integer> vec = new Vector<>();
    int count=0;
    friendDetailAdapter adapter;
    List<DataNoteProvider> niitemlist;
    ArrayAdapter adapter1;
    ListView listView;
    ListView listView1;
    RadioButton rg,rt;
    int y=0;
    int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_friend_details);

        Intent in=getIntent();
        Bundle bundle=in.getExtras();
        p=bundle.getString("id");
        setTitle(p);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.list_note);
        listView1=(ListView)findViewById(R.id.list_note);
        db2 = openOrCreateDatabase("trans.db", Context.MODE_PRIVATE, null);
        rg=(RadioButton)findViewById(R.id.given);
        rt=(RadioButton)findViewById(R.id.taken);

        niitemlist=new ArrayList<>();
        fillActivity();
        f=(FloatingActionButton)findViewById(R.id.myFAB);
        f.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        //alert dialog box
                        LayoutInflater factory = LayoutInflater.from(Display_friendDetails.this);
                        final View textEntryView = factory.inflate(R.layout.alert_addtrans, null);
//text_entry is an Layout XML file containing two text field to display in alert dialog
                        final EditText input1 = (EditText) textEntryView.findViewById(R.id.rdescription);
                        final EditText input2 = (EditText) textEntryView.findViewById(R.id.ramount);
                        //  input1.setText("Enter note", TextView.BufferType.EDITABLE);
                        //  input2.setText("Enter amount", TextView.BufferType.EDITABLE);
                        final AlertDialog.Builder alert = new AlertDialog.Builder(Display_friendDetails.this);

                        alert.setIcon(R.drawable.image)
                                .setTitle("Enter Details")
                                .setView(textEntryView)
                                .setPositiveButton("Save",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                Log.i("AlertDialog", "TextEntry 1 Entered " + input1.getText().toString());
                                                Log.i("AlertDialog", "TextEntry 2 Entered " + input2.getText().toString());
                    /* User clicked OK so do some stuff */
                                                //store the values inside the friend table

                                                int z=0;
                                                if (y == 0 || input1.getText().toString().matches("") || input2.getText().toString().matches("")) {
                                                    Log.e("111111111111","1111111111");
                                                    Toast.makeText(getApplicationContext(), "Inadequate details", Toast.LENGTH_SHORT).show();}
                                                else  z=Integer.parseInt(input2.getText().toString());
                                                if(y==-1) z=(-1)*z;
                                                flag=0;
                                                if (y == 0 || input1.getText().toString().matches("") || input2.getText().toString().matches("")) {
                                                    Log.e("22222222222","2222222222");
                                                    Toast.makeText(getApplicationContext(), "Inadequate details", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    //for example breakfast is already present as a note
                                                    //and if a new note note named breakfast is added again
                                                    //then update the previous entry of breakfast;;
                                                    Cursor c2 = db2.rawQuery("SELECT * FROM " + p + ";", null);
                                                    if (c2 != null) {
                                                        if (c2.moveToFirst()) {
                                                            do {
                                                                String x = c2.getString(c2.getColumnIndex("note"));
                                                                if (x.matches(input1.getText().toString())) {
                                                                    int a = c2.getInt(c2.getColumnIndex("amount"));
                                                                    //int l=Integer.parseInt(input2.getText().toString());
                                                                    a += z;
                                                                    db2.execSQL("UPDATE " + p + " set amount = '" + a + "' WHERE note = '" + x + "';");
                                                                    flag = 1;
                                                                }
                                                            } while (c2.moveToNext());
                                                        }
                                                    }

                                                    if (flag == 0) {
                                                        // db2.execSQL("CREATE TABLE IF NOT EXISTS '"+p+"'(ID INTEGER PRIMARY KEY AUTOINCREMENT ,note TEXT NOT NULL,amount INTEGER NOT NULL DEFAULT 0);");
                                                        db2.execSQL("create table if not exists " + p + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,note TEXT,amount INTEGER NOT NULL DEFAULT 0)");
                                                        String COL_2 = "note";
                                                        String COL_3 = "amount";
                                                        String table = p;
                                                        ContentValues contentValues = new ContentValues();
                                                        contentValues.put(COL_2, input1.getText().toString());
                                                        contentValues.put(COL_3, z);
                                                        long result = db2.insert(p, null, contentValues);
                                                        if (result != -1) {
                                                            // Toast.makeText(getApplicationContext(), "note added", Toast.LENGTH_SHORT).show();
                                                            //fillActivity();
                                                            DataNoteProvider dataProvider = new DataNoteProvider(z, input1.getText().toString());
                                                            adapter.add(dataProvider);

                                                        } else
                                                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                                                    }
                                                    Cursor c3 = db2.rawQuery("SELECT * FROM friends;", null);
                                                    if (c3 != null) {
                                                        if (c3.moveToFirst()) {
                                                            do {
                                                                String t = c3.getString(c3.getColumnIndex("friend"));
                                                                if (t.matches(p)) {
                                                                    int a = c3.getInt(c3.getColumnIndex("amount"));
                                                                    a += z;
                                                                    db2.execSQL("UPDATE friends set amount = '" + a + "' WHERE friend = '" + p + "';");
                                                                }
                                                            } while (c3.moveToNext());
                                                        }
                                                    }

                                                }
                                                y=0;
                                                fillActivity();
                                                // clearlistview();
                                                //end of code added new
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int whichButton) {
                                            }
                                        });
                        alert.show();

                    }
                }
        );



    }
    public void clearlistview()
    {
        notes.clear();
        vector.clear();
        listView.setAdapter(null);
//        adapter.notifyDataSetChanged();
        fillActivity();
    }

    public int OnRadioButtonClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        //check if radio button is checked
        switch (view.getId()){
            case R.id.given:
                if (checked)
                    y=1;
                break;
            case R.id.taken:
                if (checked)
                    y=-1;
                break;
        }

        return y;
    }

    public void fillActivity(){
        niitemlist.clear();
        //listView.setAdapter(null);
        adapter = new friendDetailAdapter(getApplicationContext(),R.layout.note_row_layout,niitemlist);

        listView.setAdapter(adapter);

        db2.execSQL("CREATE TABLE IF NOT EXISTS '"+p+"' (ID INTEGER PRIMARY KEY AUTOINCREMENT ,note TEXT NOT NULL,amount INTEGER NOT NULL DEFAULT 0);");
        db2.execSQL("create table if not exists " + p+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,note TEXT,amount INTEGER NOT NULL DEFAULT 0)");
        //display values from table into list;
        //Cursor c = db2.rawQuery("SELECT * FROM '"+p+"';", null);
        Cursor c=db2.rawQuery("SELECT * FROM "+p+";",null);
        if (c != null) {
            int i = 0;
            if (c.moveToFirst()) {
                do {
                    String d2 = c.getString(c.getColumnIndex("note"));
                    int d4 = Integer.parseInt(c.getString(c.getColumnIndex("amount")));
                    niitemlist.add(new DataNoteProvider(d4,d2));
                    vec.add(1);
                } while (c.moveToNext());
            }
        }



        adapter = new friendDetailAdapter(getApplicationContext(),R.layout.note_row_layout,niitemlist);

        listView.setAdapter(adapter);



        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                if(vec.get(i)==0){
                    // if(list_items.contains(niitemlist.get(i))) {
                    list_item.remove(niitemlist.get(i));
                    count -= 1;
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);

                    vec.set(i,1);
                    //c.setSelected(true);
                    //convertView.setPressed(true);
                    String ii=Integer.toString(i);
                    Log.e("not select",ii);

                    actionMode.setTitle(count + " items selected");
                } else {
                    count += 1;
                    listView.getChildAt(i).setBackgroundColor(Color.LTGRAY);

                    vec.set(i,0);
                    String ii=Integer.toString(i);
                    Log.e("select",ii);
                    //   nlist.add(niitemlist.get(i));

                    actionMode.setTitle(count + " items selected");
                    // list_items.add(niitemlist.get(i));
                }

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.my_context_menu, menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                Log.e("1", "entwr switch");

                switch (menuItem.getItemId()) {
                    case R.id.delete_id:
                        for (int i = vec.size() - 1; i > -1; i--) {
                            if (vec.get(i) == 0) {
                                String notes = niitemlist.get(i).getNote();
                                int amount = niitemlist.get(i).getAmount();
                                vec.set(i, 1);
                                listView.getChildAt(i).setBackgroundColor(Color.WHITE);
                                final String TABLE_NAME = p;
                                final String NOTE = "note";
                                final String FRIEND = "friend_name";
                                Log.e("table name", TABLE_NAME);
                                db2.execSQL("DELETE FROM " + TABLE_NAME + " WHERE note='" + notes + "';");

                                Cursor c4 = db2.rawQuery("SELECT * FROM friends;", null);
                                if (c4 != null) {
                                    if (c4.moveToFirst()) {
                                        do {
                                            String ch = c4.getString(c4.getColumnIndex("friend"));
                                            if (ch.matches(p)) {
                                                int money = c4.getInt(c4.getColumnIndex("amount"));
                                                int index = c4.getInt(c4.getColumnIndex("ID"));
                                                money = money - amount;
                                                ContentValues cv = new ContentValues();
                                                cv.put("amount", money);
                                                db2.update("friends", cv, "ID=" + index, null);
                                            }

                                        } while (c4.moveToNext());
                                    }

                                }
                                niitemlist.remove(i);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        Toast.makeText(getApplicationContext(), count + " items removed ", Toast.LENGTH_SHORT).show();
                        count = 0;
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }

            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });



    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),Display_friend.class);
        startActivity(intent);
        finish();

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
