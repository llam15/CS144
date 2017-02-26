package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // Set page title
        request.setAttribute("title", "View Item");

        String itemXML = "";
        String idParam = request.getParameter("id");

        if (idParam != null && idParam != "") {
            AuctionSearch as = new AuctionSearch();
            itemXML = as.getXMLDataForItemId(idParam);
        }

        request.setAttribute("itemXML", itemXML);
        // Render the page
        request.getRequestDispatcher("/item.jsp").forward(request, response);
    }
}
