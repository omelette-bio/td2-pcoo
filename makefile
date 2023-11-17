JAVAC=javac
JAVA=java
SRC=source
BIN=bin
MIDI=midi_test
CLASSPATH=$(BIN)

all: compile

compile:
	mkdir -p $(BIN)
	$(JAVAC) -d $(BIN) -sourcepath $(SRC) $(SRC)/*.java

fichier1: compile
	$(JAVA) -cp $(CLASSPATH) Main $(MIDI)/fichier1.mid

fichier2: compile
	$(JAVA) -cp $(CLASSPATH) Main $(MIDI)/fichier2.mid

fichier3: compile
	$(JAVA) -cp $(CLASSPATH) Main $(MIDI)/fichier3.mid

clean:
	rm -rf $(BIN)