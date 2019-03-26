package systems.go.gomensa.Data;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import systems.go.gomensa.Entities.Day;
import systems.go.gomensa.Entities.Dish;
import systems.go.gomensa.Interface.MainActivity;

public class Crawler extends AsyncTask<String, Void, String> {

    private final String mensaURL = "https://www.studentenwerk.sh/de/essen/standorte/flensburg/mensa-flensburg/speiseplan.html";
    private final String bmensaURL = "https://www.studentenwerk.sh/de/essen/standorte/flensburg/cafeteria-im-bgebaeude-fh/speiseplan.html";
    final String TAG = "MensaApp Class: Crawler";
    ArrayList<Day> daysA = new ArrayList<>();
    ArrayList<Day> daysB = new ArrayList<>();
    Activity activity;

    public Crawler(TaskListener listener){
        this.taskListener = listener;
    }

    // TASK LISTENER FOR CALLBACK TO UI

    public interface TaskListener {
        public void onFinished(String result);
    }

    private final TaskListener taskListener;


    @Override
    protected String doInBackground(String... params) {

        //------------------------------------------------------------------------------------------
        //                                       MENSA CRAWL
        //------------------------------------------------------------------------------------------

        try {
            Log.v(TAG, "Starting A Mensa Crawl");
            Document doc = Jsoup.connect(mensaURL).get();
            Log.v(TAG, doc.toString());
            Elements dayTables = doc.getElementsByTag("tbody");			//Get all tbodys which contain the menu

            for(Element dayTable: dayTables) {
                Elements rows = dayTable.getElementsByTag("tr");			//The menu normally contains 4 tables rows (one th and three rows with dishes)

                if(dayTable.child(0).child(0).text().contains("Gericht am")){		//Check if the first element of the table contains "Gericht am" to be sure its a table with dishes
                    Day day = new Day();											//Create day to store date and dishes in
                    day.setDate(dayTable.child(0).child(0).text());

                    for(int i = 1; i < rows.size(); i++) {							//Go through all dishes for that day (Normally 3)
                        String classname = rows.get(i).className();

                        if(classname.equals("odd") || classname.equals("even")) {	//Dishes are in table row with classname odd and even
                            Dish dish = new Dish(rows.get(i).child(0).text(), rows.get(i).child(2).text());
                            day.addDish(dish);
                        }
                    }
                    daysA.add(day);
                }
            }

        } catch (Exception e) {
            Log.v(TAG, String.valueOf(e));
        }

        //------------------------------------------------------------------------------------------
        //                                      B MENSA CRAWL
        //------------------------------------------------------------------------------------------

        try {
            Document doc = Jsoup.connect(bmensaURL).get();
            Elements dayTables = doc.getElementsByTag("tbody");			//Get all tbodys which contain the menu

            for(Element dayTable: dayTables) {
                Elements rows = dayTable.getElementsByTag("tr");			//The menu normally contains 4 tables rows (one th and three rows with dishes)

                if(dayTable.child(0).child(0).text().contains("Gericht am")){		//Check if the first element of the table contains "Gericht am" to be sure its a table with dishes
                    Day day = new Day();											//Create day to store date and dishes in
                    day.setDate(dayTable.child(0).child(0).text());

                    for(int i = 1; i < rows.size(); i++) {							//Go through all dishes for that day (Normally 3)
                        String classname = rows.get(i).className();

                        if(classname.equals("odd") || classname.equals("even")) {	//Dishes are in table row with classname odd and even
                            Dish dish = new Dish(rows.get(i).child(0).text(), rows.get(i).child(2).text());
                            day.addDish(dish);
                        }
                    }
                    daysB.add(day);
                }
            }
        } catch (Exception e) {
            Log.v(TAG, String.valueOf(e));
        }

        return "Executed";
    }


    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        Dao.getInstance().updateMensa(daysA);
        Dao.getInstance().updateMensaB(daysB);

        // In onPostExecute we check if the listener is valid
        if(this.taskListener != null) {

            // And if it is we call the callback function on it.
            this.taskListener.onFinished(result);
        }
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}