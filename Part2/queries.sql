-- Find the number of users in the database.
SELECT COUNT(*) 
FROM Users;

-- Find the number of items in "New York", (i.e., items whose location is exactly the string "New York"). 
-- Pay special attention to case sensitivity. You should match the items in "New York" but not in "new york".
SELECT COUNT(*)
FROM Items
WHERE BINARY location='New York';

-- Find the number of auctions belonging to exactly four categories.
SELECT COUNT(*)
FROM (SELECT COUNT(*) AS cnt
		FROM Item_Category
		GROUP BY item_id
		HAVING cnt=4) as fourCategoryItems;

-- Find the ID(s) of current (unsold) auction(s) with the highest bid. Remember that the data was 
-- captured at the point in time December 20th, 2001, one second after midnight, so you can use this 
-- time point to decide which auction(s) are current. Pay special attention to the current auctions without any bid.
SELECT item_id
FROM Items
WHERE num_bids>0 
	AND ends>'2001-12-20 00:00:01' 
	AND currently=(SELECT MAX(currently) FROM Items WHERE ends>'2001-12-20 00:00:01' AND num_bids>0);

-- Find the number of sellers whose rating is higher than 1000.
SELECT COUNT(*)
FROM Users
WHERE seller_rating > 1000;

-- Find the number of users who are both sellers and bidders.
SELECT COUNT(*)
FROM Users
WHERE seller_rating IS NOT NULL AND bidder_rating IS NOT NULL;

-- Find the number of categories that include at least one item with a bid of more than $100.
SELECT COUNT(*)
FROM (SELECT COUNT(*)
		FROM Item_Category i, Bids b
		WHERE i.item_id=b.item_id AND b.amount>100
		GROUP BY i.category) AS ib;
