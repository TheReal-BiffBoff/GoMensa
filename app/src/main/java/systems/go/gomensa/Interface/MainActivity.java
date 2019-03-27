package systems.go.gomensa.Interface;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import systems.go.gomensa.Data.Crawler;
import systems.go.gomensa.Data.Dao;
import systems.go.gomensa.Entities.Day;
import systems.go.gomensa.Entities.Dish;
import systems.go.gomensa.R;

public class MainActivity extends AppCompatActivity {

    private PagerAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton refreshBtn;
    private PagerAdapter adapter;
    private static String TAG = "MENSAMAIN";

    public static SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences = getSharedPreferences( getPackageName() + "_preferences", MODE_PRIVATE);

        //Page Adapter for swiping pages:
        mSectionsPageAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        setupViewPager(mViewPager);

        //Init tab layout:
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Refresh button calls crawler
        refreshBtn = findViewById(R.id.refresh);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadButtonClick(v.getContext());
            }
        });

        loadButtonClick(this);
    }

    //----------------------------------------------------------------------------------------------
    // LOAD BUTTON CLICK
    //----------------------------------------------------------------------------------------------

    public void loadButtonClick(Context c){
        Toast.makeText(c, "Lade Daten...", Toast.LENGTH_SHORT).show();

        Crawler crawler = new Crawler(new Crawler.TaskListener() {
            @Override
            public void onFinished(String result) {
                Log.v(TAG, "Crawler finished loading");
                refreshFragmentContents();
            }
        });

        crawler.execute();
    }

    //----------------------------------------------------------------------------------------------
    // VIEW PAGER SETUP
    //----------------------------------------------------------------------------------------------

    private void setupViewPager(ViewPager viewPager) {
        adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MensaFragment(), "Mensa");
        adapter.addFragment(new MensaFragment(), "B-Mensa");
        viewPager.setAdapter(adapter);
    }

    //----------------------------------------------------------------------------------------------
    // LOAD CONTENT TO FRAGMENTS:
    //----------------------------------------------------------------------------------------------

    private void refreshFragmentContents(){
        Log.v(TAG, "Refreshing fragment contents");
        try{
            LinearLayout amensaList = adapter.getItem(0).getView().findViewById(R.id.container);
            LinearLayout bmensaList = adapter.getItem(1).getView().findViewById(R.id.container);
            amensaList.removeAllViews();
            bmensaList.removeAllViews();
            fillDishList(amensaList, Dao.getInstance().getMensaA());
            fillDishList(bmensaList, Dao.getInstance().getMensaB());
            Toast.makeText(this, "Guten Hunger! ;)", Toast.LENGTH_SHORT).show();    //TODO: Don't show if loading failed
        }catch (NullPointerException e){
            Toast.makeText(this, "Whoops, da ist was schief gelaufen :/", Toast.LENGTH_SHORT).show();
        }

    }

    public void fillDishList(LinearLayout list, ArrayList<Day> days){
        for (Day day : days) {
            list.addView(createDaySeperatorElement(day.getDate()));   //Adding a day seperator

            for (Dish dish : day.getDishes()){                  //Iterate through all dishes
                list.addView(createDishElement(dish));
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    // CUSTOM VIEW ELEMENTS
    //----------------------------------------------------------------------------------------------

    private View createDishElement(Dish dish){
        View view = getLayoutInflater().inflate(R.layout.dish, null);
        TextView title = view.findViewById(R.id.dish_title);
        TextView prices = view.findViewById(R.id.dish_prices);

        title.setText(dish.title);
        prices.setText(dish.prices);

        return view;
    }

    private View createDaySeperatorElement(String dateStr){
        View view = getLayoutInflater().inflate(R.layout.day_seperator, null);
        TextView date = view.findViewById(R.id.date);
        date.setText(dateStr);
        return view;
    }
}