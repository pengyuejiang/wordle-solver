package com.pengyue.app;

import java.util.List;
import java.util.Map;

public class App {
  public static void main(String[] args) {
    DataBank db = new DataBank("en-lexicon-reduced.txt");
//    Map<Character, Double> countMapping = db.getFrequencyMapping();
    Map<String, Double> positionedLexicalItemProbabilityMapping = db.getPositionedLexicalItemProbabilityMapping();
    // https://www.baeldung.com/java-hashmap-sort
    positionedLexicalItemProbabilityMapping.entrySet()
            .stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(System.out::println);

    List<Map<Character, Double>> positionedFrequencyMapping = db.getPositionedFrequencyMapping();
    for (Map<Character, Double> map : positionedFrequencyMapping) {
      map.entrySet()
              .stream()
              .sorted(Map.Entry.<Character, Double>comparingByValue().reversed())
              .forEach(System.out::println);
    }
  }
}
