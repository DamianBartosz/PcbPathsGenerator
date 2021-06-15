package com.example.pcbgenerator.pcb;

/**
 * Klasa reprezentująca segment ścieżki na płytce drukowanej
 */
public class Section {
    /**
     * Kierunek segmentu
     */
    private Direction dir;

    /**
     * Długość segmentu w danym kierynku
     */
    private int step;

    /**
     * Konstrukor segmentu o określonym kierunku i długości
     *
     * @param dir  Kierunek segmentu
     * @param step Długość segmentu w danym kierynku
     */
    public Section(Direction dir, int step) {
        this.dir = dir;
        this.step = step;
    }

    /**
     * Konstruktor kopiujący segmentu
     * @param other segment kopiowany
     */
    public Section(Section other) {
        this.dir = other.dir;
        this.step = other.step;
    }

    public Direction getDir() {
        return dir;
    }

    public int getStep() {
        return step;
    }

    public void setDir(Direction dir) {
        this.dir = dir;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public String toString() {
        return "Section{" +
                "dir=" + dir +
                ", step=" + step +
                '}';
    }
}