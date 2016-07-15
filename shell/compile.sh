#!/bin/sh

compile() {
	for file in $1/*
	do
		if [ -d $file ]; then
			[[ $file = */test ]] && continue 
			compile $file
		elif [ -f $file ]; then
			[[ $file = *.java ]] && javac -classpath bin -d bin $file
		fi
	done
}

compile "$PWD/../src"
