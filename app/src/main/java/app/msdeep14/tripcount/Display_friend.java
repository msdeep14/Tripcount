package app.msdeep14.tripcount;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Display_friend extends AppCompatActivity {

    private String m_Text = "";
       ListView listView;
    int count=0;
    SQLiteDatabase db2 = null;
    ArrayList<DataProvider> list_item = new ArrayList<>();
    Vector<Integer> vector = new Vector<>();
    Vector<Integer> vec = new Vector<>();
    friendAdapter adapter;
    private List<DataProvider> niitemlist;
    EditText e1;
    RadioButton rb;
    RadioButton rb2;
    ImageButton f1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_friend);
        listView = (ListView) findViewById(R.id.list_friend);
        db2 = openOrCreateDatabase("trans.db", Context.MODE_PRIVATE, null);
        e1 = (EditText) findViewById(R.id.addname);
        f1=(ImageButton) findViewById(R.id.myF);

        setTitle("Friend List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        db2.execSQL("CREATE TABLE IF NOT EXISTS friends (ID INTEGER PRIMARY KEY AUTOINCREMENT ,friend TEXT NOT NULL,amount INTEGER NOT NULL DEFAULT 0);");


        Cursor c = db2.rawQuery("SELECT * FROM friends;", null);
        niitemlist=new ArrayList<>();

        if (c != null) {
            int i = 0;
            if (c.moveToFirst()) {
                do {
                    String d2 = c.getString(c.getColumnIndex("friend"));
                    int d4 = Integer.parseInt(c.getString(c.getColumnIndex("amount")));
                    niitemlist.add(new DataProvider(d4,d2));
                    vec.add(1);


                } while (c.moveToNext());
            }
        }

        try {
            adapter = new friendAdapter(getApplicationContext(),R.layout.row_layout,niitemlist);

            listView.setAdapter(adapter);
        } catch (Exception e) {
            Log.e("hvfjdkl", "fjdkmcxz");
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
            System.out.println(e);
        }

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                if(vec.get(i)==0){
                    list_item.remove(niitemlist.get(i));
                    count -= 1;
                    listView.getChildAt(i).setBackgroundColor(Color.WHITE);

                    vec.set(i,1);
                    String ii=Integer.toString(i);
                    Log.e("not select",ii);

                    actionMode.setTitle(count + " items selected");
                } else {
                    count += 1;
                    listView.getChildAt(i).setBackgroundColor(Color.LTGRAY);

                    vec.set(i,0);
                    String ii=Integer.toString(i);
                    Log.e("select",ii);
                    actionMode.setTitle(count + " items selected");
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
                Log.e("1","entwr switch");

                switch (menuItem.getItemId()) {
                    case R.id.delete_id:
                        for (int i=vec.size()-1;i>-1;i--) {
                            if (vec.get(i) == 0) {
                                String name=niitemlist.get(i).getName();
                                int amount=niitemlist.get(i).getAmount();
                                vec.set(i,1);
                                listView.getChildAt(i).setBackgroundColor(Color.WHITE);
                                final String TABLE_NAME ="friends";
                                final String NOTE="note";
                                final String FRIEND = "friend_name";
                                Log.e("table name",TABLE_NAME);
                                db2.execSQL("DELETE FROM "+TABLE_NAME+" WHERE friend='"+name+"';");
                                db2.execSQL("DROP TABLE IF EXISTS " +name+ ";");
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

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        TextView temp = (TextView) view.findViewById(R.id.name);
                        String str = temp.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), Display_friendDetails.class);
                        intent.putExtra("id", str);
                        startActivity(intent);
                        finish();
                    }
                }
        );

        OnFabButtonClicked();
    }

    void OnFabButtonClicked() {
        f1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int flag=0;
                        String s = e1.getText().toString().toLowerCase();
                        s=s.toLowerCase();
                        if (s.matches("")) {
                            Toast.makeText(getApplicationContext(), "field empty", Toast.LENGTH_SHORT).show();
                        } else {
                            //check if the new name already exists in the table
                            Cursor c1=db2.rawQuery("SELECT * FROM friends;",null);
                            if(c1!=null){
                                if(c1.moveToFirst()){
                                    do{
                                        String  m=c1.getString(c1.getColumnIndex("friend"));
                                        if(m.matches(e1.getText().toString().toLowerCase())){
                                            Toast.makeText(getApplicationContext(),"Name already exists",Toast.LENGTH_SHORT).show();
                                            e1.setText("");
                                            flag=1;
                                            break;
                                        }
                                    }while(c1.moveToNext());
                                }
                            }


                            if(flag==0) {
                                String COL_2 = "friend";
                                String table = "friends";
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(COL_2, e1.getText().toString().toLowerCase());
                                long result = db2.insert(table, null, contentValues);
                                if (result != -1) {
                                    //  Toast.makeText(getApplicationContext(), "friend Added", Toast.LENGTH_SHORT).show();
                                    Intent lis = new Intent(getApplicationContext(), Display_friend.class);
                                    startActivity(lis);
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "friend not added!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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