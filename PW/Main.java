package hw4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 * @author Julio Hernandez & Jonathan Argumedo
 * @version 4.4 (5/2/2019)
 * @since 3.0
 *
 *        This is the main class to the application called price watcher. The
 *        application intends to keep track and randomize prices of the specific
 *        item. In addition, with this version you are able to add and modify
 *        items using a more advanced GUI system.
 */

@SuppressWarnings("serial")
public class Main extends JFrame {
	private Database database = new Database();
	private JList<Item> list;
	private ItemManager itemList = new ItemManager();
	private final static Dimension DEFAULT_SIZE = new Dimension(400, 600);
	private JLabel msgBar = new JLabel(" ");
	private final static String IMAGE_DIR = "/images/";
	private JPanel bar;
	private JPanel top = new JPanel(new BorderLayout());

	public Main() {
		this(DEFAULT_SIZE);
	}

	/**
	 * Create a new dialog of the given screen dimension. Main container that calls
	 * and makes the GUI visible
	 */
	public Main(Dimension dim) {
		super("Price Watcher");
		setSize(dim);
		setIconImage(getIcon("pw.png"));
		configureUI();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
		setResizable(true);
		setLocationRelativeTo(null);
		showMessage("Welcome!");
		try {
			Thread.sleep(1000);
			bar.removeAll();
		} catch (InterruptedException e1) {
		}
		
	}

