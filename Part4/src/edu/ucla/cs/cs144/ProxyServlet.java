package edu.ucla.cs.cs144;

import java.io.IOException;
import java.net.HttpURLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.io.PrintWriter;

import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
		/* 
		i. extract the passed-in query string, 
		ii. issues a request to the Google suggest service for that query (at http://google.com/complete/search?output=toolbar&q=<your query>)
		iii. returns the results back to the original caller*/
		
		PrintWriter pw = response.getWriter();
		
		request.setAttribute("title", "Query suggestions");
		
		String queryValue = (String) request.getParameter("q");
		
		if (queryValue != null && queryValue != "") {
			//open connection & send a GET request
			URL url = new URL("http://google.com/complete/search?output=toolbar&q=" + URLEncoder.encode(queryValue, "UTF-8"));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			// read returned XML data
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuffer xmlOutput = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				xmlOutput.append(inputLine);
			}
			in.close();
			
			// print XML to page
			pw.println(xmlOutput);
		}
		pw.close();
		
		response.setContentType("text/xml");
    }
}
