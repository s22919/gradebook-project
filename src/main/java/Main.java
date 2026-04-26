import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Uruchamianie Dzienniczka Ocen (Gradebook) ===");

        // Inicjalizacja dzienniczka
        Gradebook gradebook = new Gradebook();

        // Dodawanie przedmiotów
        gradebook.addSubject("Matematyka");
        gradebook.addSubject("Fizyka");
        gradebook.addSubject("Historia");

        // Dodawanie ocen
        gradebook.addGrade("Matematyka", 5.0);
        gradebook.addGrade("Matematyka", 4.0);

        gradebook.addGrade("Fizyka", 3.0);
        gradebook.addGrade("Fizyka", 3.5);

        // Dodawanie ocen do historii
        gradebook.addGrade("Historia", 4.0);

        // Wyświetlanie średnich z poszczególnych przedmiotów
        System.out.println("\nŚrednia z Matematyki: " + gradebook.calcAvgForSubject("Matematyka"));
        System.out.println("Średnia z Fizyki: " + gradebook.calcAvgForSubject("Fizyka"));

        // Wyświetlanie wszystkich średnich za pomocą mapy
        System.out.println("\nŚrednie ze wszystkich przedmiotów:");
        Map<String, Double> allAverages = gradebook.calcAvgForAllSubjects();

        for (Map.Entry<String, Double> entry : allAverages.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue());
        }
    }
}