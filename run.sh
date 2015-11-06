#! /bin/sh
java -Xms4096m -Xmx4096m -classpath bin fixed.main.MainMain 1 learning estimation &
java -Xms4096m -Xmx4096m -classpath bin fixed.main.MainMain 2 noLearning estimation &
java -Xms4096m -Xmx4096m -classpath bin fixed.main.MainMain 3 learning noEstimation &
java -Xms4096m -Xmx4096m -classpath bin fixed.main.MainMain 4 random noEstimation &
