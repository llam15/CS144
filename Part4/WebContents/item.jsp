<%@ page import="java.util.*" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%
    String itemID = (String) request.getParameter("id");
    String itemXML = (String) request.getAttribute("itemXML");
%>
<html>
    <head>
        <title><%= request.getAttribute("title") %></title>
    </head>
    <body>
        <a href="/eBay">Home</a>

        <% if (itemXML != null && itemXML != "") { %>
            <x:parse xml="${itemXML}" var="output"/>

            <table>
                <tr>
                    <td>Item ID</td>
                    <td><%= itemID %></td>
                </tr>
                <tr>
                    <td>Item Name</td>
                    <td><x:out select="$output/Item/Name" /></td>
                </tr>
                <tr>
                    <td>Item Categories</td>
                    <td>
                        <x:forEach select="$output/Item/Category" var="category">
                           <x:out select="$category" /><br>
                        </x:forEach>
                    </td>
                </tr>
                <tr>
                    <td>Currently</td>
                    <td>
                        <x:out select="$output/Item/Currently" />
                    </td>
                </tr>
                <tr>
                    <td>Buy Price</td>
                    <td>
                        <x:choose>
                            <x:when select="$output/Item/Buy_Price">
                                <x:out select="$output/Item/Buy_Price" />
                            </x:when>
                            <x:otherwise>N/A</x:otherwise>
                        </x:choose>
                    </td>
                </tr>
                <tr>
                    <td>First Bid</td>
                    <td>
                        <x:out select="$output/Item/First_Bid" />
                    </td>
                </tr>
                <tr>
                    <td>Number of Bids</td>
                    <td>
                        <x:out select="$output/Item/Number_of_Bids" />
                    </td>
                </tr>
                <tr>
                    <td>Location</td>
                    <td>
                        <x:out select="$output/Item/Location" />
                        <x:if select="$output/Item/Location/@Latitude">
                            (<x:out select="$output/Item/Location/@Latitude/" />, <x:out select="$output/Item/Location/@Longitude/" />)
                        </x:if>
                    </td>
                </tr>
                <tr>
                    <td>Country</td>
                    <td>
                        <x:out select="$output/Item/Country" />
                    </td>
                </tr>
                <tr>
                    <td>Started</td>
                    <td>
                        <x:out select="$output/Item/Started" />
                    </td>
                </tr>
                <tr>
                    <td>Ends</td>
                    <td>
                        <x:out select="$output/Item/Ends" />
                    </td>
                </tr>
                <tr>
                    <td>Seller</td>
                    <td>
                        <x:out select="$output/Item/Seller/@UserID" /> (<x:out select="$output/Item/Seller/@Rating" />)
                    </td>
                </tr>
                <tr>
                    <td>Description</td>
                    <td>
                        <x:out select="$output/Item/Description" />
                    </td>
                </tr>
            </table>
        <% } else { %>
            <h2>Invalid id: <%= itemID %></h2>
        <% } %>
        <!-- TODO: Add Table for bids -->
    </body>
</html>