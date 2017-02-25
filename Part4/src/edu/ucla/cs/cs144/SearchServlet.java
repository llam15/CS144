package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        int NUM_PER_PAGE = 20;
        String searchParams = "";
        String searchParamURI = "";
        int page = 0;

        // Set page title
        String pageTitle = "eBay Search";
        request.setAttribute("title", pageTitle);

        // Parse parameters
        Enumeration paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = (String) paramNames.nextElement();

            // s = search params. concatenate into one string.
            if (name.equals("s")) {
                String[] params = request.getParameterValues(name);
                for (int i = 0; i < params.length; i++) {
                    searchParams += params[i];
                    searchParamURI += params[i];
                    if (i != params.length - 1) {
                        searchParams += " ";
                        searchParamURI += "+";
                    }
                }
            }

            // page = page number. Set page number.
            if (name.equals("page")) {
                String pageParam = request.getParameter(name);
                page = Integer.parseInt(pageParam);
            }
        }
        request.setAttribute("searchParamString", searchParams);
        request.setAttribute("searchParamURI", searchParamURI);
        request.setAttribute("page", page);

        if (searchParams != "") {
            // If search parameters exist, then find results
            AuctionSearch as = new AuctionSearch();
            SearchResult[] results = as.basicSearch(searchParams, page*NUM_PER_PAGE, NUM_PER_PAGE);
            request.setAttribute("results", results);
        }

        // Render the page
        request.getRequestDispatcher("/search.jsp").forward(request, response);
    }
}
