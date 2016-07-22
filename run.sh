#! /bin/sh
memory=2048
new=1024
java -Xms${memory}m -Xmx${memory}m -Xmn${new}m -classpath 'jar/*:bin' main.Main debug 1 Rational learning estimation BTFM/ED &
java -Xms${memory}m -Xmx${memory}m -Xmn${new}m -classpath 'jar/*:bin' main.Main debug 2 Rational noLearning estimation ED &
java -Xms${memory}m -Xmx${memory}m -Xmn${new}m -classpath 'jar/*:bin' main.Main debug 3 Rational learning noEstimation BTFM &
java -Xms${memory}m -Xmx${memory}m -Xmn${new}m -classpath 'jar/*:bin' main.Main debug 4 Rational random noEstimation Random &
java -Xms${memory}m -Xmx${memory}m -Xmn${new}m -classpath 'jar/*:bin' main.Main debug 5 Structured learning estimation BTFM/ED &
java -Xms${memory}m -Xmx${memory}m -Xmn${new}m -classpath 'jar/*:bin' main.Main debug 6 Structured noLearning estimation ED &
java -Xms${memory}m -Xmx${memory}m -Xmn${new}m -classpath 'jar/*:bin' main.Main debug 7 Structured learning noEstimation BTFM &
