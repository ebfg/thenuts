package cards;

import codes.derive.foldem.Card;
import codes.derive.foldem.Hand;

/**
 * A {@link codes.derive.foldem.Hand} that supports characterization, i.e. otherwise identical hands
 * that only differ in the specific suits.
 *
 * <p>For example, both {@code AhKh} and {@code AcKc} will be characterized as {@code AKs}.
 */
final class CharacterizedHand extends Hand {
  private final String characterization;

  CharacterizedHand(Card a, Card b) {
    super(a, b);
    characterization = characterize(a, b);
  }

  String characterization() {
    return characterization;
  }

  private String characterize(Card a, Card b) {
    return String.format(
        "%s%s%s", Card.LABEL[a.getValue()], Card.LABEL[b.getValue()], suited() ? "s" : "o");
  }
}
