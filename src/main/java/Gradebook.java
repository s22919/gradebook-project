import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Gradebook {
  // Zmiana: Usunąłem listę subjects, ponieważ nazwy przedmiotów są już kluczami w mapie
  // subjectsGrades.
  // Przechowywanie tego samego w dwóch miejscach prowadzi do błędów synchronizacji danych (tzw.
  // redundancja).
  private final Map<String, List<Double>> subjectsGrades;

  public Gradebook() {
    this.subjectsGrades = new HashMap<>();
  }

  public void addSubject(String subject) {
    // Zmiana: Dodano sprawdzenie, czy przedmiot już istnieje, aby nie nadpisać listy ocen nową,
    // pustą listą.
    if (subject == null || subject.isBlank()) {
      throw new IllegalArgumentException("Subject name cannot be empty");
    }
    if (!subjectsGrades.containsKey(subject)) {
      subjectsGrades.put(subject, new ArrayList<>());
    }
  }

  public void addGrade(String subject, double grade) {
    if (subjectsGrades.containsKey(subject)) {
      // Zmiana: Usunięto warunek if (!subject.equals("History")).
      // Wcześniej kod blokował dodawanie ocen z historii bez żadnego komunikatu, co było błędem
      // logicznym.
      subjectsGrades.get(subject).add(grade);
    } else {
      throw new IllegalArgumentException(subject + " not found in list of subjects");
    }
  }

  public double calcAvgForSubject(String subject) {
    if (subjectsGrades.containsKey(subject)) {
      List<Double> grades = subjectsGrades.get(subject);

      // Zmiana: Zamiast rzucać wyjątek przy braku ocen, lepiej zwrócić 0.0 lub poinformować o tym
      // inaczej.
      // Rzucanie błędu przy pustej liście często psuje działanie pętli obliczających średnią dla
      // wszystkich.
      if (grades.isEmpty()) {
        return 0.0;
      }

      double subjectGradeSum = grades.stream().mapToDouble(Double::doubleValue).sum();
      int subjectGradeCount = grades.size();

      return Math.round((subjectGradeSum / subjectGradeCount) * 100.0) / 100.0;
    } else {
      throw new IllegalArgumentException("Subject not in subjects: " + subject);
    }
  }

  public Map<String, Double> calcAvgForAllSubjects() {
    // Zmiana: Implementacja metody TODO.
    // Wykorzystujemy Stream API, aby stworzyć nową mapę, gdzie kluczem jest przedmiot,
    // a wartością wyliczona średnia za pomocą stworzonej wcześniej metody.
    return subjectsGrades.keySet().stream()
        .collect(Collectors.toMap(subject -> subject, this::calcAvgForSubject));
  }

  public List<String> getSubjects() {
    // Zmiana: Zwracamy listę kluczy z mapy, aby zachować spójność z nową strukturą.
    return new ArrayList<>(subjectsGrades.keySet());
  }

  public Map<String, List<Double>> getGrades() {
    // Dobra praktyka: Zwracamy kopię lub widok niemodyfikowalny,
    // aby nikt z zewnątrz nie mógł zmienić ocen bez użycia metody addGrade.
    return Map.copyOf(subjectsGrades);
  }
}
