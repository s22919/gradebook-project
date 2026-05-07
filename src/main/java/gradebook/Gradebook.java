package gradebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Manages subjects and grades for a student gradebook. */
public final class Gradebook {

  /** Factor used to round averages to two decimal places. */
  private static final double ROUNDING_FACTOR = 100.0;

  /** Map from subject name to list of grades. */
  private final Map<String, List<Double>> subjectsGrades;

  /** Creates a new empty Gradebook. */
  public Gradebook() {
    this.subjectsGrades = new HashMap<>();
  }

  /**
   * Adds a new subject to the gradebook.
   *
   * @param subject the subject name to add
   */
  public void addSubject(final String subject) {
    if (subject == null || subject.isBlank()) {
      throw new IllegalArgumentException("Subject name cannot be empty");
    }
    if (!subjectsGrades.containsKey(subject)) {
      subjectsGrades.put(subject, new ArrayList<>());
    }
  }

  /**
   * Adds a grade for the specified subject.
   *
   * @param subject the subject name
   * @param grade the grade to add
   */
  public void addGrade(final String subject, final double grade) {
    if (!subjectsGrades.containsKey(subject)) {
      String msg = subject + " not found in list of subjects";
      throw new IllegalArgumentException(msg);
    }
    subjectsGrades.get(subject).add(grade);
  }

  /**
   * Calculates the average grade for the specified subject.
   *
   * @param subject the subject name
   * @return average grade, or 0.0 if no grades recorded
   */
  public double calcAvgForSubject(final String subject) {
    if (!subjectsGrades.containsKey(subject)) {
      String msg = "Subject not in subjects: " + subject;
      throw new IllegalArgumentException(msg);
    }
    List<Double> grades = subjectsGrades.get(subject);
    if (grades.isEmpty()) {
      return 0.0;
    }
    double sum = grades.stream().mapToDouble(Double::doubleValue).sum();
    double rounded = Math.round((sum / grades.size()) * ROUNDING_FACTOR);
    return rounded / ROUNDING_FACTOR;
  }

  /**
   * Returns a map of all subjects to their average grades.
   *
   * @return map from subject name to average grade
   */
  public Map<String, Double> calcAvgForAllSubjects() {
    return subjectsGrades.keySet().stream()
        .collect(Collectors.toMap(subject -> subject, this::calcAvgForSubject));
  }

  /**
   * Returns the list of all subject names.
   *
   * @return list of subject names
   */
  public List<String> getSubjects() {
    return new ArrayList<>(subjectsGrades.keySet());
  }

  /**
   * Returns an unmodifiable copy of the grades map.
   *
   * @return map from subject name to list of grades
   */
  public Map<String, List<Double>> getGrades() {
    return Map.copyOf(subjectsGrades);
  }
}
