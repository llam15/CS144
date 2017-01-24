1. Relations:     
    Items(ItemID, Name, Currently, Buy_Price, First_Bid,
        Num_Bids, Latitude, Longitude, Location, Country, Started, Ends, Seller,
        Description) [Key: ItemID]

    Item_Category(ItemID, Category) [Key: ItemID, Category]

    Bidders(UID, Location, Country, Bidder_Rating) [Key: UID]

    Sellers(UID, Seller_Rating) [Key: UID]

    Bids(ItemID, UID, Time, Amount) [Key: ItemID, UID, Time]

2. Completely Non-trivial Dependencies:
    All non-trivial dependencies specify keys in our relations

3. Yes, our relations are all BCNF because all the non-trivial dependencies are keys.

4. Yes, our relations are in 4NF.