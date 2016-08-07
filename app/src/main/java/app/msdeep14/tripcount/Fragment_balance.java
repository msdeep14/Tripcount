package app.msdeep14.tripcount;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mandeep singh on 7/23/2016.
 * show results of balanced amount to be shared between friends
 */
public class Fragment_balance extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    String p;
    int n;
    SQLiteDatabase db1=null;
    balance_adapter adapter;
    ArrayAdapter adapter2;
    ArrayList<balancedata> list_bal=new ArrayList<>();
    ArrayList<balancedata> list_items=new ArrayList<>();
    ListView list_a,list_b;
    ArrayList<String> list=new ArrayList<String>();
    ArrayList<String> list_ball=new ArrayList<String >();

    // newInstance constructor for creating fragment with arguments
    public static Fragment_balance newInstance(int page, String title) {
        Fragment_balance fragmentSecond = new Fragment_balance();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        Log.e("frament balance","222222222");
        fragmentSecond.setArguments(args);
        return fragmentSecond;
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
        View view = inflater.inflate(R.layout.balance_fragment, container, false);
        List<calculate> list = new ArrayList<>();

        list_a=(ListView)view.findViewById(R.id.list_above);
        list_b=(ListView)view.findViewById(R.id.list_below);
        db1=getActivity().openOrCreateDatabase("trip.db", Context.MODE_PRIVATE, null);
        Intent in=getActivity().getIntent();
        Bundle bundle=in.getExtras();
        p=bundle.getString("id");
        Cursor c=db1.rawQuery("SELECT * FROM Trip_details ORDER BY date_go DESC;",null);
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    String s=c.getString(c.getColumnIndex("place_name"));
                    if(s.matches(p)){
                        n=c.getInt(c.getColumnIndex("friend_no"));
                        break;
                    }

                }while(c.moveToNext());
            }
        }
        String table="f"+p;
        int i=0;
        c=db1.rawQuery("SELECT * FROM "+table+";",null);
        if(c!=null){
            if (c.moveToFirst()) {
                do{
                    calculate assignment1 = new calculate();
                    assignment1.name = c.getString(c.getColumnIndex("friend"));
                    assignment1.money = c.getDouble(c.getColumnIndex("amount"));
                    list.add(assignment1);
                    i++;
                }while(c.moveToNext());
            }
        }



        Collections.sort(list, new Comparator<calculate>() {
            @Override
            public int compare(calculate fruit2, calculate fruit1)
            {
                return Double.compare(fruit2.money, fruit1.money);
            }
        });

        double avg=0.0;
        for(calculate p :list)
        {
            avg+=p.money;
        }
        avg=(1.0*avg)/n;
        DecimalFormat df = new DecimalFormat("####0.00");
        for(calculate p:list){
            p.money-=avg;
            String d1=p.name;
            String d2=df.format(p.money);
            list_items.add(new balancedata(d1,d2));
        }
        adapter= new balance_adapter(getActivity(),R.layout.balance_textview,list_items);
        list_a.setAdapter(adapter);

        for(calculate p : list){
            Log.e(p.name,String.valueOf(p.money));
        }

        //update the second list for sorting out who owes whom and how much money

        int j=list.size()-1;
        i=0;
        while(i<j){
            if (Math.abs(list.get(i).money)>Math.abs(list.get(j).money)){
                list_ball.add(list.get(i).name + " owes " + list.get(j).name + " :: " + df.format(Math.abs(list.get(j).money)));
                list.get(i).money += list.get(j).money;
                list.get(j).money = 0.0;
                j--;
            }
            else if(Math.abs(list.get(i).money)<Math.abs(list.get(j).money)){
                list_ball.add(list.get(i).name + " owes " + list.get(j).name + " :: " + df.format(Math.abs(list.get(i).money)));
                list.get(j).money += list.get(i).money;
                list.get(i).money = 0.0;
                i++;
            }
            else {
                list_ball.add(list.get(i).name + " owes " + list.get(j).name + " :: " + df.format(Math.abs(list.get(i).money)));
                list.get(i).money = 0.0;
                list.get(j).money = 0.0;
                i++;
                j--;
            }
        }
        adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_ball);
        list_b.setAdapter(adapter2);

        return view;
    }


    public class calculate
    {
        String name;
        Double money;

    }

}
