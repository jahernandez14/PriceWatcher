package hw4;

import java.awt.Component;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Class that extends ItemView and implements ListCellRendered to render objects to be visible in JList
 * @author Julio Hernandez & Jonathan Argumedo & Jose Lujan
 *
 */
@SuppressWarnings("serial")
public class ItemRenderer extends ItemView implements ListCellRenderer<Item> {	
	
	/**
	 * method to retrieve a component of rendered list
	 * @return this a component that was rendered
	 */
	public Component getListCellRendererComponent(JList<? extends Item> list, Item item, int index, boolean isSelected, boolean cellHasFocus) {
		
		itemImport(item);
		
		if(isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
			}
		else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		return this;
	}
	
}

