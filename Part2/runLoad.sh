#!/bin/bash

# Run the drop.sql batch file to drop existing tables
# Inside the drop.sql, you sould check whether the table exists. Drop them ONLY if they exists.
mysql CS144 < drop.sql

# Run the create.sql batch file to create the database and tables
mysql CS144 < create.sql

# Compile and run the parser to generate the appropriate load files
ant run-all

# If the Java code does not handle duplicate removal, do this now
uniq bids.dat > temp_bids.dat
cat temp_bids.dat > bids.dat

uniq items.dat > temp_items.dat
cat temp_items.dat > items.dat

uniq item_category.dat > temp_item_cat.dat
cat temp_item_cat.dat > item_category.dat

uniq users.dat > temp_users.dat
cat temp_users.dat > users.dat

# Run the load.sql batch file to load the data
mysql CS144 < load.sql

# Remove all temporary files
rm *.dat