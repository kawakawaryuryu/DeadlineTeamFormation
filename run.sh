#! /bin/sh
memory=4096
java -Xms${memory}m -Xmx${memory}m -classpath 'jar/*:bin' main.Main debug 1 learning estimation BTFM/ED &
java -Xms${memory}m -Xmx${memory}m -classpath 'jar/*:bin' main.Main debug 2 noLearning estimation ED &
java -Xms${memory}m -Xmx${memory}m -classpath 'jar/*:bin' main.Main debug 3 learning noEstimation BTFM &
java -Xms${memory}m -Xmx${memory}m -classpath 'jar/*:bin' main.Main debug 4 random noEstimation Random &
