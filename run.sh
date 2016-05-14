#! /bin/sh
memory=4096
java -Xms${memory}m -Xmx${memory}m -classpath bin main.Main debug 1 learning estimation &
java -Xms${memory}m -Xmx${memory}m -classpath bin main.Main debug 2 noLearning estimation &
java -Xms${memory}m -Xmx${memory}m -classpath bin main.Main debug 3 learning noEstimation &
java -Xms${memory}m -Xmx${memory}m -classpath bin main.Main debug 4 random noEstimation &
