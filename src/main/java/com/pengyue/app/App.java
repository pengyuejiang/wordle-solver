package com.pengyue.app;

import com.pengyue.app.solver.Solver;
import com.pengyue.app.solver.StandardSolver;

import java.util.Scanner;

public class App {
  public static void main(String[] args) {
    Solver solver = new StandardSolver("wordle-lexicon.txt");
    Scanner sc = new Scanner(System.in);

//    DataBank db = new DataBank(solver.getLexicon());
//    Map<String, Double> positionedWordProbabilityMap = db.getPositionedWordProbabilityMap();
//    // https://www.baeldung.com/java-hashmap-sort
//    positionedWordProbabilityMap.entrySet()
//            .stream()
//            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
//            .forEach(System.out::println);

    String hint = "";
    // TODO: Set that there's only a number of limited guesses
    while (!hint.equals("ggggg")) {
      System.out.println("The best options are " + solver.getNextSteps().toString());
      System.out.print("Your guess: ");
      String guess = sc.nextLine();
      solver.guess(guess);
      System.out.print("Next hint: ");
      hint = sc.nextLine();
      solver.updateWithHint(hint);
    }
  }
}
