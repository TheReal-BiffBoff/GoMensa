package systems.go.gomensa.Entities;

import java.io.Serializable;
import java.util.ArrayList;

import systems.go.gomensa.Entities.Dish;

public class Day implements Serializable {
	
	String date;
	
	private ArrayList<Dish> dishes = new ArrayList<Dish>();
	
	public Day() {
		
	}
	
	public void addDish(Dish dish) {
		dishes.add(dish);
	}
	
	public ArrayList<Dish> getDishes(){
		return dishes;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getDate() {
		return this.date;
	}

}
