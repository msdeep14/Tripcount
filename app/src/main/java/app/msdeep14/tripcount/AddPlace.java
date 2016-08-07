package app.msdeep14.tripcount;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/*
* displays list of created trips
* */

public class AddPlace extends AppCompatActivity {

    private static Button btn_go;
    TextView t;

    Long k;
    int j;
    FloatingActionButton fb;
    String j1;
    TextView noDet;
    SQLiteDatabase db1=null;
    FloatingActionButton f;
    ArrayList<String> notes = new ArrayList<String>();
    ArrayList<String> list_item = new ArrayList<>();
    Vector<Integer> vector = new Vector<>();
    Vector<Integer> vec = new Vector<>();
    int count=0;
    add_adapter adapter;
    List<additem> niitemlist;
    ArrayAdapter adapter1;
    ListView listView;
    ListView listView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillActivity();
        fb=(FloatingActionButton)findViewById(R.id.mynew);
        setTitle("Trips");
        opennewwindow();
        listView=(ListView)findViewById(R.id.list_add);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView temp = (TextView) view.findViewById(R.id.name);
                        String str = temp.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), ViewTripDetails.class);
                        intent.putExtra("id", str);
                        startActivity(intent);
                        finish();
                    }
                }
        );
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(AddPlace.this, MainActivity.class));
        finish();

    }

    //function to fill up the activity with trip details from data base create trip.db
    void fillActivity(){
        db1=openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);

        niitemlist=new ArrayList<>();
        listView=(ListView)findViewById(R.id.list_add);
        db1.execSQL("CREATE TABLE IF NOT EXISTS Trip_details (id INTEGER PRIMARY KEY AUTOINCREMENT ,place_name TEXT NOT NULL, date_go DATE NOT NULL, "+"friend_no INTEGER NOT NULL DEFAULT 0);");
        Cursor c=db1.rawQuery("SELECT * FROM Trip_details ORDER BY date_go DESC;",null);
        if(c!=null)
        {if(c.moveToFirst())

            do{

                String d4=c.getString(c.getColumnIndex("place_name"));
                final Long d1=c.getLong((c.getColumnIndex("date_go")));
                String d2=c.getString(c.getColumnIndex("date_go"));
                String temp=d4;
                niitemlist.add(new additem(d4,d2));
                vec.add(1);
                j++;	}while(c.moveToNext());
        }

        adapter = new add_adapter(getApplicationContext(),R.layout.add_text,niitemlist);

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
                                String name = niitemlist.get(i).getName();
                                String amount = niitemlist.get(i).getDate();
                                vec.set(i, 1);
                                listView.getChildAt(i).setBackgroundColor(Color.WHITE);
                                final String TABLE_NAME = "Trip_details";
                                final String NOTE = "note";
                                Log.e("table name", TABLE_NAME);
                                db1.execSQL("DELETE FROM " + TABLE_NAME + " WHERE place_name='" + name + "';");
                                String tb1="f"+name;
                                db1.execSQL("DROP TABLE IF EXISTS'"+tb1+"';");
                                db1.execSQL("DROP TABLE IF EXISTS'"+name+"';");
                                niitemlist.remove(i);

                            }

                        }
                        adapter.notifyDataSetChanged();
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

    //when clicked on floating action button
    public void opennewwindow(){
        fb.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //
                        Intent intent= new Intent(AddPlace.this,AddPlaceDetails.class);
                        startActivity(intent);
                        finish();
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
