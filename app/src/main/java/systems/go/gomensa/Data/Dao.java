package systems.go.gomensa.Data;

import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import systems.go.gomensa.Entities.Day;
import systems.go.gomensa.Entities.Dish;
import systems.go.gomensa.Interface.MainActivity;

public class Dao {
    private final String TAG = "DAO";
    private final String MENSA_PREF = "goMensaA";
    private final String MENSAB_PREF = "goMensaB";

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
    // UPDATE FUNCTIONS
    //----------------------------------------------------------------------------------------------

    public void updateMensa(ArrayList<Day> days){
        aMensaDays = days;
        storeJson(MENSA_PREF, days);
        logAll(days);
    }

    public void updateMensaB(ArrayList<Day> days){
        bMensaDays = days;
        storeJson(MENSAB_PREF, days);
        logAll(days);
    }

    //----------------------------------------------------------------------------------------------
    // JSON STORAGE
    //----------------------------------------------------------------------------------------------

    public void storeJson(String key, ArrayList<Day> values) {
        Gson gson = new Gson();
        String jsonObject = gson.toJson(values);

        SharedPreferences.Editor editor = MainActivity.preferences.edit();
        editor.putString(key, jsonObject);
        editor.commit();
        Log.v(TAG, jsonObject);
    }

    public ArrayList<Day> loadJson(String key){
        SharedPreferences sharedPref = MainActivity.preferences;    //Load from shared pref
        String json = sharedPref.getString(key, "");
        Gson gson = new Gson();                                     //JSON to Array List
        Type type = new TypeToken<ArrayList<Day>>(){}.getType();

        return gson.fromJson(json, type);
    }

    //----------------------------------------------------------------------------------------------
    // GET FUNCTIONS
    //----------------------------------------------------------------------------------------------

    public ArrayList<Day> getMensaA(){
        if(aMensaDays.size() == 0){
            aMensaDays = loadJson(MENSA_PREF);
        }

        return aMensaDays;
    }

    public ArrayList<Day> getMensaB(){
        if(bMensaDays.size() == 0){
            bMensaDays = loadJson(MENSAB_PREF);
        }

        return bMensaDays;
    }

    //----------------------------------------------------------------------------------------------
    // LOG HELPER FOR TESTING PURPOSES
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
