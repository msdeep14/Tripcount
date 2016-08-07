package app.msdeep14.tripcount;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by mandeep singh on 7/23/2016.
 */
public class Fragment_show extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    String p;
    private ListView nlist;
    private adapter_Show adapter;
    private List<item> niitemlist;
    ArrayList<item> list_items= new ArrayList<>();
    Vector<Integer> vec =new Vector<>();

    int flag=0;
    SQLiteDatabase db1=null;


    Button btn;
    int count=0;
    FloatingActionButton f;

    // newInstance constructor for creating fragment with arguments
    public static Fragment_show newInstance(int page, String title) {
        Fragment_show fragmentFirst = new Fragment_show();
        Bundle args = new Bundle();
        Log.e("fragment show","1111111111111");
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.show_fragment, container, false);

        db1 = getActivity().openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);
        Intent in = getActivity().getIntent();
        Bundle bundle = in.getExtras();
        p = bundle.getString("id");
        f = (FloatingActionButton) view.findViewById(R.id.myFAB);
        f.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Update.class);
                        intent.putExtra("id", p);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
        );


        nlist = (ListView) view.findViewById(R.id.list_view);
        niitemlist = new ArrayList<>();
        int x=0;
        db1.execSQL("create table if not exists " + p + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,friend_name TEXT,note TEXT,amount TEXT)");
        Cursor c = db1.rawQuery("SELECT * FROM " + p + ";", null);
        if (c != null) {
            int i = 0;
            if (c.moveToFirst()) {
                do {
                    //create list view from table place_name
                    String d2 = c.getString(c.getColumnIndex("friend_name"));
                    String d1 = c.getString(c.getColumnIndex("note"));
                    String d4 = c.getString(c.getColumnIndex("amount"));
                    vec.add(1);
                    niitemlist.add(new item(d1,d4,d2));

                } while (c.moveToNext());
            }
        }
        try {
            //adapter=new adapter_Show(Fragment_show.this,niitemlist);
            adapter =new adapter_Show(getActivity(),R.layout.textfile,niitemlist);

            nlist.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "error", Toast.LENGTH_SHORT).show();
            System.out.println(e);
        }

        nlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        nlist.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                if(vec.get(i)==0){
                    list_items.remove(niitemlist.get(i));
                    count -= 1;
                    nlist.getChildAt(i).setBackgroundColor(Color.WHITE);

                    vec.set(i,1);
                    String ii=Integer.toString(i);
                    Log.e("not select",ii);

                    actionMode.setTitle(count + " items selected");
                } else {
                    count += 1;
                    nlist.getChildAt(i).setBackgroundColor(Color.LTGRAY);

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
                                TextView t = (TextView) view.findViewById(R.id.name);
                                String name=niitemlist.get(i).getName();
                                String amount=niitemlist.get(i).getAmount();
                                String note=niitemlist.get(i).getNote();

                                vec.set(i,1);
                                nlist.getChildAt(i).setBackgroundColor(Color.WHITE);
                                final String TABLE_NAME =p;
                                final String NOTE="note";
                                final String FRIEND = "friend_name";
                                Log.e("table name",TABLE_NAME);


                                db1.delete(TABLE_NAME,
                                        FRIEND + " = ? AND " + NOTE + " = ?",
                                        new String[] {name, note+""});
                                //update fpune
                                String tb="f"+p;
                                Log.e("sdhjbs",name);
                                Cursor c3 = db1.rawQuery("SELECT * FROM " + tb + ";", null);
                                if (c3 != null) {
                                    if (c3.moveToFirst()) {
                                        do {
                                            String ch = c3.getString(c3.getColumnIndex("friend"));
                                            Log.e("friend name",ch);
                                            if (ch.matches(name)) {
                                                String temp = c3.getString(c3.getColumnIndex("amount"));
                                                int index = c3.getInt(c3.getColumnIndex("ID"));
                                                int money1 = Integer.parseInt(temp);
                                                int money2 = Integer.parseInt(amount);
                                                int money= money1 - money2;
                                                ContentValues cv=new ContentValues();
                                                cv.put("amount",money);
                                                cv.put("friend",name);
                                                db1.update(tb,cv, "ID="+index, null);
                                                temp =c3.getString(c3.getColumnIndex("amount"));
                                                break;
                                            }

                                        } while (c3.moveToNext());
                                    }
                                }

                                niitemlist.remove(i);
                            }
                            adapter.notifyDataSetChanged();
                            flag=1;
                            start();
                        }
                        Toast.makeText(getActivity(), count + " items removed ", Toast.LENGTH_SHORT).show();
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

        return view;
    }
    public void start(){
        Intent same=new Intent(getActivity(),ViewTripDetails.class);
        same.putExtra("id",p);
        startActivity(same);
        getActivity().finish();
    }
}




