package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }

    private IndexWriter indexWriter = null;

    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index1"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
    }

    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
    }
 
    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
    	try {
    	    conn = DbManager.getConnection(true);
    	} catch (SQLException ex) {
    	    System.out.println(ex);
    	}

        try {
            // Erase existing index
            getIndexWriter(true);
            // Index all items
            Statement stmt = conn.createStatement();
            ResultSet itemSet = stmt.executeQuery("SELECT item_id, name, description FROM Items");

            // Prepared Statement to select item categories
            PreparedStatement categoriesQuery = conn.prepareStatement("SELECT category FROM Item_Category WHERE item_id = ?");

            while (itemSet.next()) {
                indexItem(itemSet, categoriesQuery);
            }
            // Don't forget to close the index writer when done
            closeIndexWriter();
        } catch (Exception ex) {
            System.out.println(ex);
        }


        // close the database connection
    	try {
    	    conn.close();
    	} catch (SQLException ex) {
    	    System.out.println(ex);
    	}
    }    

    public void indexItem(ResultSet item, PreparedStatement categoriesQuery) {
        try {
            // Get Item attributes
            String id = item.getString("item_id");
            String name = item.getString("name");
            String description = item.getString("description");
            String categories = "";

            // Get item categories
            categoriesQuery.setString(1, id);
            ResultSet categoryResults = categoriesQuery.executeQuery();

            while (categoryResults.next()) {
                categories += categoryResults.getString("category") + " ";
            }

            // Do not erase index, just add to it
            IndexWriter writer = getIndexWriter(false);   

            // New Item
            Document doc = new Document();
            doc.add(new StringField("id", id, Field.Store.YES));
            doc.add(new StringField("name", name, Field.Store.YES));

            String fullSearchableText = id + " " + name + " " + description + " " + categories;
            doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
            writer.addDocument(doc);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
