package cards;

import codes.derive.foldem.Card;
import codes.derive.foldem.Deck;
import codes.derive.foldem.Hand;
import codes.derive.foldem.Poker;
import codes.derive.foldem.board.Board;
import codes.derive.foldem.eval.DefaultEvaluator;
import codes.derive.foldem.eval.Evaluator;
import java.util.ArrayList;
import java.util.List;

final class NutsCounter {
  private static final Evaluator EVALUATOR = new DefaultEvaluator();

  private final int iterations;

  NutsCounter(int iterations) {
    this.iterations = iterations;
  }

  int getNutsCount(CharacterizedHand hand) {
    int nutsCount = 0;
    for (int k = 0; k < iterations; k++) {
      Deck deck = Poker.shuffledDeck();
      deck.pop(hand);

      Board board = Poker.board(deck.pop(), deck.pop(), deck.pop(), deck.pop(), deck.pop());

      if (getRemainingHands(deck)
          .stream()
          // Note, that the DefaultEvaluator returns lower numbers for better ranking hands, which is backwards
          // from the Evaluator documentation.
          // See https://github.com/ableiten/foldem/issues/35
          .allMatch(other -> EVALUATOR.rank(hand, board) < EVALUATOR.rank(other, board))) {
        nutsCount++;
      }
    }
    System.out.println(
        String.format("Done %s (%s) was the nuts %d times", hand, hand.characterization(), nutsCount));
    return nutsCount;
  }

  private List<Hand> getRemainingHands(Deck d) {
    List<Card> remaining = new ArrayList<>(45);
    while (d.remaining() > 0) {
      remaining.add(d.pop());
    }

    List<Hand> hands = new ArrayList<>();
    for (int n = 0; n < remaining.size(); n++) {
      for (int m = n + 1; m < remaining.size(); m++) {
        hands.add(new Hand(remaining.get(n), remaining.get(m)));
      }
    }
    return hands;
  }
}
