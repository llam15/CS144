// Read in data file
val rawData = sc.textFile("twitter.edges")

// Split data file to ("follower", "user1,user2,user3")
val userMap = rawData.map(x => x.split(": "))

// Get all users
val users = userMap.flatMap(x => x(1).split(","))

// Top 100 users with largest follower counts
val followerCount = users.map(user => (user, 1)).reduceByKey(_+_).takeOrdered(100)(Ordering[Int].reverse.on(x => x._2.toInt))

// Convert Array back to RDD to use saveAsTextFile
sc.parallelize(followerCount).saveAsTextFile("output")

// Exit interpreter
System.exit(0)