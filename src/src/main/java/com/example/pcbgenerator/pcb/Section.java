package com.example.pcbgenerator.pcb;

public class Section {
    private Direction dir;
    private int step;

    public Section(Direction dir, int step) {
        this.dir = dir;
        this.step = step;
    }

    public Section(Section other){
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