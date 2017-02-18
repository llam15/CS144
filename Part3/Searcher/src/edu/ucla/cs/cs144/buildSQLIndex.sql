-- create a table that contains information on items' latitude & longitude using the MyISAM storage engine
-- table should contain (itemId, latitude, longitude) of each item where (latitude, longitude) is stored as 
-- a single column of POINT type so that a spatial index can be created on the column

-- (1) create table using MyISAM storage engine
-- (2) populate table w/ itemID, lat, long information
-- (3) create a spatial index on lat and long

-- use POINT() function in MySQL to covert a pair of numberic values to a POINT

CREATE TABLE Location(
	item_id INT NOT NULL,
	lat_long POINT NOT NULL,
	SPATIAL INDEX (lat_long),
	FOREIGN KEY (item_id) REFERENCES Items(item_id),
	PRIMARY KEY (item_id),
) ENGINE=MyISAM;

INSERT INTO Location 
SELECT item_id, Point(latitude, longitude)
FROM Items 
WHERE latitude IS NOT NULL AND longitude IS NOT NULL;

-- to query:
-- SET @rec = 'Polygon((LowerLeft LowerRight UpperRight UpperLeft LowerLeft))';
-- SELECT fid,AsText(loc) FROM location WHERE MBRContains(GeomFromText(@rec), loc);

-- MBRContains(g1, g2): Returns 0 or 1 whether the bounding rec of g1 conains the min bounding rec of g2
