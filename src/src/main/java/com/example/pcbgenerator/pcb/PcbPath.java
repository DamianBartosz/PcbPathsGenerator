package com.example.pcbgenerator.pcb;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * Klasa reprezenująca ścieżkę na płytce drukowanej
 */
public class PcbPath {
    /**
     * Szerokość płytki
     */
    private int sizeX;
    /**
     * Wysokość płytki
     */
    private int sizeY;
    /**
     * Punkt początkowy ścieżki
     */
    private Point start;
    /**
     * Punkt końcowy ścieżki
     */
    private Point end;
    /**
     * Lista sekcji tworzących ścieżkę
     */
    private List<Section> sections;

    /**
     * Konstruktor ścieżki dla zadanych parametrów
     * @param sizeX Szerokość płytki
     * @param sizeY Wysokość płytki
     * @param start Punkt początkowy ścieżki
     * @param end Punkt końcowy ścieżki
     */
    public PcbPath(int sizeX, int sizeY, Point start, Point end) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.start = start;
        this.end = end;
        this.sections = new ArrayList<>();
    }

    /**
     * Konstruktor kopiujący ścieżki
     * @param other Kopiowana ścieżka
     */
    public PcbPath(PcbPath other) {
        this.sizeX = other.sizeX;
        this.sizeY = other.sizeY;
        this.start = other.start;
        this.end = other.end;
        this.sections = new ArrayList<>();
        for (var section : other.sections) {
            this.sections.add(new Section(section));
        }
    }

    /**
     * Metoda generująca ścieżkę w sposób losowy
     */
    public void generateRandomPath() {
        Random random = new Random();
        int x = start.getX();
        int y = start.getY();

        if (random.nextInt(2) > 0) {
            int step = calcSectionInDimension(x, end.getX(), y, end.getY(), true, sizeX);
            if (step == 0) return;
            x += step;
        }
        while (true) {
            int step = calcSectionInDimension(y, end.getY(), x, end.getX(), false, sizeY);
            if (step == 0) break;
            y += step;

            step = calcSectionInDimension(x, end.getX(), y, end.getY(), true, sizeX);
            if (step == 0) break;
            x += step;
        }
        optimizePath();
    }

    /**
     * Metoda losująca następny segment w danym wymiarze.
     * @param actDim współrzędna w danym wymiarze punktu w którym ma zostać dodany segment. Jeśli segment jest horyzontalny to jest to wartość x punktu, w przeciwnym wypadku jest y
     * @param actEnd współrzędna w danym wymiarze punktu końcowego ścieżki. Jeśli segment jest horyzontalny to jest to wartość x punktu, w przeciwnym wypadku jest y
     * @param secDim współrzędna w drugim wymiarze punktu w którym ma zostać dodany segment. Jeśli segment jest horyzontalny to jest to wartość y punktu, w przeciwnym wypadku jest x
     * @param secEnd współrzędna w drugim wymiarze punktu końcowego ścieżki. Jeśli segment jest horyzontalny to jest to wartość y punktu, w przeciwnym wypadku jest x
     * @param horizontally parametr określający czy dany segment jest horyzontalny
     * @param size wielkość płytki w danym wymiarze. Jeśli segment jest horyzontalny jest wartość sizeX, w przeciwnym wypadku sizeY
     * @return liczbę kroków wykonanych w danym wymiarze
     */
    private int calcSectionInDimension(int actDim, int actEnd, int secDim, int secEnd, boolean horizontally, int size) {
        Random random = new Random();
        int factor = actEnd - actDim;
        if (factor == 0) factor++;
        factor *= random.nextInt(4) + 1;
        int maxStep = factor < 0 ? actDim : size - actDim - 1;
        if (maxStep == 0) {
            factor *= -1;
            maxStep = size - 1;
        }
        int step = random.nextInt(maxStep) + 1;
        Direction dir;
        if (horizontally) dir = factor > 0 ? Direction.RIGHT : Direction.LEFT;
        else dir = factor > 0 ? Direction.DOWN : Direction.UP;
        if (secDim == secEnd && abs(actEnd - actDim) <= step && (actEnd - actDim) * factor > 0) {
            sections.add(new Section(dir, abs(actEnd - actDim)));
            return 0;
        }
        sections.add(new Section(dir, step));
        return factor > 0 ? step : -step;
    }

    /**
     * Metoda optymalizująca ścieżkę poprzez usunięcie zapętleń oraz scalenie następujacych po sobie segmentów w tym samym wymiarze.
     */
    private void optimizePath() {
        Point point = checkForLoops();
        while (point != null) {
            List<Section> newSections = new ArrayList<>();
            int x = start.getX();
            int y = start.getY();
            boolean deleting = false;
            boolean deleted = false;
            if (point.getX() == x && point.getY() == y) {
                deleting = true;
            }
            for (var section : sections) {
                if (deleted)
                    newSections.add(section);
                else {
                    int stepsLeft = section.getStep();
                    while (stepsLeft > 0) {
                        switch (section.getDir()) {
                            case UP -> y--;
                            case DOWN -> y++;
                            case LEFT -> x--;
                            case RIGHT -> x++;
                        }
                        stepsLeft--;
                        if (!deleted) {
                            if (point.getX() == x && point.getY() == y) {
                                if (!deleting) {
                                    deleting = true;
                                    newSections.add(new Section(section.getDir(), section.getStep() - stepsLeft));
                                } else {
                                    deleted = true;
                                    newSections.add(new Section(section.getDir(), stepsLeft));
                                }
                            }
                        }
                    }
                    if (!deleting) {
                        newSections.add(section);
                    }
                }
            }
            sections = newSections;
            point = checkForLoops();
        }

        deleteDoubleSteps();

    }

    /**
     * Metoda wyszukująca zapętleń
     * @return punkt w którym następuje zapętlenie
     */
    private Point checkForLoops() {
        int[][] crossArray = new int[sizeX][sizeY];

        int x = start.getX();
        int y = start.getY();
        crossArray[x][y]++;
        for (var section : sections) {
            int steps_left = section.getStep();
            while (steps_left > 0) {
                switch (section.getDir()) {
                    case UP -> y--;
                    case DOWN -> y++;
                    case LEFT -> x--;
                    case RIGHT -> x++;
                }
                if (x >= 0 && x < sizeX && y >= 0 && y < sizeY) {
                    crossArray[x][y]++;
                    if (crossArray[x][y] > 1) return new Point(x, y);
                }
                steps_left--;
            }
        }
        return null;
    }

    /**
     * Metoda scalająca następujące po sobie segmenty w tym samym wymiarze
     */
    private void deleteDoubleSteps() {
        List<Section> newSections = new ArrayList<>();
        for (var section : sections) {
            if (section.getStep() > 0) {
                if (newSections.size() > 0 && newSections.get(newSections.size() - 1).getDir() == section.getDir()) {
                    newSections.get(newSections.size() - 1).setStep(newSections.get(newSections.size() - 1).getStep() + section.getStep());
                    if (newSections.get(newSections.size() - 1).getStep() == 0) {
                        newSections.remove(newSections.size() - 1);
                    }
                } else {
                    newSections.add(section);
                }
            }
        }
        sections = newSections;
    }

    /**
     * Metoda mutująca ścieżkę. Wybiera losowy segment ścieżki i następnie przesuwa jego fragment o wylosowaną wartość
     */
    public void mutate() {
        Random random = new Random();
        int sectionIndex = random.nextInt(sections.size());
        Section it = sections.get(sectionIndex);
        int breakPoint = random.nextInt(it.getStep() + 1);
        int shiftedFragment = random.nextInt(2);
        int dir = random.nextInt(2);
        int step = random.nextInt(min(sizeX, sizeY) + 1);
        Section firstSection = new Section(Direction.UP, step);
        Section secondSection = new Section(Direction.DOWN, step);
        if (it.getDir() == Direction.UP || it.getDir() == Direction.DOWN) {
            if (dir == 1) {
                firstSection.setDir(Direction.RIGHT);
                secondSection.setDir(Direction.LEFT);
            } else {
                firstSection.setDir(Direction.LEFT);
                secondSection.setDir(Direction.RIGHT);
            }
        } else {
            if (dir == 1) {
                firstSection.setDir(Direction.DOWN);
                secondSection.setDir(Direction.UP);
            }
        }

        if (shiftedFragment == 0) {
            sections.add(sections.indexOf(it), firstSection);
            sections.add(sections.indexOf(it), new Section(it.getDir(), breakPoint));
            sections.add(sections.indexOf(it), secondSection);
            sections.add(sections.indexOf(it), new Section(it.getDir(), it.getStep() - breakPoint));
        } else {
            sections.add(sections.indexOf(it), new Section(it.getDir(), breakPoint));
            sections.add(sections.indexOf(it), firstSection);
            sections.add(sections.indexOf(it), new Section(it.getDir(), it.getStep() - breakPoint));
            sections.add(sections.indexOf(it), secondSection);
        }
        sections.remove(it);
        optimizePath();
    }

    public int getPathLength() {
        int pathLength = 0;
        for (var section : sections) {
            pathLength += section.getStep();
        }
        return pathLength;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public List<Section> getSections() {
        return sections;
    }

    @Override
    public String toString() {
        return "PcbPath{" +
                "start=" + start +
                ", end=" + end +
                ", sections=" + sections +
                '}';
    }
}

