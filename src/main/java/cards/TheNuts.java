package cards;

import codes.derive.foldem.Card;
import codes.derive.foldem.Poker;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public final class TheNuts {

  private static final int ITERATIONS = 10_000;

  public static void main(String[] args) throws Exception {
    List<Card> cards = new ArrayList<>(Poker.cards());

    int cores = Runtime.getRuntime().availableProcessors();
    System.out.println(String.format("Have %d cores", cores));
    ExecutorService threadPool = Executors.newFixedThreadPool(cores);
    List<Future<?>> futures = new ArrayList<>();

    Set<String> seen = new HashSet<>();
    Map<String, Integer> nutsCount = new ConcurrentHashMap<>();
    NutsCounter nutsCounter = new NutsCounter(ITERATIONS);
    for (int i = 0; i < cards.size(); i++) {
      for (int j = i + 1; j < cards.size(); j++) {
        CharacterizedHand hand = new CharacterizedHand(cards.get(i), cards.get(j));
        if (!seen.contains(hand.characterization())) {
          seen.add(hand.characterization());
          futures.add(
              threadPool.submit(
                  () -> nutsCount.put(hand.characterization(), nutsCounter.getNutsCount(hand))));
        }
      }
    }

    threadPool.shutdown();
    threadPool.awaitTermination(1, TimeUnit.DAYS);

    nutsCount
        .entrySet()
        .stream()
        .sorted(Comparator.comparing(Map.Entry::getValue))
        .map(
            entry ->
                String.format(
                    "%s,%d,%f",
                    entry.getKey(), entry.getValue(), entry.getValue() / (ITERATIONS * 1.0)))
        .forEach(System.out::println);
  }
}
