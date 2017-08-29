![Tile Logo](http://www.nickguys.com/lang/Tile-Small-Logo.png)

[![Build](http://nornick3.zapto.org/tile/events/get_badge.php?type=build)]() [![Bitbucket issues](https://img.shields.io/bitbucket/issues/atlassian/python-bitbucket.svg)]()

Install Antlr4:
```
#!javascript
$ cd /usr/local/lib
$ curl -O http://www.antlr.org/download/antlr-4.5.3-complete.jar
$ export CLASSPATH=".:/usr/local/lib/antlr-4.5.3-complete.jar:$CLASSPATH"
```
Add an Alias:
```
#!javascript
$ alias antlr4='java -Xmx500M -cp "/usr/local/lib/antlr-4.5.3-complete.jar:$CLASSPATH" org.antlr.v4.Tool'
```

Run programs with
```
#!javascript
$ java Main "./Tests/ProgramName.tl"
```
Compile Java with
```
#!javascript
$ antlr4 Tile.g4 -visitor
$ javac *.java
```
