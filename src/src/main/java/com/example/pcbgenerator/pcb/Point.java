package com.example.pcbgenerator.pcb;

import java.util.Objects;

/**
 * Klasa reprezentująca punkt na płytce drukowanej
 */
public class Point {
    /**
     * Współrzędna x punktu
     */
    private int x;

    /**
     * Współrzędna y punktu
     */
    private int y;

    /**
     * Konstruktor punktu o określonych współrzędnych
     * @param x Współrzędna x punktu
     * @param y Współrzędna y punktu
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
