build:
	find -name "*.java" -path "./src/*" > sources.txt
	javac @sources.txt -d out
	jar --create --file TripleA.jar --manifest ./META-INF/MANIFEST.MF -C out/ .

run: build
	java -jar TripleA.jar

start:
	xboard -debug -nameOfDebugFile debug.txt -fcp "make run"

bot:
	xboard -debug -nameOfDebugFile debug.txt -variant 3check -fcp "make run" -scp "pulsar2009-9b-64 mxT-4" -tc 5 -inc 2 -autoCallFlag true -mg 4 -sgf partide.txt -reuseFirst false

clean:
	rm -rdf out sources.txt debug.txt TripleA.jar