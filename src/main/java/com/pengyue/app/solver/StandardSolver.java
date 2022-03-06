package com.pengyue.app.solver;

import com.pengyue.app.CharacterStatus;
import com.pengyue.app.Constants;
import com.pengyue.app.DataBank;
import com.pengyue.app.LexiconReader;
import com.pengyue.app.UpdateType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StandardSolver extends Solver {
  DataBank db;

  private StandardSolver() {
    guesses = new ArrayList<>();
    hints = new ArrayList<>();

    Map<Character, CharacterStatus> templateMap = new HashMap<>();
    letterCountConstraint = new HashMap<>();
    for (char i = 'a'; i <= 'z'; i++) {
      templateMap.put(i, CharacterStatus.UNKNOWN);
    }
    letterStatus = (Map<Character, CharacterStatus>) ((HashMap<Character, CharacterStatus>) templateMap).clone();
    positionedLetterStatus = new ArrayList<>();
    for (int i = 0; i < Constants.WORD_LENGTH; i++) {
      positionedLetterStatus.add(
              (Map<Character, CharacterStatus>) ((HashMap<Character, CharacterStatus>) templateMap).clone());
    }
  }

  public StandardSolver(List<String> lexicon) {
    this();
    this.lexicon = lexicon;
    db = new DataBank(lexicon);
  }

  public StandardSolver(String path) {
    this();
    lexicon = (new LexiconReader(path)).getLexicon();
    db = new DataBank(lexicon);
  }

  private void updateLexicon(int positionOrCount, UpdateType update, char letterAffected) {
    switch (update) {
      case CORRECT_FILTER:
        lexicon = lexicon.stream()
                .filter(word -> word.charAt(positionOrCount) == letterAffected)
                .collect(Collectors.toList());
        break;
      case INCORRECT_FILTER:
        lexicon = lexicon.stream()
                .filter(word -> word.charAt(positionOrCount) != letterAffected)
                .collect(Collectors.toList());
        break;
      case COUNT_LIMIT:
        lexicon = lexicon.stream()
                .filter(word ->
                        word.chars().filter(character -> character == letterAffected).count() <= positionOrCount)
                .collect(Collectors.toList());
        break;
      case REQUIRE_EXISTENCE:
        lexicon = lexicon.stream()
                .filter(word -> word.contains("" + letterAffected))
                .collect(Collectors.toList());
        break;
    }
    db = new DataBank(lexicon);
  }

  @Override
  public void updateWithHint(String hint) {
    hints.add(hint);
    String latestGuess = guesses.get(guesses.size() - 1);
    // Decompose the hint character by character
    for (int position = 0; position < Constants.WORD_LENGTH; position++) {
      char currentHint = hint.charAt(position);
      char currentLetter = latestGuess.charAt(position);
      switch (currentHint) {
        case Constants.CORRECT:
          // 1. Set global status
          letterStatus.put(currentLetter, CharacterStatus.CORRECT);
          // 2. Set local status
          positionedLetterStatus.get(position).put(currentLetter, CharacterStatus.CORRECT);
          // 3. Change lexicon
          updateLexicon(position, UpdateType.CORRECT_FILTER, currentLetter);
          break;
        case Constants.MISPLACED:
          // 1. Set global status to MISPLACED if UNKNOWN
          if (letterStatus.get(currentLetter) == CharacterStatus.UNKNOWN) {
            letterStatus.put(currentLetter, CharacterStatus.MISPLACED);
          }
          // 2. Set local status to WRONG
          positionedLetterStatus.get(position).put(currentLetter, CharacterStatus.WRONG);
          // 3. Change lexicon for local incorrect
          updateLexicon(position, UpdateType.INCORRECT_FILTER, currentLetter);
          // 4. Change lexicon for existence
          updateLexicon(position, UpdateType.REQUIRE_EXISTENCE, currentLetter);
          break;
        case Constants.WRONG:
          // 1. If it already has something higher than "WRONG", add a count limit
          if (letterStatus.get(currentLetter) == CharacterStatus.CORRECT
              || letterStatus.get(currentLetter) == CharacterStatus.MISPLACED) {
            // 1.1 Count up the letters and set a limit to 1 lower than that (can be optimized)
            // https://www.delftstack.com/howto/java/count-characters-in-a-string-java/
            int newCount = (int) latestGuess.chars().filter(character -> character == currentLetter).count() - 1;
            letterCountConstraint.put(currentLetter, newCount);
            // 1.2 Update lexicon
            updateLexicon(newCount, UpdateType.COUNT_LIMIT, currentLetter);
          } else {
            // 2.1 Set global status to WRONG
            letterStatus.put(currentLetter, CharacterStatus.WRONG);
            for (int i = 0; i < Constants.WORD_LENGTH; i++) {
              // 2.2 Set local status to WRONG
              positionedLetterStatus.get(i).put(currentLetter, CharacterStatus.WRONG);
              // 2.3 Update lexicon
              updateLexicon(i, UpdateType.INCORRECT_FILTER, currentLetter);
            }
          }
          break;
      }
    }
  }

  @Override
  public void guess(String guess) {
    guesses.add(guess);
  }

  @Override
  public List<String> getNextSteps() {
    // Will always do the same thing because we are updating DataBank.
    // TODO: Change the interface so that it only returns one
    List<String> bestMoves = db.getPositionedWordProbabilityMap().entrySet()
            .stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .limit(Constants.WORD_LENGTH)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    if (bestMoves.isEmpty()) {
      System.out.println(lexicon.toString());
    }
    return bestMoves;
  }
}
