package systems.go.gomensa.Entities;

import java.io.Serializable;

public class Dish implements Serializable {

	public String title;
	public String prices;
	
	public Dish(String title, String prices) {
		this.title = title;
		this.prices = prices;
	}
}
