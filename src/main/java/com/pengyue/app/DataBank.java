package com.pengyue.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class DataBank {
  private List<String> lexicon = new ArrayList<>();
  private List<Map<Character, Integer>> positionedCountMapping = new ArrayList<>();
  private List<Map<Character, Double>> positionedFrequencyMapping = new ArrayList<>();
  private Map<Character, Integer> countMapping;
  private Map<Character, Double> frequencyMapping;
  private Map<String, Double> probabilityScore = new HashMap<>();
  private int lexiconSize;
  private Map<String, Double> lexicalItemProbabilityMapping = new HashMap<>();
  private Map<String, Double> positionedLexicalItemProbabilityMapping = new HashMap<>();

  public List<Map<Character, Double>> getPositionedFrequencyMapping() {
    return List.copyOf(positionedFrequencyMapping);
  }

  public Map<Character, Double> getFrequencyMapping() {
    return Map.copyOf(frequencyMapping);
  }

  public Map<String, Double> getLexicalItemProbabilityMapping() {
    return Map.copyOf(lexicalItemProbabilityMapping);
  }

  public Map<String, Double> getPositionedLexicalItemProbabilityMapping() {
    return Map.copyOf(positionedLexicalItemProbabilityMapping);
  }

  private void computeFrequencyMapping() {
    for (char i = 'a'; i <= 'z'; i++) {
      frequencyMapping.put(i, ((double) countMapping.get(i)) / ((double) lexiconSize));
      for (int j = 0; j < Constants.WORD_LENGTH; j++) {
        positionedFrequencyMapping.get(j).put(i,
                ((double) positionedCountMapping.get(j).get(i)) / ((double) lexiconSize));
      }
    }
  }

  private void computeLexicalProbabilityMapping() {
    for (String lexicalItem : lexicon) {
      double probability = 1.0;
      for (int i = 0; i < Constants.WORD_LENGTH; i++) {
        probability *= frequencyMapping.get(lexicalItem.charAt(i));
      }
      lexicalItemProbabilityMapping.put(lexicalItem, probability);
    }
  }

  private void computePositionedLexicalProbabilityMapping() {
    for (String lexicalItem : lexicon) {
      double probability = 1.0;
      for (int i = 0; i < Constants.WORD_LENGTH; i++) {
        probability *= positionedFrequencyMapping.get(i).get(lexicalItem.charAt(i));
      }
      positionedLexicalItemProbabilityMapping.put(lexicalItem, probability);
    }
  }

  public DataBank(String lexiconPath) {
    HashMap<Character, Integer> discreteTemplateMapping = new HashMap<>();
    HashMap<Character, Double> continuousTemplateMapping = new HashMap<>();
    for (char i = 'a'; i <= 'z'; i++) {
      discreteTemplateMapping.put(i, 0);
      continuousTemplateMapping.put(i, 0.0);
    }

    for (int i = 0; i < Constants.WORD_LENGTH; i++) {
      // Turns out that Map.copyOf(discreteTemplateMapping) will be immutable
      positionedCountMapping.add((Map<Character, Integer>) discreteTemplateMapping.clone());
      positionedFrequencyMapping.add((Map<Character, Double>) continuousTemplateMapping.clone());
    }
    countMapping = (Map<Character, Integer>) discreteTemplateMapping.clone();
    frequencyMapping = (Map<Character, Double>) continuousTemplateMapping.clone();

    try {
      File lexiconFile = new File(lexiconPath);
      Scanner lexiconReader = new Scanner(lexiconFile);
      while (lexiconReader.hasNextLine()) {
        String lexicalItem = lexiconReader.nextLine();

        lexicon.add(lexicalItem);
        for (int i = 0; i < Constants.WORD_LENGTH; i++) {
          Character currentChar = lexicalItem.charAt(i);
          positionedCountMapping.get(i).put(currentChar, positionedCountMapping.get(i).get(currentChar) + 1);
          countMapping.put(currentChar, countMapping.get(currentChar) + 1);
        }
      }
    } catch (FileNotFoundException e) {
      System.err.println("File not found.");
      e.printStackTrace();
    }

    lexiconSize = lexicon.size();
    computeFrequencyMapping();
    computeLexicalProbabilityMapping();
    computePositionedLexicalProbabilityMapping();
  }
}
