<%@ page import="edu.ucla.cs.cs144.SearchResult" %>
<%
    Integer pageNum = (Integer) request.getAttribute("page");
    String searchParamString = (String) request.getAttribute("searchParamString");
    String searchParamURI = (String) request.getAttribute("searchParamURI");
    SearchResult[] results = (SearchResult[]) request.getAttribute("results");

%>
<html>
    <head>
        <title><%= request.getAttribute("title") %></title>
		<script type="text/javascript" src="suggestionresults.js"></script>
		<script type="text/javascript" src="autosuggest.js"></script>
		<script type="text/javascript">
            window.onload = function () {
				oServerSuggestions = new ServerSuggestions();
                var oTextbox = new AutoSuggestControl(document.getElementById("search-input"), oServerSuggestions);        
            }
        </script>
		<link rel="stylesheet" type="text/css" href="autosuggest.css" />      
    </head>
    <body>
        <a href="/eBay">Home</a>
        <br>
        <form action="/eBay/search">
            Search Items:<br>
            <input id="search-input" type="text" name="s" autocomplete="off">
            <input type="submit" value="Search">
        </form>

        <% if (results != null) { %>
            <% if (results.length > 0) { %>
                <h2>Results:</h2>
                <ul>
                    <% for (int i = 0; i < results.length; i++) { 
                        SearchResult item = results[i];
                    %>
                        <li>
                            <a href="/eBay/item?id=<%= item.getItemId() %>"> <%= item.getName() %></a>
                        </li>
                    <% } %>
                </ul>

                <% if (pageNum > 0) { %>
                    <a href="/eBay/search?s=<%= searchParamURI %>&page=<%= pageNum-1 %>">Previous 20 Results</a>
                <% } else { %>
                    <a disabled>Previous 20 Results</a>
                <% } %>

                <% if (results.length == 20) { %>
                    <a href="/eBay/search?s=<%= searchParamURI %>&page=<%= pageNum+1 %>">Next 20 Results</a>
                <% } else { %>
                    <a disabled>Next 20 Results</a>
                <% } %>
            <% } else { %>
                <h2>No results found.</h2>
                <% if (pageNum > 0) { %>
                    <a href="/eBay/search?s=<%= searchParamURI %>&page=<%= pageNum-1 %>">Previous 20 Results</a>
                <% } else { %>
                    <a disabled>Previous 20 Results</a>
                <% } %>
                <a disabled>Next 20 Results</a>
            <% } %>
        <% } %>

  
	</body>
</html>
