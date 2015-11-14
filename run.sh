#! /bin/sh
java -Xms4096m -Xmx4096m -classpath bin fixed.main.MainMain debug 1 learning estimation &
java -Xms4096m -Xmx4096m -classpath bin fixed.main.MainMain debug 2 noLearning estimation &
java -Xms4096m -Xmx4096m -classpath bin fixed.main.MainMain debug 3 learning noEstimation &
java -Xms4096m -Xmx4096m -classpath bin fixed.main.MainMain debug 4 random noEstimation &
