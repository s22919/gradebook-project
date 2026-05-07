package gradebook;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

/** Unit tests for {@link Gradebook}. */
public final class GradebookTest {

  /** Grade value: 5.0. */
  private static final double GRADE_HIGH = 5.0;

  /** Grade value: 4.0. */
  private static final double GRADE_MED = 4.0;

  /** Grade value: 3.0. */
  private static final double GRADE_LOW = 3.0;

  /** Grade value: 6.0. */
  private static final double GRADE_VERY_HIGH = 6.0;

  /** Tolerance for floating-point equality checks. */
  private static final double DELTA = 0.001;

  /** Gradebook instance reset before each test. */
  private Gradebook gradebook;

  /** Initialises a fresh Gradebook before each test. */
  @BeforeEach
  public void setUp() {
    gradebook = new Gradebook();
  }

  /** Tests that a subject can be added and retrieved. */
  @Test
  public void testAddSubject() {
    gradebook.addSubject("Physics");
    assertTrue(gradebook.getSubjects().contains("Physics"));
  }

  /**
   * Dynamic tests: verifies grades and average for each subject.
   *
   * @return stream of dynamic tests
   */
  @TestFactory
  public Stream<DynamicTest> dynamicTestsForSubjects() {
    gradebook.addSubject("Math");
    gradebook.addSubject("History");
    gradebook.addSubject("Biology");

    return gradebook.getSubjects().stream()
        .map(
            subject ->
                DynamicTest.dynamicTest(
                    "Test for: " + subject,
                    () -> {
                      gradebook.addGrade(subject, GRADE_HIGH);
                      gradebook.addGrade(subject, GRADE_LOW);
                      int cnt = gradebook.getGrades().get(subject).size();
                      assertEquals(2, cnt);
                      double avg = gradebook.calcAvgForSubject(subject);
                      assertEquals(GRADE_MED, avg, DELTA);
                    }));
  }

  /** Tests average calculation across all subjects. */
  @Test
  public void testCalcAvgForAllSubjects() {
    gradebook.addSubject("Math");
    gradebook.addSubject("Physics");

    gradebook.addGrade("Math", GRADE_HIGH);
    gradebook.addGrade("Math", GRADE_LOW);

    gradebook.addGrade("Physics", GRADE_VERY_HIGH);
    gradebook.addGrade("Physics", GRADE_MED);

    Map<String, Double> averages = gradebook.calcAvgForAllSubjects();

    assertNotNull(averages, "Map should not be null");
    assertEquals(2, averages.size(), "Map should have 2 entries");
    assertEquals(GRADE_MED, averages.get("Math"), DELTA, "Math avg");
    assertEquals(GRADE_HIGH, averages.get("Physics"), DELTA, "Physics avg");
  }

  /** Tests that adding a grade to a missing subject throws. */
  @Test
  public void testAddGradeToNonExistentSubject() {
    assertThrows(
        IllegalArgumentException.class,
        () -> gradebook.addGrade("Music", GRADE_MED),
        "Expected exception for unknown subject");
  }

  /** Tests that a subject with no grades returns average 0.0. */
  @Test
  public void testAverageForSubjectWithNoGrades() {
    gradebook.addSubject("EmptySubject");
    Map<String, Double> averages = gradebook.calcAvgForAllSubjects();
    assertTrue(averages.containsKey("EmptySubject"), "Subject expected");
    assertEquals(0.0, averages.get("EmptySubject"), DELTA, "Avg should be 0");
  }

  /** Tests that re-adding a subject does not clear existing grades. */
  @Test
  public void testAddDuplicateSubjectDoesNotClearGrades() {
    gradebook.addSubject("Math");
    gradebook.addGrade("Math", GRADE_HIGH);
    gradebook.addSubject("Math");
    List<Double> mathGrades = gradebook.getGrades().get("Math");
    assertEquals(1, mathGrades.size(), "Grade count should not change");
    assertEquals(GRADE_HIGH, mathGrades.get(0), "Grade value should remain");
  }
}
