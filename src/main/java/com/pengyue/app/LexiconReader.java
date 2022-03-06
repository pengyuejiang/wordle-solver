package com.pengyue.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Reads a raw lexicon from file path. */
public class LexiconReader {
  private List<String> lexicon;

  public List<String> getLexicon() {
    return lexicon;
  }

  public LexiconReader(String path) {
    lexicon = new ArrayList<>();
    try {
      File lexiconFile = new File(path);
      Scanner scanner = new Scanner(lexiconFile);
      while (scanner.hasNextLine()) {
        String word = scanner.nextLine();
        lexicon.add(word);
      }
    } catch (FileNotFoundException e) {
      System.err.println("File not found.");
      e.printStackTrace();
    }
  }
}
