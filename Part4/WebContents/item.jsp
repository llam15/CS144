<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String itemID = (String) request.getParameter("id");
    String itemXML = (String) request.getAttribute("itemXML");
    String latitude = "";
    String longitude = "";
%>
<html>
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
    <head>
        <title><%= request.getAttribute("title") %></title>
        <style type="text/css">
            html {
                height: 100%;
            }

            body {
                height: 100%;
                margin: 0px;
                padding: 10px;
            }

            table {
                border-collapse: collapse;
                margin: 20px 0;
                width: 80%;
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

            #map_canvas {
                height: 60%;
                width: 80%;
                margin-bottom: 10px;
            }
        </style>
    </head>
    <body>
		<div>
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
                        <x:if select="$output/Item/Location/@Latitude" >
                            <x:if select="$output/Item/Location/@Longitude">
                                <c:set var="latitude">
                                    <x:out select="$output/Item/Location/@Latitude"/>
                                </c:set>
                                <c:set var="longitude">
                                    <x:out select="$output/Item/Location/@Longitude"/>
                                </c:set>
                                <%
                                    latitude = (String) pageContext.getAttribute("latitude");
                                    longitude= (String) pageContext.getAttribute("longitude");
                                %>
                                (<x:out select="$output/Item/Location/@Latitude" />, <x:out select="$output/Item/Location/@Longitude" />)
                            </x:if>
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
		</div>

            <!-- Bids -->
		<div>
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
        </div>

        <!-- Google Maps -->
        <div id="map_canvas"></div>

        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
        <script type="text/javascript">
            document.body.onload = function() {
                var lat, lng, zoomValue;
                // Set the latitude and longitude if it exists
                <% if ((latitude != "") && (longitude != "")) { %>
                    lat = <%= latitude %>;
                    lng = <%= longitude %>;
                    zoomValue = 14;
                <% } else { %>
                    lat = 0;
                    lng = 0;
                    zoomValue = 1; // Show the whole world
                <% } %>
                var latlng = new google.maps.LatLng(lat, lng);
                var myOptions = {
                    zoom: zoomValue, // default is 8
                    center: latlng,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                };
                var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

                // Add marker if latitude and longitude are given.
                if (lat != 0 && lng != 0) {
                    var marker = new google.maps.Marker({
                        position: latlng,
                        map: map
                    });
                }
            }
        </script>
    </body>
</html>
