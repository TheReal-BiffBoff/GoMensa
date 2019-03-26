package systems.go.gomensa.Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import systems.go.gomensa.Entities.Day;
import systems.go.gomensa.Entities.Dish;
import systems.go.gomensa.Interface.MainActivity;
import systems.go.gomensa.R;

public class Dao {
    final String TAG = "DAO";
    final String MENSA_FILE = "goMensaA";
    final String MENSAB_FILE = "goMensaB";
    Context context;

    //TODO: Implement sqLite Room Database for storage

    private ArrayList<Day> aMensaDays = new ArrayList<>();
    private ArrayList<Day> bMensaDays = new ArrayList<>();
    private static Dao instance;

    public Dao(){

    }

    public static Dao getInstance(){
        if (instance == null) {
            instance = new Dao();
        }

        return instance;
    }

    //----------------------------------------------------------------------------------------------

    public void updateMensa(ArrayList<Day> days){
        aMensaDays = days;
        // save the task list to preference
        logAll(days);
    }

    public void updateMensaB(ArrayList<Day> days){
        bMensaDays = days;
        logAll(days);
    }

    //----------------------------------------------------------------------------------------------

    public ArrayList<Day> getMensaA(){
        return aMensaDays;
    }

    public ArrayList<Day> getMensaB(){
        return bMensaDays;
    }

    //----------------------------------------------------------------------------------------------

    public void logAll(ArrayList<Day> days){

        Log.v(TAG, "LOGGING ALL DAYS:");

        for(Day day:days){
            Log.v(TAG, day.getDate());

            for(Dish dish: day.getDishes()){
                String dishStr = dish.title + " | " + dish.prices;
                Log.v(TAG, dishStr);
            }
        }
        Log.v(TAG, "------------------------------------------------------------------------");
    }

}
