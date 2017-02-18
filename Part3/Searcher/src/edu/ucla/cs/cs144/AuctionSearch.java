package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
     * You will probably have to use JDBC to access MySQL data
     * Lucene IndexSearcher class to lookup Lucene index.
     * Read the corresponding tutorial to learn about how to use these.
     *
     * You may create helper functions or classes to simplify writing these
     * methods. Make sure that your helper functions are not public,
     * so that they are not exposed to outside of this class.
     *
     * Any new classes that you create should be part of
     * edu.ucla.cs.cs144 package and their source files should be
     * placed at src/edu/ucla/cs/cs144.
     *
     */
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
        try {
            // instantiate the search engine
            SearchEngine se = new SearchEngine();

            TopDocs topDocs = se.performSearch(query, numResultsToSkip + numResultsToReturn); 

            // obtain the ScoreDoc (= documentID, relevanceScore) array from topDocs
            ScoreDoc[] hits = topDocs.scoreDocs;
			
            int total = hits.length < numResultsToSkip ? 0 : hits.length - numResultsToSkip;
            SearchResult[] results = new SearchResult[total];

            // retrieve each matching document from the ScoreDoc arry
            for (int i = numResultsToSkip; i < hits.length; i++) {
                Document doc = se.getDocument(hits[i].doc);
                String itemId = doc.get("item_id");
                String name = doc.get("name");

                results[i-numResultsToSkip] = new SearchResult(itemId, name);
            }

            return results;
        } catch (Exception ex) {
            System.out.println(ex);
        }

        return new SearchResult[0];
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		
		// 1. items should have at least one keyword in the query parameter in their title, 
		// category, or description fields
		// 2. items should be located within the rectangular region specified
		
		SearchResult[] basicQueryResult = basicSearch(query, 0, numResultsToReturn);
		ArrayList<SearchResult> spatialResults = new ArrayList<SearchResult>();
		Connection conn = null;
		int start = 0;
		int added = 0;
		int skipped = 0;
		
		
		try {
    	    conn = DbManager.getConnection(true);
			Statement stmt = conn.createStatement();
			
			String rectangle = getPolygon(region.getLx(), region.getLy(), region.getRx(), region.getRy());
			
			PreparedStatement prepareIncludedRegion = conn.prepareStatement("SELECT MBRContains(GeomFromText(" + rectangle + "), lat_long) AS inRange FROM Location WHERE item_id = ?");
						
			while(added < numResultsToReturn && basicQueryResult.length > 0){
				for(int i = 0; i < basicQueryResult.length; i++){
					prepareIncludedRegion.setString(1, basicQueryResult[i].getItemId());
					ResultSet rs = prepareIncludedRegion.executeQuery();
					if (rs.next() && rs.getBoolean("inRange")) {
						if(added < numResultsToReturn){
							if(skipped < numResultsToSkip)
								skipped++;
							else{
								spatialResults.add(basicQueryResult[i]);
								added++;
							}
						}
						else 
							break;
					}
					rs.close();
				}
				//run through basic query again to get more results
				if(added < numResultsToReturn){
					start += numResultsToReturn;
					basicQueryResult = basicSearch(query, start, start+numResultsToReturn);
				}
			}
			//close all connections
			prepareIncludedRegion.close();
			conn.close();
			
			SearchResult[] results = new SearchResult[added];
			
			for(int j = 0; j < added; j++){
				results[j] = spatialResults.get(j);
			}
			
			return results;
			
    	} catch (SQLException ex) {
    	    System.out.println(ex);
    	}
		
		//if there are no hits
		return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
        try {
            conn = DbManager.getConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        try {
            // Index all items
            Statement stmt = conn.createStatement();

            // Prepared Statement to select item categories
            PreparedStatement itemQuery = conn.prepareStatement(
                "SELECT Items.*, Users.seller_rating " +
                "FROM Items " +
                "LEFT JOIN Users ON Items.seller_name = Users.uid " +
                "WHERE item_id = ?"
            );

            // Get item
            itemQuery.setString(1, itemId);
            ResultSet item = itemQuery.executeQuery();

            if (item.next()) {
                // Prepared Statement to select item categories
                PreparedStatement categoriesQuery = conn.prepareStatement("SELECT category FROM Item_Category WHERE item_id = ?");

                // Prepared Statement to select item bids
                PreparedStatement bidsQuery = conn.prepareStatement(
                    "SELECT Bids.*, Users.location, Users.country, Users.bidder_rating " +
                    "FROM Bids " +
                    "JOIN Users " +
                    "ON Bids.uid = Users.uid " +
                    "WHERE item_id = ? " +
                    "ORDER BY Bids.time ASC"
                );

                // Get item categories
                categoriesQuery.setString(1, itemId);
                ResultSet categoryResults = categoriesQuery.executeQuery();

                // Get item bids
                bidsQuery.setString(1, itemId);
                ResultSet bidResults = bidsQuery.executeQuery();

                return generateItemXML(item, categoryResults, bidResults);
            }

        } catch (Exception ex) {
            System.out.println(ex);
        }

        // close the database connection
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }

		return "";
	}

    private String generateItemXML(ResultSet item, ResultSet categories, ResultSet bids) {
        String itemXML = "";
        try {
            String item_id = item.getString("item_id");
            String name = escapeXML(item.getString("name"));
            String currently = item.getString("currently");
            String buy_price = item.getString("buy_price");
            String first_bid = item.getString("first_bid");
            int num_bids = item.getInt("num_bids");
            String latitude = item.getString("latitude");
            String longitude = item.getString("longitude");
            String location = escapeXML(item.getString("location"));
            String country = escapeXML(item.getString("country"));
            String started = getXMLDate(item.getString("started"));
            String ends = getXMLDate(item.getString("ends"));
            String seller_name = escapeXML(item.getString("seller_name"));
            String seller_rating = item.getString("seller_rating");
            String description = escapeXML(item.getString("description"));

            itemXML += "<Item ItemID=\"" + item_id + "\">\n";
            itemXML += "<Name>" + name + "</Name>\n";

            while (categories.next()) {
                itemXML += "<Category>" + escapeXML(categories.getString("category")) + "</Category>\n";
            }

            itemXML += "<Currently>$" + currently + "</Currently>\n";

            if (buy_price != null) {
                itemXML += "<Buy_Price>$" + buy_price + "</Buy_Price>\n";
            }

            itemXML += "<First_Bid>$" + first_bid + "<First_Bid>\n";
            itemXML += "<Number_of_Bids>" + num_bids + "<Number_of_Bids>\n";

            if (num_bids != 0) {
                itemXML += "<Bids>\n";
                while (bids.next()) {
                    String rating = bids.getString("bidder_rating");
                    String bidder = escapeXML(bids.getString("uid"));
                    String bidder_location = escapeXML(bids.getString("location"));
                    String bidder_country = escapeXML(bids.getString("country"));
                    String bid_time = escapeXML(getXMLDate(bids.getString("time")));
                    String bid_amount = escapeXML(bids.getString("amount"));
                    itemXML += "<Bid>\n";
                    itemXML += "<Bidder Rating=\"" + rating + "\" UserID=\"" +  bidder +"\">\n";
                    itemXML += "<Location>" + bidder_location + "</Location>\n";
                    itemXML += "<Country>" + bidder_country + "</Country>\n";
                    itemXML += "<Time>" + bid_time + "</Time>\n";
                    itemXML += "<Amount>$" + bid_amount + "</Amount>\n";
                    itemXML+= "</Bid>\n";
                }
                itemXML += "</Bids>\n";
            } else {
                itemXML += "<Bids />\n";
            }


            if (latitude != null && longitude != null) {
                itemXML += "<Location Latitude=\"" + latitude + "\" Longitude=\"" + longitude + "\">" + location +"</Location>\n";
            } else {
                itemXML += "<Location>" + location +"</Location>\n";
            }

            itemXML += "<Country>" + country +"</Country>\n";
            itemXML += "<Started>" + started +"</Started>\n";
            itemXML += "<Ends>" + ends +"</Ends>\n";
            itemXML += "<Seller Rating=\"" + seller_rating + "\" UserID=\"" + seller_name +"\" />\n";
            itemXML += "<Ends>" + ends +"</Ends>\n";
            if (description != null && !description.isEmpty()) {
                itemXML += "<Description>" + description + "</Description>\n";
            } else {
                itemXML += "<Description />\n";
            }
            itemXML += "</Item>";

        } catch (Exception ex) {
            System.out.println(ex);
        }

        return itemXML;
    }

    private String escapeXML(String raw) {
        if (raw == null || raw.isEmpty()) {
            return raw;
        }

        String escaped = raw.replaceAll("<", "&lt;");
        escaped = escaped.replaceAll(">", "&gt;");
        escaped = escaped.replaceAll("&", "&amp;");
        escaped = escaped.replaceAll("\"", "&quot;");
        escaped = escaped.replaceAll("'", "&apos;");

        return escaped;
    }

    private String getXMLDate(String date) {
        String XMLDate = "";

        SimpleDateFormat input_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output_sdf = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");

        try {
            Date timestamp = input_sdf.parse(date);
            XMLDate = output_sdf.format(timestamp).toString();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return XMLDate;
    }
	
	public String echo(String message) {
		return message;
	}
	
	private String getPolygon(double lx, double ly, double rx, double ry){
		return "'POLYGON((" + lx + " " + ly + ", " + lx + " " + ry + ", " + rx + " "  + ry + ", " + rx + " " + ly + ", " + lx + " " + ly + "))'";
	}
	
	

}
