#! /bin/sh
memory=1024
java -Xms${memory}m -Xmx${memory}m -classpath bin fixed.main.MainMain debug 1 learning estimation &
java -Xms${memory}m -Xmx${memory}m -classpath bin fixed.main.MainMain debug 2 noLearning estimation &
java -Xms${memory}m -Xmx${memory}m -classpath bin fixed.main.MainMain debug 3 learning noEstimation &
java -Xms${memory}m -Xmx${memory}m -classpath bin fixed.main.MainMain debug 4 random noEstimation &
