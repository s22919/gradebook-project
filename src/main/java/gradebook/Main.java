package gradebook;

import java.util.Map;

/** Entry point for the Gradebook application. */
public final class Main {

  /** Grade value: 5.0. */
  private static final double GRADE_A = 5.0;

  /** Grade value: 4.0. */
  private static final double GRADE_B = 4.0;

  /** Grade value: 3.0. */
  private static final double GRADE_C = 3.0;

  /** Grade value: 3.5. */
  private static final double GRADE_C_PLUS = 3.5;

  /** Utility class — not instantiated. */
  private Main() {
    // not instantiated
  }

  /**
   * Application entry point.
   *
   * @param args command-line arguments (unused)
   */
  public static void main(final String[] args) {
    System.out.println("=== Uruchamianie Dzienniczka Ocen (Gradebook) ===");

    Gradebook gradebook = new Gradebook();

    gradebook.addSubject("Matematyka");
    gradebook.addSubject("Fizyka");
    gradebook.addSubject("Historia");

    gradebook.addGrade("Matematyka", GRADE_A);
    gradebook.addGrade("Matematyka", GRADE_B);
    gradebook.addGrade("Fizyka", GRADE_C);
    gradebook.addGrade("Fizyka", GRADE_C_PLUS);
    gradebook.addGrade("Historia", GRADE_B);

    double avgMat = gradebook.calcAvgForSubject("Matematyka");
    System.out.println("\nSrednia z Matematyki: " + avgMat);

    double avgFiz = gradebook.calcAvgForSubject("Fizyka");
    System.out.println("Srednia z Fizyki: " + avgFiz);

    System.out.println("\nSrednie ze wszystkich przedmiotow:");
    Map<String, Double> allAverages = gradebook.calcAvgForAllSubjects();
    for (Map.Entry<String, Double> entry : allAverages.entrySet()) {
      System.out.println("- " + entry.getKey() + ": " + entry.getValue());
    }
  }
}
