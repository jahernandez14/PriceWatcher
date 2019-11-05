package hw4;

import javax.swing.DefaultListModel;

/**
 *A class used to store all items and make them accessible by the user
 * @author Julio Hernandez & Jonathan Argumedo & Jose Lujan
 */
public class ItemManager {
	private DefaultListModel<Item> itemList;
	
	/**
	 * Method used to retrieve the created list
	 * @return itemList list created
	 */
	public DefaultListModel<Item> getList(){
		return itemList;
	}
	
	/**
	 * Item list constructor
	 */
	public ItemManager(){
		itemList = new DefaultListModel<Item>();
	}
	
	/**
	 * Method used to add an element to the item list
	 * @param item item added to the list
	 */
	public void addElement(Item item) {
		itemList.addElement(item);
	}
	
	/**
	 * Method used to acquire the index of an item in the list
	 * @param index index of item in list
	 * @return item in the desired index
	 */
	public Item get(int index) {
		return itemList.get(index);
	}
	
	/**
	 * Method to retrive size of list
	 * @return size of list
	 */
	public int sizeOfList() {
		return itemList.size();
	}
	
	/**
	 * Method to update selected item
	 * @param index index of item in list
	 * @param item item selected
	 */
	public void updateSelected(int index, Item item) {
		item.update();
		itemList.setElementAt(item, index);
	}
	
	/**
	 * Method to remove item from list
	 * @param index index of item you wish to remove
	 */
	public void removeItem(int index) {
		itemList.remove(index);
	}
	
}
