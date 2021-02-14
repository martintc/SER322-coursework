Assignment 5

Here are sample command that I used to compile and run. This is from my Makefile that I was using for testing.

buildLab:
	javac src/ser322/JdbcLab.java src/ser322/ser322/*.java -d classes/

buildLab2:
	javac src/ser322/JdbcLab2.java -d classes/

query1:
	java -cp lib/mysql-connector-java-8.0.23.jar:classes ser322.JdbcLab "jdbc:mysql://localhost:3306/movies_simple?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&&serverTimezone=America/New_York" root **** com.mysql.cj.jdbc.Driver query1

query2:
	java -cp lib/mysql-connector-java-8.0.23.jar:classes ser322.JdbcLab "jdbc:mysql://localhost:3306/movies_simple?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&&serverTimezone=America/New_York" root **** com.mysql.cj.jdbc.Driver query2 Spanish		

updateFilm:
	java -cp lib/mysql-connector-java-8.0.23.jar:classes ser322.JdbcLab "jdbc:mysql://localhost:3306/movies_simple?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&&serverTimezone=America/New_York" root **** com.mysql.cj.jdbc.Driver updateFilm 1 50

addActor:
	java -cp lib/mysql-connector-java-8.0.23.jar:classes ser322.JdbcLab "jdbc:mysql://localhost:3306/movies_simple?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&&serverTimezone=America/New_York" root **** com.mysql.cj.jdbc.Driver addActor 3 1

exportDB:
	java -cp lib/mysql-connector-java-8.0.23.jar:classes ser322.JdbcLab "jdbc:mysql://localhost:3306/movies_simple?autoReconnect=true&useSSL=false&useLegacyDatetimeCode=false&&serverTimezone=America/New_York" root **** com.mysql.cj.jdbc.Driver export test.xml

XPATH:
	java -cp classes ser322.JdbcLab2 /Users/toddmartin/ASU/SER322/Homework/Week5/Code/SER322-coursework/Assignment5/code/test.xml 1

clean:
	rm test.xml
	clear

The general format of JdbcLab:
	java -cp <path to mysql connector jar>:classes ser322.JdbcLab <path_to_host_of_mysql_server> <username> <password> <driver> <method> [<args> <args> <args>]

General formate for JdbcLab2:
	java -cp classes ser322.JdbcLab2 <path_to_xml> <language_id>