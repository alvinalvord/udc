@echo off

javac -d bin *.java -Xlint:all -Xdiags:verbose

java -cp .\bin Driver