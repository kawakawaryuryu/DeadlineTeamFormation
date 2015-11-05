#! /bin/sh
java -Xms4096m -Xmx4096m -classpath bin fixed.main.MainMain 1000 learning estimation &
java -Xms4096m -Xmx4096m -classpath bin fixed.main.MainMain 1001 noLearning estimation &
java -Xms4096m -Xmx4096m -classpath bin fixed.main.MainMain 1002 learning noEstimation &
java -Xms4096m -Xmx4096m -classpath bin fixed.main.MainMain 1003 random noEstimation &
