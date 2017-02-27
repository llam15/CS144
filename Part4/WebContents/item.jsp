<%@ page import="java.util.*" %>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%
    String itemID = (String) request.getParameter("id");
    String itemXML = (String) request.getAttribute("itemXML");
%>
<html>
    <head>
        <title><%= request.getAttribute("title") %></title>
        <style>
            table {
                border-collapse: collapse;
                margin: 20px 0;
            }

            th, td {
                padding: 8px;
                text-align: left;
                border-bottom: 1px solid #ddd;
                border-top: 1px solid #ddd;
            }

            #item-table td:nth-child(1) {
                font-weight: bold;
                background-color: #eee;
            }

            #bid-table th {
                background-color: #eee;
            }

            #bid-table tr:nth-child(odd) {
                background-color: #eef;
            }

        </style>
    </head>
    <body>
        <a href="/eBay">Home</a>

        <% if (itemXML != null && itemXML != "") { %>
            <x:parse xml="${itemXML}" var="output"/>

            <!-- Item Information -->
            <table id="item-table">
                <tr>
                    <td >Item ID</td>
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
                            (<x:out select="$output/Item/Location/@Latitude" />, <x:out select="$output/Item/Location/@Longitude" />)
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
                        <x:out select="$output/Item/Seller/@UserID" /> (Rating: <x:out select="$output/Item/Seller/@Rating" />)
                    </td>
                </tr>
                <tr>
                    <td>Description</td>
                    <td>
                        <x:out select="$output/Item/Description" />
                    </td>
                </tr>
            </table>

            <!-- Bids -->
            <x:if select="$output/Item/Number_of_Bids != 0">
                <table id="bid-table">
                    <tr>
                        <th>Bidder</th>
                        <th>Bidder Location</th>
                        <th>Time</th>
                        <th>Amount</th>
                    </tr>
                    <x:forEach select="$output/Item/Bids/Bid" var="bid">
                        <tr>
                            <td>
                                <x:out select="$bid/Bidder/@UserID" /> (Rating: <x:out select="$bid/Bidder/@Rating" />)
                            </td>
                            <td>
                                <x:choose>
                                    <x:when select="$bid/Bidder/Location">
                                        <x:out select="$bid/Bidder/Location" />
                                    </x:when>
                                    <x:otherwise>Location: N/A</x:otherwise>
                                </x:choose>
                                <br>
                                <x:choose>
                                    <x:when select="$bid/Bidder/Country">
                                        <x:out select="$bid/Bidder/Country" />
                                    </x:when>
                                    <x:otherwise>Country: N/A</x:otherwise>
                                </x:choose>
                            </td>
                            <td>
                                <x:out select="$bid/Time" />
                            </td>
                            <td>
                                <x:out select="$bid/Amount" />
                            </td>
                        </tr>
                    </x:forEach>
                </table>
            </x:if>
        <% } else { %>
            <h2>Invalid id: <%= itemID %></h2>
        <% } %>
    </body>
</html>