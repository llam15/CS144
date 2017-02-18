package edu.ucla.cs.cs144;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class Item {
    String name;
    String currently;
    String buyPrice;
    String firstBid;
    String numBids;
    String latitude;
    String longitude;
    String location;
    String country;
    String started;
    String ends;
    String seller;
    String description;
	
	public Item(){
		name = "\\N";
		currently = "\\N";
		buyPrice = "\\N";
		firstBid = "\\N";
		numBids = "\\N";
		latitude = "\\N";
		longitude = "\\N";
		location = "\\N";
		country = "\\N";
		started = "\\N";
		ends = "\\N";
		seller = "\\N";
		description = "\\N";
	}

    void setName(String _name) {
        name = _name;
    }

    void setCurrently(String _currently) {
        currently = _currently;
    }

    void setBuyPrice(String _buyPrice) {
        buyPrice = _buyPrice;
    }

    void setFirstBid(String _firstBid) {
        firstBid = _firstBid;
    }

    void setNumBids(String _numBids) {
        numBids = _numBids;
    }

    void setLocation(String _location) {
        location = _location;
    }

    void setLatitude(String _latitude) {
        latitude = _latitude;
    }

    void setLongitude(String _longitude) {
        longitude = _longitude;
    }

    void setCountry(String _country) {
        country = _country;
    }

    void setStarted(String _started) {
        SimpleDateFormat input_sdf = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat output_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date timestamp = input_sdf.parse(_started);
            started = output_sdf.format(timestamp).toString();
        }
        catch(ParseException pe) {
            System.out.println(pe);
        }
    }

    void setEnds(String _ends) {
        SimpleDateFormat input_sdf = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat output_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date timestamp = input_sdf.parse(_ends);
            ends = output_sdf.format(timestamp).toString();
        }
        catch(ParseException pe) {
            System.out.println(pe);
        }
    }

    void setSeller(String _seller) {
        seller = _seller;
    }

    void setDescription(String _description) {
        description = _description.substring(0, Math.min(_description.length(), 4000));;
    }

    public String toString() { 
        return name + "|*|" + currently + "|*|" + buyPrice + "|*|" + firstBid + "|*|" +
               numBids + "|*|" + latitude + "|*|" + longitude + "|*|" + location + "|*|" +
               country + "|*|" + started + "|*|" + ends + "|*|" + seller + "|*|" + description;
    } 
}

class Bid {
    String itemID;
    String userID;
    String time;
    String amount;
	
	public Bid(){
		itemID = "\\N";
		userID = "\\N";
		time = "\\N";
		amount = "\\N";
	}

    void setItemID(String _itemID) {
        itemID = _itemID;
    }

    void setUID(String _userID) {
        userID = _userID;
    }

    void setTime(String _time) {
        SimpleDateFormat input_sdf = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat output_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date timestamp = input_sdf.parse(_time);
            time = output_sdf.format(timestamp).toString();
        }
        catch(ParseException pe) {
            System.out.println(pe);
        }
    }

    void setAmount(String _amount) {
        amount = _amount;
    }

    public String toString() {
        return itemID + "|*|" + userID + "|*|" + time + "|*|" + amount;
    }
}

class User {
    String location; 
    String country;
    String bidderRating;
    String sellerRating;
	
	public User(){
		location = "\\N"; 
		country = "\\N";
		bidderRating = "\\N";
		sellerRating = "\\N";
	}


    void setLocation(String _location) {
        location = _location;
    }
    
    void setCountry(String _country) {
        country = _country;
    }
    
    void setBidderRating(String _bidderRating) {
        bidderRating = _bidderRating;
    }
    
    void setSellerRating(String _sellerRating) {
        sellerRating = _sellerRating;
    }

    public String toString() {
        return location + "|*|" + country + "|*|" + bidderRating + "|*|" + sellerRating;
    }
}