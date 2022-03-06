package com.pengyue.app.solver;

import com.pengyue.app.CharacterStatus;

import java.util.List;
import java.util.Map;

public abstract class Solver {
  protected List<String> guesses;
  protected List<String> hints;
  protected List<String> lexicon;
  protected List<Map<Character, CharacterStatus>> positionedLetterStatus;
  protected Map<Character, CharacterStatus> letterStatus;
  protected Map<Character, Integer> letterCountConstraint;

  /**
   * Obtain a list of the most probable words based on current situation.
   * @return the best options to try
   */
  public abstract List<String> getNextSteps();

  /**
   * Given a color-coded hint, update internal game status.
   * @param hint the color-coded hint, where 'g' stands for green, 'y' stands for yellow, and 'b' for black
   */
  public abstract void updateWithHint(String hint);

  /**
   * Register a guess into the solver.
   * @param guess the guess to register for
   */
  public abstract void guess(String guess);
}
