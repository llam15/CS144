CREATE TABLE Items(
	item_id INT NOT NULL,
	name VARCHAR(200),
	currently  DECIMAL(8,2),
	buy_price  DECIMAL(8,2),
	first_bid  DECIMAL(8,2),
	num_bids INT,
	latitude VARCHAR(20),
	longitude VARCHAR(20),
	location VARCHAR(200),
	country VARCHAR(200),
	started TIMESTAMP,
	ends TIMESTAMP,
	seller_name VARCHAR(200),
	description VARCHAR(4000),
	PRIMARY KEY (item_id)
);

CREATE TABLE Item_Category(
	item_id INT NOT NULL,
	category VARCHAR(200),
	FOREIGN KEY (item_id) REFERENCES Items(item_id),
	PRIMARY KEY(item_id,category)
);

CREATE TABLE Users(
	uid VARCHAR(200) NOT NULL,
	location VARCHAR(4000),
	country VARCHAR(200),
	bidder_rating INT,
	seller_rating INT,
	PRIMARY KEY (uid)
);

CREATE TABLE Bids(
	item_id INT NOT NULL,
	uid VARCHAR(200) NOT NULL,
	time TIMESTAMP,
	amount DECIMAL(8,2),
	FOREIGN KEY(item_id) REFERENCES Items(item_id),
	FOREIGN KEY(uid) REFERENCES Users(uid),
	PRIMARY KEY(item_id,uid,time)
);