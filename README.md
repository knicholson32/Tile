Install Antlr4:

* cd /usr/local/lib
* curl -O http://www.antlr.org/download/antlr-4.5.3-complete.jar
* export CLASSPATH=".:/usr/local/lib/antlr-4.5.3-complete.jar:$CLASSPATH"

Add an Alias:

* alias antlr4='java -Xmx500M -cp "/usr/local/lib/antlr-4.5.3-complete.jar:$CLASSPATH" org.antlr.v4.Tool'


Run programs with

* java Main "./Tests/ProgramName.tl"

Compile Java with

* antlr4 Tile.g4 -visitor
* javac *.java
