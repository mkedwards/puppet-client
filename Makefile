# You need CUP v0.10j (or newer) for this makefile to work (for puppet.cup)
#
# CUP classes should be included in CLASSPATH 

CUP        = java java_cup.Main -interface <
JFLEX      = jflex
CUPJAR     = /usr/share/java/cup-0.11a.jar
CP         = .:$(CUPJAR):/usr/share/java/commons-httpclient.jar:/usr/share/java/httpcore.jar
JAVA       = java
JAVAC      = javac
JAVACFLAGS = -cp $(CP)
CUP        = $(JAVA) -jar $(CUPJAR)



# --------------------------------------------------

all: test jar

jar: compile
	jar cvfm PuppetClient.jar manifest.txt *.class

test: lexer-output.txt
	@(diff lexer-output.txt lexer-output.good && echo "Test OK!") || echo "Test failed!"

lexer-output.txt: compile
	$(JAVA) -cp $(CP) TestLexer sample.puppet > lexer-output.txt


compile: scanner parser unicode
	$(JAVAC) $(JAVACFLAGS) PuppetParser.java TestLexer.java
	$(JAVAC) $(JAVACFLAGS) PuppetParser.java PuppetClient.java

parser: parser.java 

parser.java: puppet.cup
	$(CUP) -interface puppet.cup

scanner: Scanner.java

Scanner.java: puppet.flex
	$(JFLEX) puppet.flex

unicode: UnicodeEscapes.java

UnicodeEscapes.java: unicode.flex
	$(JFLEX) unicode.flex

clean:
	rm -f *.class
	rm -f *~
	rm -f Scanner.java
	rm -f parser.java
	rm -f sym.java
	rm -f UnicodeEscapes.java
	rm -f lexer-output.txt
	rm -f PuppetClient.jar
