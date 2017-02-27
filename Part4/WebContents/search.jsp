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
            <input id="search-input" type="text" name="s"
                   value="<%= searchParamString %>"
                   onkeyup="sendAjaxRequest(this.value)" autocomplete="off">
            <input type="submit" value="Search">
        </form>
		<div id="suggestions"></div>

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

        <script type="text/javascript">
            function sendAjaxRequest(input) {
                var request = "/eBay/suggest?q="+encodeURI(input);
                var xmlHttp = new XMLHttpRequest();

                xmlHttp.open("GET", request);
                xmlHttp.onreadystatechange = function() {
                    if (xmlHttp.readyState == 4 && xmlHttp.responseXML != null) {
                        // get the CompleteSuggestion elements from the response
                        var s = xmlHttp.responseXML.getElementsByTagName('CompleteSuggestion');

                        // construct a bullet list from the suggestions
                        var htmlCode = "<ul>";
                        for (i = 0; i < s.length; i++) {
                           var text = s[i].childNodes[0].getAttribute("data");

                           htmlCode += "<li><b>" + text + "</b></li>";
                        }
                        htmlCode += "</ul>";

                        // display the list on the page
                        document.getElementById("suggestion").innerHTML = htmlCode;
                    }
                };
                xmlHttp.send(null);
            }
        </script>
    </body>
</html>
