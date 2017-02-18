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
import java.sql.SQLException;
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

                results[i] = new SearchResult(itemId, name);
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
		Connection con = null;
		int start = 0;
		int added = 0;
		int skipped = 0;
		
		
		try {
    	    conn = DbManager.getConnection(true);
			Statement stmt = con.createStatement();
			
			String rectangle = getPolygon(region.getLx(), region.getLy(), region.getRx(), region.getRy());
			
			PreparedStatement prepareIncludedRegion = con.prepareStatement("SELECT MBRContains(GeomFromText(" + rectangle + "), lat_long) AS inRange FROM Location WHERE item_id = ?");
			
			while(added < numResultsToReturn && basicQueryResult.length > 0){
				for(int i = 0; i < basicQueryResult.length; i++){
					prepareIncludedRegion.setInt(1, basicQueryResult[i].getItemId());
					ResultSet rs = prepareIncludedRegion.executeQuery();
					if (rs.next() && rs.getBoolean("inRange") {
						SearchResult r = new SearchResult(rs.getString("item_id"), rs.getString("name"));
						if(added < numResultsToReturn){
							if(skipped < numResultsToSkip)
								skipped++;
							else{
								spatialResults.add(r);
								added++;
							}
						}
						else 
							break;
					}
				}
				//run through basic query again to get more results
				if(added < numResultsToReturn){
					start += numResultsToReturn;
					basicQueryResult = basicSearch(query, start, start+numResultsToReturn);
				}
			}
			//close all connections
			rs.close();
			con.close();
			
			SearchResult[] results = new SearchResult[added];
			
			for(int j = 0; j < added; j++){
				results[i] = spatialResults.get(i);
			}
			
			return results;
			
    	} catch (SQLException ex) {
    	    System.out.println(ex);
    	}
		
		//if there are no hits
		return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return "";
	}
	
	public String echo(String message) {
		return message;
	}
	
	private String getPolygon(double lx, double ly, double rx, double ry){
		return "'POLYGON((" + lx + " " + ly + ", " + lx + " " + ry + ", " + rx + " "  + ry + ", " + rx + " " + ly + ", " + lx + " " + ly + "))'";
	}
	
	

}
