package com.example.pcbgenerator.pcb;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa służąca do tworzenia obiektu Pcb w oparciu o dane przesłane w zapytaniu HTTP
 */
public class PcbJsonData {
    /**
     * Szerokość płytki drukowanej
     */
    public int sizeX;
    /**
     * Wysokość płytki drukowanej
     */
    public int sizeY;
    /**
     * Lista punktów rozpoczynających ścieżki
     */
    public List<Point> starts;
    /**
     * Lista punktów kończących ścieżki
     */
    public List<Point> ends;
    /**
     * Wartość kary za każde przecięcie. Domyślnie: 1000
     */
    public Integer crossingPenalty;
    /**
     * Wartość kary za długość ścieżki. Domyślnie: 20
     */
    public Integer pathLengthPenalty;
    /**
     * Wartość kary za liczbę sekcji wchodzących w skład ścieżki. Domyślnie: 1
     */
    public Integer numberOfSectionsPenalty;
    /**
     * Wartość kary za każdą ścieżkę która wykroczyła poza obszar płytki drukowanej. Domyślnie: 2000
     */
    public Integer pathsOutOfPcbPenalty;
    /**
     * Wartość kary za długość ścieżek poza obszarem płytki drukowanej. Domyślnie: 1500
     */
    public Integer pathsOutOfPcbLengthPenalty;
    /**
     * Liczba osobników w jednej populacji. Domyślnie: 100
     */
    public Integer populationSize;
    /**
     * Liczba osobników losowanych w selektorze turniejowym. Domyślnie: 10
     */
    public Integer tournamentSize;
    /**
     * Liczba pokoleń w algorytmie genetycznym. Domyślnie: 1000
     */
    public Integer numberOfGenerations;
    /**
     * Prawdopodobnieństwa krzyżowania rodziców. Domyślnie: 0.6
     */
    public Double crossingProbability;
    /**
     * Prawdopodobieństwo z jakim zostanie dokonana zamiana genów rodziców podczas krzyżowania. Domyślnie: 0.2
     */
    public Double swapProbability;
    /**
     * Prawdopodobieństo mutacji osobników po krzyżowaniu. Domyślnie: 0.2
     */
    public Double mutationProbability;
    /**
     * Rodzaj wybranego selektora: true - rulekta, false - turniej. Domyślnie: false
     */
    public boolean rouletteSelection;

    /**
     * Metoda weryfikująca poprawność przesłanych danych w zapytaniu HTTP
     *
     * @return null - jeśli dane są prawidłowe, wiadomość o błędzie - jeśli dane są nieprawidłowe
     */
    public String validate() {
        if (sizeX <= 0 || sizeX > 100)
            return "SizeX must be bigger than 0 and less or equal to 100.";
        if (sizeY <= 0 || sizeY > 100)
            return "SizeY must be bigger than 0 and less or equal to 100.";

        if (crossingPenalty != null && crossingPenalty < 0)
            return "Crossing penalty must be bigger or equal to 0.";

        if (pathLengthPenalty != null && pathLengthPenalty < 0)
            return "Path length penalty must be bigger or equal to 0.";

        if (numberOfSectionsPenalty != null && numberOfSectionsPenalty < 0)
            return "Number of sections penalty must be bigger or equal to 0.";

        if (pathsOutOfPcbPenalty != null && this.pathsOutOfPcbPenalty < 0)
            return "Paths out of PCB penalty must be bigger or equal to 0.";

        if (pathsOutOfPcbLengthPenalty != null && this.pathsOutOfPcbLengthPenalty < 0)
            return "Paths out of PCB length penalty must be bigger or equal to 0.";

        this.populationSize = populationSize != null ? populationSize : 100;

        if (this.populationSize < 1 || this.populationSize > 1000)
            return "Population size must be bigger than 0 and less or equal to 1000";

        if (tournamentSize != null && (this.tournamentSize < 1 || this.tournamentSize > this.populationSize))
            return "Tournament size must be bigger than 0 and less or equal to population size.";

        if (numberOfGenerations != null && (this.numberOfGenerations < 1 || this.numberOfGenerations > 100000))
            return "Number of generations must be bigger than 0 and less or equal to 100000.";

        if (starts == null)
            return "Starts parameter is mandatory";

        if (ends == null)
            return "Ends parameter is mandatory";

        if (starts.isEmpty())
            return "Starts can not be empty.";

        if (ends.isEmpty())
            return "Ends can not be empty.";

        if (starts.size() != ends.size())
            return "Starts' size and ends' size must be equal";

        List<Point> points = new ArrayList<>();
        List<Point> concatenated = new ArrayList<>();
        concatenated.addAll(starts);
        concatenated.addAll(ends);

        for (var point : concatenated) {
            if (point.getX() >= sizeX || point.getX() < 0 || point.getY() >= sizeY || point.getY() < 0)
                return "Coordinates must be bigger or equal to 0 and less than size in their dimension.";
            if (points.contains(point))
                return "Points must have unique coordinate pairs";
            points.add(point);
        }

        return null;
    }
}
