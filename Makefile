run: program
	java -cp out Main

program:
	rm -rf out/*
	javac -d out src/*.java src/*/*.java
