@echo off

javac -d bin *.java -Xlint:all -Xdiags:verbose

cd bin

java Driver

cd ..

