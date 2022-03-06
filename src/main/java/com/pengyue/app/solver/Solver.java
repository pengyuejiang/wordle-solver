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

  public abstract List<String> getNextSteps();
  public abstract void updateWithHint(String hint);
  public abstract void guess(String guess);

  public List<String> getLexicon() {
    return List.copyOf(lexicon);
  }
}
