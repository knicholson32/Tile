![Tile Logo](http://www.nickguys.com/lang/Tile-Small-Logo.png)

[![Tag Badge](http://nornick3.zapto.org/tile/events/get_badge.php?type=tag)]()
[![Build Badge](http://nornick3.zapto.org/tile/events/get_badge.php?type=build)]()
[![Assert Badge](http://nornick3.zapto.org/tile/events/get_badge.php?type=assert)]()

# Welcome to Tile
Tile is a Java based (with more to come) programming language written for high fidelity mathematical operations. Its original intended use was for simulated autopilot systems for spacecraft, however it continues to evolve.

## Getting Started
Tile is compiled in Java and depends heavily on [Antlr4](https://github.com/antlr/antlr4) for grammar and lexer support. 

### To Install Antlr4:
For Unix systems:

Download Antlr4 and add it to the Classpath:
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

### Compiling Tile
In the root Tile directory, compile with:
```
#!javascript
$ antlr4 Tile.g4 -visitor
$ javac *.java
```

### Testing Tile
Once Tile is downloaded and installed, use the following to run the assertion tests:
```
#!javascript
$ java Main "./Tests/utils/tileAssertion.tl"
```

### Using Tile
Run programs with:
```
#!javascript
$ java Main "./Tests/ProgramName.tl"
```

### Documentation
For help working with Tile, please refer to our [Documentation](https://bitbucket.org/roundpixeldev/tile/wiki/Home).

## Built With

* [Java](https://www.oracle.com/java/index.html) - The Base Language
* [Antlr4](https://github.com/antlr/antlr4) - Grammar and Lexer Support

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests.

## Versioning

We use [SemVer](http://semver.org/) for versioning.

## Authors

* **Keenan Nicholson** - *Initial Idea and language base*
* **Norris Nicholson** - *Tile Utilities and Build Procedures*

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
