#! /bin/sh
memory=4096
java -Xms${memory}m -Xmx${memory}m -classpath 'jar/*:bin' main.Main debug 1 Structured learning estimation BTFM/ED &
java -Xms${memory}m -Xmx${memory}m -classpath 'jar/*:bin' main.Main debug 2 Structured noLearning estimation ED &
java -Xms${memory}m -Xmx${memory}m -classpath 'jar/*:bin' main.Main debug 3 Structured learning noEstimation BTFM &
java -Xms${memory}m -Xmx${memory}m -classpath 'jar/*:bin' main.Main debug 4 Structured random noEstimation Random &
