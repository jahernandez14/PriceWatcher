package hw4;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class to store items persistently and write to a txt file
 * 
 * @author Julio Hernandez & Jonathan Argumedo
 *
 */
public class Database {
	private final String DIR = "/database/database.txt";
	private JSONArray jsonList = new JSONArray();
	private JSONParser jsonParser = new JSONParser();

	// make a JSONArray and then start populating it

	public Database() {

	}

	/**
	 * Method created to add an item to the database and write to database.txt
	 * 
	 * @param item
	 */
	@SuppressWarnings("unchecked")
	public void addToDatabase(Item item) {
		try {
			FileWriter pw = new FileWriter(getClass().getResource(DIR).getPath());
			jsonList.add(item.toJson());
			pw.write(jsonList.toJSONString());// convert to JSON and make that JSON object a string
			pw.flush();
			pw.close();
		} catch (IOException ee) {
			ee.getStackTrace();
		}
	}

	/**
	 * Method to retrieve the database and add it to itemManager at program
	 * initialization
	 * 
	 * @return
	 */
	public ItemManager retrieveDatabase() {
		ItemManager itemList = new ItemManager();
		JSONObject jsonObject;
		Object obj;
		try {
			obj = jsonParser.parse(new FileReader(getClass().getResource(DIR).getPath()));
			jsonList = (JSONArray) obj;
		} catch (IOException | ParseException e) {
		}

		for (int i = 0; i < jsonList.size(); i++) {
			Item item = new Item();
			jsonObject = (JSONObject) jsonList.get(i);

			item.setName((String) jsonObject.get("name"));
			item.seturl((String) jsonObject.get("url"));
			item.setInitialPrice((Double) jsonObject.get("initialPrice"));
			item.setCurrentPrice((Double) jsonObject.get("currentPrice"));
			item.setChange((Double) jsonObject.get("change"));
			item.setDateAdded((String) jsonObject.get("dateAdded"));
			itemList.addElement(item);
		}
		return itemList;
	}

	/**
	 * Method to edit item at a certain location and write changes to txt file
	 * 
	 * @param updatedItem
	 * @param index
	 */
	@SuppressWarnings("unchecked")
	public void edit(Item updatedItem, int index) {

		try {
			FileWriter pw = new FileWriter(getClass().getResource(DIR).getPath());
			jsonList.set(index, updatedItem.toJson());
			pw.write(jsonList.toJSONString());// convert to JSON and make that JSON object a string
			pw.flush();
			pw.close();
		} catch (IOException ee) {

		}

	}

	/**
	 * Method to delete item and rewrite to the txt file
	 * 
	 * @param list
	 */
	@SuppressWarnings("unchecked")
	public void delete(ItemManager list) {
		jsonList.clear();
		try {
			int i = 0;
			while (list.sizeOfList() >= i) {
				FileWriter pw = new FileWriter(getClass().getResource(DIR).getPath());
				jsonList.add(list.get(i).toJson());
				pw.write(jsonList.toJSONString());
				pw.flush();
				pw.close();
				i++;
			}

		} catch (IOException ee) {
			ee.getStackTrace();
		}
	}
}
