import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach; // ZMIANA z BeforeAll, przez co testy nie wpływają na siebie nawzajem.
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import java.util.List;
import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap; //Dodajemy mapę i hashmapę, żeby móc poprawnie wykonać test ze średnią z wszystkich przedmiotów.


public class GradebookTest {
    private Gradebook gradebook;

    @BeforeEach
    public void setUp() {
        // ZMIANA: Inicjalizacja przed KAŻDYM testem.
        // DLACZEGO: Gwarantuje to, że każdy test zaczyna z "czystą kartą".
        // Przedmioty dodane w jednym teście nie psują wyników w drugim.
        gradebook = new Gradebook();
    }

    @Test
    public void testAddSubject() {
        gradebook.addSubject("Physics");
        assertTrue(gradebook.getSubjects().contains("Physics"));
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsForSubjects() {
        gradebook.addSubject("Math");
        gradebook.addSubject("History");
        gradebook.addSubject("Biology");

        return gradebook.getSubjects().stream()
                .map(subject -> DynamicTest.dynamicTest("Test for: " + subject, () -> {
                    // 1. Dodajemy oceny
                    gradebook.addGrade(subject, 5.0);
                    gradebook.addGrade(subject, 3.0);

                    // 2. Sprawdzamy ilość ocen
                    assertEquals(2, gradebook.getGrades().get(subject).size());

                    // 3. Sprawdzamy średnią (5+3 / 2 = 4.0)
                    double expected = 4.0;
                    double actual = gradebook.calcAvgForSubject(subject);
                    assertEquals(expected, actual, 0.001);
                }));
    }
    @Test
    public void testCalcAvgForAllSubjects() {
        // 1. Przygotowanie danych
        gradebook.addSubject("Math");
        gradebook.addSubject("Physics");

        gradebook.addGrade("Math", 5.0);
        gradebook.addGrade("Math", 3.0); // Średnia: 4.0

        gradebook.addGrade("Physics", 6.0);
        gradebook.addGrade("Physics", 4.0); // Średnia: 5.0

        // 2. Wywołanie testowanej metody
        Map<String, Double> averages = gradebook.calcAvgForAllSubjects();

        // 3. Weryfikacja wyników
        assertNotNull(averages, "Mapa średnich nie powinna być nullem");
        assertEquals(2, averages.size(), "Mapa powinna zawierać średnie dla 2 przedmiotów");
        assertEquals(4.0, averages.get("Math"), 0.001, "Niepoprawna średnia dla Math");
        assertEquals(5.0, averages.get("Physics"), 0.001, "Niepoprawna średnia dla Physics");
    }

    @Test
    public void testAddGradeToNonExistentSubject() {
        // Sprawdzamy, czy próba dodania oceny do "Music" (którego nie ma)
        // rzuca wyjątek lub czy system tworzy przedmiot automatycznie (zależnie od implementacji)
        // Zakładając, że Twoja metoda powinna rzucić wyjątek:
        assertThrows(IllegalArgumentException.class, () -> {
            gradebook.addGrade("Music", 4.0);
        }, "Powinien zostać rzucony wyjątek przy próbie dodania oceny do nieistniejącego przedmiotu");
    }

    @Test
    public void testAverageForSubjectWithNoGrades() {
        gradebook.addSubject("EmptySubject");

        // Pobieramy mapę średnich
        Map<String, Double> averages = gradebook.calcAvgForAllSubjects();

        // Sprawdzamy, czy przedmiot bez ocen nie psuje mapy i zwraca 0.0
        assertTrue(averages.containsKey("EmptySubject"), "Mapa powinna zawierać przedmiot nawet bez ocen");
        assertEquals(0.0, averages.get("EmptySubject"), 0.001, "Średnia dla przedmiotu bez ocen powinna wynosić 0.0");
    }

    @Test
    public void testAddDuplicateSubjectDoesNotClearGrades() {
        gradebook.addSubject("Math");
        gradebook.addGrade("Math", 5.0);

        // Ponowne dodanie tego samego przedmiotu
        gradebook.addSubject("Math");

        // Sprawdzamy, czy oceny nie zniknęły (czy mapa nie została nadpisana pustą listą)
        List<Double> mathGrades = gradebook.getGrades().get("Math");
        assertEquals(1, mathGrades.size(), "Ponowne dodanie przedmiotu nie powinno czyścić listy jego ocen");
        assertEquals(5.0, mathGrades.get(0), "Ocena powinna pozostać w dzienniczku");
    }
}