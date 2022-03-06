package com.pengyue.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataBank {
  private List<String> lexicon;
  private List<Map<Character, Integer>> positionedCountMap = new ArrayList<>();
  private List<Map<Character, Double>> positionedFrequencyMap = new ArrayList<>();
  private Map<Character, Integer> countMap;
  private Map<Character, Double> frequencyMap;
  private int lexiconSize;
  private Map<String, Double> wordProbabilityMap = new HashMap<>();
  private Map<String, Double> positionedWordProbabilityMap = new HashMap<>();

  public List<Map<Character, Double>> getPositionedFrequencyMap() {
    return List.copyOf(positionedFrequencyMap);
  }

  public Map<Character, Double> getFrequencyMap() {
    return Map.copyOf(frequencyMap);
  }

  public Map<String, Double> getWordProbabilityMap() {
    return Map.copyOf(wordProbabilityMap);
  }

  public Map<String, Double> getPositionedWordProbabilityMap() {
    return Map.copyOf(positionedWordProbabilityMap);
  }

  private void computeFrequencyMapping() {
    for (char i = 'a'; i <= 'z'; i++) {
      frequencyMap.put(i, ((double) countMap.get(i)) / ((double) lexiconSize));
      for (int j = 0; j < Constants.WORD_LENGTH; j++) {
        positionedFrequencyMap.get(j).put(i,
                ((double) positionedCountMap.get(j).get(i)) / ((double) lexiconSize));
      }
    }
  }

  private void computeLexicalProbabilityMapping() {
    for (String lexicalItem : lexicon) {
      double probability = 1.0;
      for (int i = 0; i < Constants.WORD_LENGTH; i++) {
        probability *= frequencyMap.get(lexicalItem.charAt(i));
      }
      wordProbabilityMap.put(lexicalItem, probability);
    }
  }

  private void computePositionedLexicalProbabilityMapping() {
    for (String lexicalItem : lexicon) {
      double probability = 1.0;
      for (int i = 0; i < Constants.WORD_LENGTH; i++) {
        probability *= positionedFrequencyMap.get(i).get(lexicalItem.charAt(i));
      }
      positionedWordProbabilityMap.put(lexicalItem, probability);
    }
  }

  public DataBank(List<String> lexicon) {
    this.lexicon = lexicon;
    HashMap<Character, Integer> discreteTemplateMap = new HashMap<>();
    HashMap<Character, Double> continuousTemplateMap = new HashMap<>();
    for (char i = 'a'; i <= 'z'; i++) {
      discreteTemplateMap.put(i, 0);
      continuousTemplateMap.put(i, 0.0);
    }

    for (int i = 0; i < Constants.WORD_LENGTH; i++) {
      // Turns out that Map.copyOf(discreteTemplateMap) will be immutable
      positionedCountMap.add((Map<Character, Integer>) discreteTemplateMap.clone());
      positionedFrequencyMap.add((Map<Character, Double>) continuousTemplateMap.clone());
    }
    countMap = (Map<Character, Integer>) discreteTemplateMap.clone();
    frequencyMap = (Map<Character, Double>) continuousTemplateMap.clone();

    for (String word : lexicon) {
      for (int i = 0; i < Constants.WORD_LENGTH; i++) {
        Character currentChar = word.charAt(i);
        positionedCountMap.get(i).put(currentChar, positionedCountMap.get(i).get(currentChar) + 1);
        countMap.put(currentChar, countMap.get(currentChar) + 1);
      }
    }

    lexiconSize = lexicon.size();
    computeFrequencyMapping();
    computeLexicalProbabilityMapping();
    computePositionedLexicalProbabilityMapping();
  }
}
