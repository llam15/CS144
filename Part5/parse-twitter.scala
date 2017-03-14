// Read in data file
val rawData = sc.textFile("twitter.edges")

// Split data file to ("follower", "user1,user2,user3")
val userMap = rawData.map(x => x.split(": "))

// Get all users
val users = userMap.flatMap(x => x(1).split(","))

// save top users that have > 1000 followers to an array
val topUsers = users.map(user => (user, 1)).reduceByKey(_+_).filter(x => x._2.toInt > 1000)

//print topUsers to output
topUsers.saveAsTextFile("output")

// Exit interpreter
System.exit(0)