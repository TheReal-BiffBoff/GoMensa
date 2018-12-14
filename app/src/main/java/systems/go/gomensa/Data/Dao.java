package systems.go.gomensa.Data;

import android.util.Log;

import java.util.ArrayList;

import systems.go.gomensa.Entities.Day;
import systems.go.gomensa.Entities.Dish;

public class Dao {
    final String TAG = "MensaApp DAO";
    //TODO: Implement sqLite Room Database for storage

    public Dao(){

    }

    public void updateMensa(ArrayList<Day> days){
        logAll(days);
    }

    public void updateMensaB(ArrayList<Day> days){
        logAll(days);
    }

    public ArrayList<Day> getMensaA(){
        return null;
    }

    public ArrayList<Day> getMensaB(){
        return null;
    }

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
