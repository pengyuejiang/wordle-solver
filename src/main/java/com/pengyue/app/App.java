package com.pengyue.app;

import com.pengyue.app.solver.Solver;
import com.pengyue.app.solver.StandardSolver;

import java.util.List;
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
    while (!hint.equals("ggggg")) {
      List<String> bestOptions = solver.getNextSteps();
      if (bestOptions.size() == 0) {
        System.err.println("You must got the hints wrong, check again :(");
        System.exit(1);
      } else if (bestOptions.size() == 1) {
        System.out.println("Congratulations! The word must be \"" + bestOptions.get(0) + "\"!");
        break;
      }
      System.out.println("The best options are " + bestOptions.toString());
      System.out.print("Your guess: ");
      String guess = sc.nextLine();
      solver.guess(guess);
      System.out.print("Next hint [type 'g' for green, 'y' for yellow, and 'b' for black, e.g.: ggbby]: ");
      hint = sc.nextLine();
      solver.updateWithHint(hint);
    }
  }
}