	/** Configure UI */
	private void configureUI() {
		/** Layout of UI is set */
		setLayout(new BorderLayout());
		setJMenuBar(createMenuBar());
		JToolBar control = makeControlPanel();
		JToolBar controlR = makeControlPanelRight();
		top.add(control, BorderLayout.CENTER);
		bar = new ProgressBar();
		top.add(bar, BorderLayout.SOUTH);
		top.add(controlR, BorderLayout.EAST);
		add(top, BorderLayout.NORTH);

		/**
		 * Default item is created to allow a visual representation of how JList should
		 * look
		 */
		itemList = database.retrieveDatabase();
		list = new JList<Item>(itemList.getList());

		// dimensions
		list.setFixedCellWidth(100);
		list.setFixedCellHeight(150);

		add(new JScrollPane(list));
		list.setCellRenderer(new ItemRenderer());
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayout(new BoxLayout(list, BoxLayout.PAGE_AXIS));
		list.setVisibleRowCount(2);

		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
			}
		});

		list.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent ee) {
				if (SwingUtilities.isLeftMouseButton(ee)) {
					list.setSelectedIndex(list.locationToIndex(ee.getPoint()));
					if (ee.getPoint().x >= 25 && ee.getPoint().x <= 40
							&& ee.getPoint().y >= (list.getSelectedIndex() * 150) + 10
							&& ee.getPoint().y < (list.getSelectedIndex() * 150) + 25) {
						viewPageClicked(list.getModel().getElementAt(list.getSelectedIndex()));
					}
				}
			}
		});

		/** rightClick popUpMenu for JList */
		list.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					list.setSelectedIndex(list.locationToIndex(e.getPoint()));
					JPopupMenu popUpMenu = new JPopupMenu();

					JMenuItem checkPriceSelected = new JMenuItem("Price");
					checkPriceSelected.addActionListener(Main.this::refreshButtonClicked);
					checkPriceSelected.setToolTipText("Check price of selected item");

					JMenuItem viewUrlSelected = new JMenuItem("View");
					viewUrlSelected.addActionListener(Main.this::searchWebClicked);
					viewUrlSelected.setToolTipText("View web page of selected item");

					JMenuItem editItemSelected = new JMenuItem("Edit");
					editItemSelected.addActionListener(Main.this::editButtonClicked);
					editItemSelected.setToolTipText("Edit selected item");

					JMenuItem removeItemSelected = new JMenuItem("Remove");
					removeItemSelected.addActionListener(Main.this::deleteButtonClicked);
					removeItemSelected.setToolTipText("Delete selected item");

					checkPriceSelected.setIcon(new ImageIcon(getIcon("check_green.png")));

					viewUrlSelected.setIcon(new ImageIcon(getIcon("document.png")));
					viewUrlSelected.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

					editItemSelected.setIcon(new ImageIcon(getIcon("edit.png")));
					editItemSelected.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

					removeItemSelected.setIcon(new ImageIcon(getIcon("remove.png")));
					removeItemSelected.setIconTextGap(3);
					removeItemSelected.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

					checkPriceSelected.setMnemonic(KeyEvent.VK_P);
					checkPriceSelected.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));

					viewUrlSelected.setMnemonic(KeyEvent.VK_V);
					viewUrlSelected.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));

					editItemSelected.setMnemonic(KeyEvent.VK_E);
					editItemSelected.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));

					removeItemSelected.setMnemonic(KeyEvent.VK_R);
					removeItemSelected.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));

					popUpMenu.add(checkPriceSelected);
					popUpMenu.add(viewUrlSelected);
					popUpMenu.add(editItemSelected);
					popUpMenu.add(removeItemSelected);

					popUpMenu.show(list, e.getPoint().x, e.getPoint().y);
				}
			}
		});

		msgBar.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 0));
		add(msgBar, BorderLayout.SOUTH);
	}

	private void invalidUrlDialog() {
		JOptionPane.showMessageDialog(this,
				"ERROR: Invalid URL\n\nValid format: \nhttps://www.ebay.com/\nhttps://www.homedepot.com/\nhttps://www.walmart.com/",
				"URL ERROR", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Helper method to allow easy image additions
	 * 
	 * @param icon name of the image to be searched in the IMAGE_DIR directory
	 * @return An instance of an Image is returned, null if no image is found.
	 */
	public Image getIcon(String icon) {
		try {

			URL url = new URL(getClass().getResource(IMAGE_DIR), icon);
			return ImageIO.read(url);
		} catch (IOException e) {
			System.out.println("IMAGE NOT FOUND!");
		}
		return null;
	}

	/**
	 * Callback to be invoked when the view-page icon is clicked. Launch a (default)
	 * web browser by supplying the URL of the item.
	 * 
	 * @param Item name of URL of the specified item
	 */
	private void viewPageClicked(Item itemUrl) {
		try {
			Desktop d = Desktop.getDesktop();
			d.browse(new URI(itemUrl.geturl()));
			showMessage("url launched!");
		} catch (Exception e) {
			showMessage("ERROR: Invalid URL");
		}
	}

	/**
	 * Implementation of the control panel The control panel includes all the
	 * buttons needed to modify the selected item.
	 * 
	 * @return panel constructed to populate the north panel of the GUI
	 */
	private JToolBar makeControlPanel() {
		JToolBar panel = new JToolBar();

		/** Prevents the control panel from becoming detachable */
		panel.setFloatable(false);

		/** Button for checking all item prices */
		JButton check = new JButton(new ImageIcon(getIcon("check.png")));
		check.setToolTipText("Check all prices");
		check.setFocusPainted(false);
		check.addActionListener(this::checkButtonClicked);
		panel.add(check);

		/** Button for adding an item */
		JButton add = new JButton(new ImageIcon(getIcon("add.png")));
		add.setToolTipText("Add an item");
		add.setFocusPainted(false);
		add.addActionListener(this::addItemClicked);
		panel.add(add);

		panel.add(new JLabel("    "));

		/** Button for checking the price of selected item */
		JButton check_green = new JButton(new ImageIcon(getIcon("check_green.png")));
		check_green.setToolTipText("Check price of selected item");
		check_green.setFocusPainted(false);
		check_green.addActionListener(this::refreshButtonClicked);
		panel.add(check_green);

		/** Button for launching URL of selected item */
		JButton search_web = new JButton(new ImageIcon(getIcon("document.png")));
		search_web.setToolTipText("View web page of selected item");
		search_web.setFocusPainted(false);
		search_web.addActionListener(this::searchWebClicked);
		panel.add(search_web);

		/** Button for editing a selected item */
		JButton edit = new JButton(new ImageIcon(getIcon("edit.png")));
		edit.setToolTipText("Edit selected item");
		edit.setFocusPainted(false);
		edit.addActionListener(this::editButtonClicked);
		panel.add(edit);

		/** Button for adding an item */
		JButton delete = new JButton(new ImageIcon(getIcon("remove.png")));
		delete.setToolTipText("Delete selected item");
		delete.setFocusPainted(false);
		delete.addActionListener(this::deleteButtonClicked);
		panel.add(delete);

		return panel;
	}

	private JToolBar makeControlPanelRight() {
		JToolBar panel = new JToolBar();
		panel.setFloatable(false);
		JButton about = new JButton(new ImageIcon(getIcon("about.png")));
		about.setToolTipText("Delete selected item");
		about.setFocusPainted(false);
		about.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(panel, "   Price Watcher, version 1.0", "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		panel.add(about);

		return panel;
	}

	/**
	 * Show briefly the given string in the message bar.
	 * 
	 * @param msg message to be displayed in south border panel
	 */
	private void showMessage(String msg) {
		msgBar.setText(msg);
		new Thread(() -> {
			try {
				Thread.sleep(3 * 1000); // 3 seconds
			} catch (InterruptedException e) {
			}
			if (msg.equals(msgBar.getText())) {
				SwingUtilities.invokeLater(() -> msgBar.setText(" "));
			}
		}).start();
	}

	/**************************** Action Listeners **********************/
	/**
	 * Action Listener used to update all the prices of all of the items in the
	 * JList
	 * 
	 * @param event check button is clicked
	 */
	private void checkButtonClicked(ActionEvent event) {
		for (int i = 0; i < itemList.sizeOfList(); i++) {
			itemList.get(i).update();
			list.repaint();
		}
		showMessage("Price has updated successfully");
	}

	/**
	 * Action Listener created to detect when the add item button is clicked
	 * 
	 * @param event add button is clicked
	 */
	private void addItemClicked(ActionEvent event) {
		JDialog addItem = new JDialog(this, "Add");

		JPanel panel = new JPanel();

		JTextField nameField = new JTextField(20);
		JTextField urlField = new JTextField(20);

		JLabel name = new JLabel("Name: ");
		JLabel url = new JLabel("URL:     ");

		JButton addButton = new JButton("Add");
		JButton cancelButton = new JButton("Cancel");

		panel.setLayout(new FlowLayout());

		name.setLabelFor(nameField);
		url.setLabelFor(urlField);

		panel.add(name);
		panel.add(nameField);

		panel.add(url);
		panel.add(urlField);
		panel.add(addButton);
		panel.add(cancelButton);

		panel.setSize(300, 125);
		addItem.setSize(300, 125);
		addItem.add(panel);

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String nameString = nameField.getText();
				String urlString = urlField.getText();

				Item updatedItem = new Item(nameString, urlString);
				if (updatedItem.getCurrentPrice() <= 0) {
					addItem.dispose();
					invalidUrlDialog();
					addItem.dispose();
				}

				if (updatedItem.getCurrentPrice() > 0) {
					itemList.addElement(updatedItem);
					database.addToDatabase(updatedItem);
					addItem.dispose();
				}
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addItem.dispose();
			}
		});

		addItem.setResizable(false);
		addItem.setLocationRelativeTo(null);
		addItem.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);// make the focus lock in frame
		addItem.setVisible(true);
		addItem.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	/**
	 * Updates the current selected Item in the list
	 * 
	 * @param event event when refresh button is clicked
	 */
	private void refreshButtonClicked(ActionEvent event) {
		/**
		 * Send the index of the current selected item in the list Send the instance
		 * (Object) of the current item at that index The item will be updated and set
		 * again in the same index
		 */
		try {
			itemList.updateSelected(list.getSelectedIndex(),
					(Item) list.getModel().getElementAt(list.getSelectedIndex()));
		} catch (ArrayIndexOutOfBoundsException e) {
			showMessage("ERROR: No item is selected");
		}
	}

	/**
	 * Event that visits the web page of the selected item in the list
	 * 
	 * @param event event when search web button is clicked
	 */
	private void searchWebClicked(ActionEvent event) {
		try {
			Item item_holder = (Item) list.getModel().getElementAt(list.getSelectedIndex()); // get the item at the
																								// current
			// item
			// that's selected in the
			// JList
			viewPageClicked(item_holder);
		} catch (ArrayIndexOutOfBoundsException e) {
			showMessage("ERROR: Make sure to select an item in order to launch the URL");
		}
	}

	/**
	 * editButtonClicked is a method that let's the user edit the URL or NAME of the
	 * item that is currently selected
	 * 
	 * @param event event when edit button is clicked
	 */
	private void editButtonClicked(ActionEvent event) {
		// Dialog
		JDialog addItem = new JDialog(this, "Edit Item");
		// Panel
		JPanel panel = new JPanel();

		// TextField
		JTextField nameField = new JTextField(20);
		JTextField urlField = new JTextField(20);

		// Label
		JLabel name = new JLabel("Name: ");
		JLabel url = new JLabel("URL:     ");

		// Button
		JButton editButton = new JButton("Edit");
		JButton cancelButton = new JButton("Cancel");

		panel.setLayout(new FlowLayout());

		name.setLabelFor(nameField);
		url.setLabelFor(urlField);

		panel.add(name);
		panel.add(nameField);
		panel.add(url);
		panel.add(urlField);
		panel.add(editButton);
		panel.add(cancelButton);

		panel.setSize(300, 120);
		addItem.setSize(300, 120);
		addItem.add(panel);

		// actionListeners for button
		editButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String nameString = nameField.getText();
				String urlString = urlField.getText();

				Item updatedItem = new Item(nameString, urlString);
				if (updatedItem.getCurrentPrice() <= 0) {
					addItem.dispose();
					invalidUrlDialog();
					addItem.dispose();
				}

				if (updatedItem.getCurrentPrice() > 0) {
					int index = list.getSelectedIndex();
					updatedItem = (Item) list.getModel().getElementAt(index);
					updatedItem.seturl(urlString);
					updatedItem.setName(nameString);
					updatedItem.update();

					database.edit(updatedItem, index);
					addItem.dispose();
					list.repaint();
					
					try {
						Thread.sleep(1000);
						bar.removeAll();
					} catch (InterruptedException e1) {
					}

				}
			}
		});

		// actionListener for cancelButton
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addItem.dispose();
			}
		});

		addItem.setResizable(false);
		addItem.setLocationRelativeTo(null);
		addItem.setModalityType(Dialog.ModalityType.TOOLKIT_MODAL);// make the focus lock in frame
		addItem.setVisible(true);
		addItem.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}

	/**
	 * deleteButtonClicked is a method that deletes the selected item from the list
	 * 
	 * @param event event when delete button is clicked
	 * 
	 */
	private void deleteButtonClicked(ActionEvent event) {
		try {
			itemList.removeItem(list.getSelectedIndex());
			database.delete(itemList);
		} catch (ArrayIndexOutOfBoundsException ee) {
			showMessage("ERROR: Please select and Item to delete");
		}

	}

	/**
	 * Menu bar creation and implementation of all listeners
	 * 
	 * @return menuBar returns the UI to be displayed in the top menu bar
	 */
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar(); // menu bar
		/********** Menu Bar different sections ********/
		JMenu appMenu = new JMenu("App");
		JMenu itemMenu = new JMenu("Item");

		menuBar.add(appMenu);
		menuBar.add(itemMenu);
		menuBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		/************* appMenu tab different selections ****************/
		JMenuItem aboutPriceWatcher = new JMenuItem("About");
		aboutPriceWatcher.setToolTipText("Show App information");

		JMenuItem exit = new JMenuItem("Exit");

		aboutPriceWatcher.setIcon(new ImageIcon(getIcon("about.png")));

		// information of app displayed
		aboutPriceWatcher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// SHOW APP INFORMATION
				JOptionPane.showMessageDialog(Main.this, "   Price Watcher, version 1.0", "About",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		appMenu.add(aboutPriceWatcher);
		appMenu.add(exit);

		aboutPriceWatcher.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
		/************* itemMenu tab different selections ****************/
		JMenuItem checkPrices = new JMenuItem("Check Prices");
		checkPrices.setToolTipText("Check all prices");
		checkPrices.addActionListener(this::checkButtonClicked);

		JMenuItem addItem = new JMenuItem("Add Item");
		addItem.addActionListener(this::addItemClicked);
		addItem.setToolTipText("Add an item");

		JMenu selected = new JMenu("Selected");// selected tab is drop down menu

		/** add icons and set their icon gap so they can be align */
		checkPrices.setIcon(new ImageIcon(getIcon("check.png")));
		checkPrices.setIconTextGap(0);

		addItem.setIcon(new ImageIcon(getIcon("add.png")));
		addItem.setIconTextGap(2);

		itemMenu.add(checkPrices);
		itemMenu.add(addItem);
		addItem.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

		/** set the 'selected' dropMenu extension details */
		JMenuItem checkPriceSelected = new JMenuItem("Price");
		checkPriceSelected.addActionListener(this::refreshButtonClicked);
		checkPriceSelected.setToolTipText("Check price of selected item");

		JMenuItem viewUrlSelected = new JMenuItem("View");
		viewUrlSelected.addActionListener(this::searchWebClicked);
		viewUrlSelected.setToolTipText("View web page of selected item");

		JMenuItem editItemSelected = new JMenuItem("Edit");
		editItemSelected.addActionListener(this::editButtonClicked);
		editItemSelected.setToolTipText("Edit selected item");

		JMenuItem removeItemSelected = new JMenuItem("Remove");
		removeItemSelected.addActionListener(this::deleteButtonClicked);
		removeItemSelected.setToolTipText("Delete selected item");

		/** setIcon and boundaries for "selected" drop menu */
		checkPriceSelected.setIcon(new ImageIcon(getIcon("check_green.png")));

		viewUrlSelected.setIcon(new ImageIcon(getIcon("document.png")));
		viewUrlSelected.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		editItemSelected.setIcon(new ImageIcon(getIcon("edit.png")));
		editItemSelected.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		removeItemSelected.setIcon(new ImageIcon(getIcon("remove.png")));
		removeItemSelected.setIconTextGap(3);
		removeItemSelected.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		selected.add(checkPriceSelected);
		selected.add(viewUrlSelected);
		selected.add(editItemSelected);
		selected.add(removeItemSelected);
		// add the final selected Menu to the Main Menu
		itemMenu.add(selected);

		/** Setting Mnemonics and Accelerators */
		// appMenu JMenu Mnemonics and Accelerators
		exit.setMnemonic(KeyEvent.VK_X);
		exit.addActionListener(new ActionListener() { // EXIT Mnemonic action listener
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}

		});

		/** itemMenu JMenu Mnemonics and Accelerators */
		itemMenu.setMnemonic(KeyEvent.VK_I);

		checkPrices.setMnemonic(KeyEvent.VK_C);
		checkPrices.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
		checkPrices.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		addItem.setMnemonic(KeyEvent.VK_A);
		addItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
		addItem.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		/** selected JMenu Mnemonics and Accelerators */
		checkPriceSelected.setMnemonic(KeyEvent.VK_P);
		checkPriceSelected.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));

		viewUrlSelected.setMnemonic(KeyEvent.VK_V);
		viewUrlSelected.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));

		editItemSelected.setMnemonic(KeyEvent.VK_E);
		editItemSelected.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));

		removeItemSelected.setMnemonic(KeyEvent.VK_R);
		removeItemSelected.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
		return menuBar;
	}

	/** Main class the initiates the instance of the Price Watcher application */
	public static void main(String[] args) {
		new Main();
	}

}
