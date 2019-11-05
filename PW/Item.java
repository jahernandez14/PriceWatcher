package hw4;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;


/**
 *Class used to create an item
 * @author Julio Hernandez & Jonathan Argumedo & Jose Lujan
 */
public class Item extends PriceFinder {
	private String name = "";
	private String url = "";
	private double initialPrice= 0;
	private double currentPrice = 0;
	private double change = 0;
	private String dateAdded;
	
	/**
	 * Constructor that creates an instance of an Item.
	 */
	public Item() {
	}
	
	/**
	 * Constructor used to create an item
	 * @param name description of item
	 * @param url URL of specified item
	 */
	public Item(String name, String url) {
		this.name = name;
		this.url = url;
		currentPrice = urlPrice(url);
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		dateAdded = format.format(date);	
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJson() {
    	JSONObject obj = new JSONObject();
    	obj.put("name",  name);
    	obj.put("url", url);
    	obj.put("initialPrice", initialPrice);
    	obj.put("currentPrice", currentPrice);
    	obj.put("change", change);
    	obj.put("dateAdded", dateAdded);
    	return obj;
    }
	
	/**
	 * Getter to retrieve item name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter to retrieve URL
	 * @return
	 */
	public String geturl() {
		return url;
	}
	
	/**
	 * Getter to return current price
	 * @return
	 */
	public double getCurrentPrice() {
		return currentPrice;
	}
	
	/**
	 * Getter to return initial price of item
	 * @return
	 */
	public double getInitialPrice() {
		return initialPrice;
	}
	
	/**
	 * Getter to get percent of change of a given item
	 * @return
	 */
	public double getChange() {
		return change;
	}
	
	/**
	 * getter to get the date added of item
	 * @return
	 */
	public String getDateAdded() {
		return dateAdded;
	}
	/**
	 * Setter to set name of item
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * setter to set URL of item
	 * @param url
	 */
	public void seturl(String url) {
		this.url = url;
	}
	/**
	 * setter to set initial price of item
	 * @param initialPrice
	 */
	public void setInitialPrice(double initialPrice) {
		this.initialPrice = initialPrice;
	}
	/**
	 * Setter to set current price of the item
	 * @param currentPrice
	 */
	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}
	/**
	 * setter to set change of item
	 * @param change
	 */
	public void setChange(double change) {
		this.change = change;
	}
	/**
	 * setter to set date added of item
	 * @param dateAdded
	 */
	public void setDateAdded(String dateAdded) {
		this.dateAdded = dateAdded;
	}
	/**
	 * method used to randomize the updated price of an item
	 */
	public void update() {
		//Randomizer random = new Randomizer();
		initialPrice= currentPrice;
    	currentPrice = urlPrice(url);
    	change = Math.round((((currentPrice-initialPrice)/ initialPrice) * 100)*100.0)/100.0;
	}
	
}
