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
    </head>
    <body>
        <a href="/eBay">Home</a>
        <br>
        <form action="/eBay/search">
            Search Items:<br>
            <input type="text" name="s" value="<%= searchParamString %>">
            <input type="submit" value="Search">
        </form>
		
		<script>
			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function() {
				if (this.readyState == 4 && this.status == 200) {
					
					console.log(xhttp.responseText);
				}
			};
			xhttp.open("GET", "http://localhost:1448/eBay/suggest", true);
			xhttp.send();
		</script>

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