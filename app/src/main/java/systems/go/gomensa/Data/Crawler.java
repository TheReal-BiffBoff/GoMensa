package systems.go.gomensa.Data;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import systems.go.gomensa.Entities.Day;
import systems.go.gomensa.Entities.Dish;

public class Crawler extends AsyncTask<String, Void, String> {

    private final String mensaURL = "https://www.studentenwerk.sh/de/essen/standorte/flensburg/mensa-flensburg/speiseplan.html";
    private final String bmensaURL = "https://www.studentenwerk.sh/de/essen/standorte/flensburg/cafeteria-im-bgebaeude-fh/speiseplan.html";
    final String TAG = "MensaApp Class: Crawler";
    ArrayList<Day> daysA = new ArrayList<>();
    ArrayList<Day> daysB = new ArrayList<>();

    @Override
    protected String doInBackground(String... params) {

        //------------------------------------------------------------------------------------------
        //                                       MENSA CRAWL
        //------------------------------------------------------------------------------------------

        try {
            Document doc = Jsoup.connect(mensaURL).get();
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
        Dao dao = new Dao();
        dao.updateMensa(daysA);
        dao.updateMensaB(daysB);
        Log.v(TAG,"Crawler executed");
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}