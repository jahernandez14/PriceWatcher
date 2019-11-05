package hw4;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

/**
 * Class created to give a visual representation of item details.
 * @author Julio Hernandez & Jonathan Argumedo & Jose Lujan
 *
 */

@SuppressWarnings("serial")
public class ItemView extends JPanel {
	protected String name = "No item added";
	protected String url= "";
	protected double initialPrice = 0.0;
	protected double currentPrice = 0.0;
	protected double change = 0.0;
	protected String date = "";
	
	/**
	 * Method that imports an item to be read by the ItemView class and populates details.
	 * @param item
	 */
	public void itemImport(Item item) {
		this.name = item.getName();
		this.url = item.geturl();
		this.date = item.getDateAdded(); 
		this.initialPrice = item.getInitialPrice();
		this.currentPrice = item.getCurrentPrice();
		this.change = item.getChange();
	}
	  
	/** Interface to notify a click on the view page icon. */
	public interface ClickListener {
		
		/** Callback to be invoked when the view page icon is clicked. */
		void clicked();
	}
	
	/** Directory for image files: src/image in Eclipse. */
	private final static String IMAGE_DIR = "/images/";
	private final static String SOUND_DIR = "/sounds/";
        
	/** View-page clicking listener. */
    private ClickListener listener;
    
    /** Create a new instance. */
    public ItemView() {
    	setPreferredSize(new Dimension(100, 160));
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            	if (isViewPageClicked(e.getX(), e.getY()) && listener != null) {
            		listener.clicked();
            	}
            }
        });
    }
        
    /** Set the view-page click listener. */
    public void setClickListener(ClickListener listener) {
    	this.listener = listener;
    }
    
    /** 
     * Overridden here to display the details of the item. 
     * this method also paints the graphical UI of the item description
     * @param g 
     */
    @Override
	public void paintComponent(Graphics g) {
        super.paintComponent(g); 
        Font initialFont = g.getFont();
        int x = 20, y = 50;
        
        g.drawImage(getIcon("document.png"), 25, 10, this);
        
        
        g.setColor(Color.BLACK);
        g.drawString("Name: ", x, y);
        g.setFont(new Font("Sans-sherif", Font.BOLD, 12));
        x += 90;
        g.drawString(name, x, y);
        g.setFont(initialFont);
        y+=20;
        x-=90;
        g.drawString("URL: ", x, y);
        x += 90;
        if(url.length()>35) {
        	g.drawString(url.substring(0,35) + "...", x, y);
        }
    	else {
    		g.drawString(url, x, y);	
        }
        y+=20;
        x-=90;
        g.drawString("Current Price: ", x, y);
        x+=90;
        g.setColor(new Color(0, 0, 250));
        g.drawString("$" + Double.toString(currentPrice), x, y);
        g.setColor(Color.BLACK);
        y+=20;
        x-=90;
        g.drawString("Change: ", x, y);
        x+=90;
        if(change < 0) {
        	g.setColor(new Color(0, 125, 50));
        	playSound("good.wav");
        	
        }
        else if(change > 0){
        	g.setColor(new Color(250, 0, 0));
        	playSound("bad.wav");
        }
        g.drawString(Double.toString(change) + "%", x, y);
        g.setColor(Color.BLACK);
        y+=20;
        x-=90;
        g.drawString("Added: ", x, y);
        x+=90;
        g.drawString(date + " ($" + Double.toString(initialPrice) +")", x, y);
   
    }
    
    /**
     * Method that indicate true or false if a given coordinate is clicked within the itemView Panel.
     * @param x coordinate
     * @param y coordinate
     * @return true or false
     */
    private boolean isViewPageClicked(int x, int y) {
    	return new Rectangle(25, 10, 30, 20).contains(x,  y);
    }
        
    /**
     * Helper method that returns and image stored from a given directory.
     * @param icon name of the image
     * @return
     */
    public Image getIcon(String icon) {
    	try {
    		URL url = new URL(getClass().getResource(IMAGE_DIR), icon);
    		return ImageIO.read(url);
    	}
    	catch (IOException e){
    		System.out.println("error reached :/");
    	}
    		return null;
    }
    
    /**
     * Helper method to play sounds from a given directory
     * @param sound
     */
    public void playSound(String sound){
        try {
        	URL url = new URL(getClass().getResource(SOUND_DIR), sound);
        	AudioInputStream in = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(in);
            clip.start();
        } 
        catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {   
        }
      
    }
    
}
