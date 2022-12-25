#!

mvn clean compile package

java -jar -Dspring.profiles.active=prod target/IThoughtILearned-0.0.1-SNAPSHOT.jar
