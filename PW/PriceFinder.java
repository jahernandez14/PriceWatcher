package hw4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceFinder {
	private double price; 
	
	public PriceFinder() {
		
	}
	
	public double urlPrice(String url){
		Pattern pattern;
		
		try {
			if(url.substring(12,21).equals("homedepot")) {
				pattern = Pattern.compile("content=\"\\d+[\\.]\\d+\\d");
				price = reader(url,pattern);
				return price;
			}
			
			else if(url.substring(12,19).equals("walmart")) {
				pattern = Pattern.compile("content=\"\\d+[\\.]\\d+\\d");
				price = reader(url,pattern);
				return price;
			}
			
			else if(url.substring(12,16).equals("ebay")) {
				pattern = Pattern.compile("content=\"\\d+[\\.]\\d+\\d");
				price = reader(url,pattern);
				return price;
			}
		}
		catch (Exception e) {
			
		}
		return price;
	}
	
	public double reader(String store, Pattern pattern) throws Exception {
		
		URL url = new URL(store);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String line;
        while ((line = in.readLine()) != null) {
        	Matcher matcher = pattern.matcher(line);
        		 if (matcher.find()) {
        	            price = Double.parseDouble((line.substring(matcher.start(0),matcher.end(0))).substring(9));
        	            return price;
        		 }
        }
		in.close();
		return price;
	}
}
