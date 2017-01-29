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
        started = _started;
    }

    void setEnds(String _ends) {
        ends = _ends;
    }

    void setSeller(String _seller) {
        seller = _seller;
    }

    void setDescription(String _description) {
        description = _description;
    }
}

class Bid {
    String itemID;
    String userID;
    String time;
    String amount;

    void setItemID(String _itemID) {
        itemID = _itemID;
    }

    void setUID(String _userID) {
        userID = _userID;
    }

    void setTime(String _time) {
        SimpleDateFormat input_sdf = new SimpleDateFormat("MMM-dd-yyyy HH:mm:ss");
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
}

class User {
    String location; 
    String country;
    String bidderRating;
    String sellerRating;


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
}