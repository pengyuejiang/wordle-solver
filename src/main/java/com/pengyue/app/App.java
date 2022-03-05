package com.pengyue.app;

import java.util.Map;

public class App {
  public static void main(String[] args) {
    DataBank db = new DataBank("en-lexicon-reduced.txt");
    Map<Character, Double> countMapping = db.getFrequencyMapping();
    System.out.println(countMapping.toString());
    // https://www.baeldung.com/java-hashmap-sort
    countMapping.entrySet()
            .stream()
            .sorted(Map.Entry.<Character, Double>comparingByValue().reversed())
            .forEach(System.out::println);
  }
}
