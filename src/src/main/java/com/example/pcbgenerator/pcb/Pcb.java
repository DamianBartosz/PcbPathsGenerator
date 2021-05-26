package com.example.pcbgenerator.pcb;

import java.util.ArrayList;
import java.util.List;


public class Pcb {
    private final int sizeX;
    private final int sizeY;
    private final List<Point> starts;
    private final List<Point> ends;

    public final int crossingPenalty;
    public final int pathLengthPenalty;
    public final int numberOfSectionsPenalty;
    public final int pathsOutOfPcbPenalty;
    public final int pathsOutOfPcbLengthPenalty;

    public final int populationSize;
    public final int tournamentSize;
    public final int numberOfGenerations;

    public final double crossingProbability;
    public final double swapProbability;
    public final double mutationProbability;

    public final boolean rouletteSelection;


    public Pcb(int sizeX, int sizeY, List<Point> starts, List<Point> ends, Integer crossingPenalty, Integer pathLengthPenalty,
               Integer numberOfSectionsPenalty, Integer pathsOutOfPcbPenalty, Integer pathsOutOfPcbLengthPenalty,
               Integer populationSize, Integer tournamentSize, Integer numberOfGenerations, Double crossingProbability, Double swapProbability,
               Double mutationProbability, Boolean rouletteSelection) {
        if (sizeX <= 0 || sizeX > 100)
            throw new IllegalArgumentException("SizeX must be bigger than 0 and less or equal to 100.");
        this.sizeX = sizeX;
        if (sizeY <= 0 || sizeY > 100)
            throw new IllegalArgumentException("SizeY must be bigger than 0 and less or equal to 100.");
        this.sizeY = sizeY;

        this.crossingPenalty = crossingPenalty != null ? crossingPenalty : 1000;
        if (this.crossingPenalty < 0)
            throw new IllegalArgumentException("Crossing penalty must be bigger or equal to 0.");

        this.pathLengthPenalty = pathLengthPenalty != null ? pathLengthPenalty : 20;
        if (this.pathLengthPenalty < 0)
            throw new IllegalArgumentException("Path length penalty must be bigger or equal to 0.");

        this.numberOfSectionsPenalty = numberOfSectionsPenalty != null ? numberOfSectionsPenalty : 1;
        if (this.numberOfSectionsPenalty < 0)
            throw new IllegalArgumentException("Number of sections penalty must be bigger or equal to 0.");

        this.pathsOutOfPcbPenalty = pathsOutOfPcbPenalty != null ? pathsOutOfPcbPenalty : 2000;
        if (this.pathsOutOfPcbPenalty < 0)
            throw new IllegalArgumentException("Paths out of PCB penalty must be bigger or equal to 0.");

        this.pathsOutOfPcbLengthPenalty = pathsOutOfPcbLengthPenalty != null ? pathsOutOfPcbLengthPenalty : 1500;
        if (this.pathsOutOfPcbLengthPenalty < 0)
            throw new IllegalArgumentException("Paths out of PCB length penalty must be bigger or equal to 0.");


        this.populationSize = populationSize != null ? populationSize : 100;
        if (this.populationSize < 1 || this.populationSize > 1000)
            throw new IllegalArgumentException("Population size must be bigger than 0 and less or equal to 1000");

        this.tournamentSize = tournamentSize != null ? tournamentSize : this.populationSize > 10 ? 10 : populationSize;
        if (this.tournamentSize < 1 || this.tournamentSize > this.populationSize)
            throw new IllegalArgumentException("Tournament size must be bigger than 0 and less or equal to population size.");

        this.numberOfGenerations = numberOfGenerations != null ? numberOfGenerations : 1000;
        if (this.numberOfGenerations < 1 || this.numberOfGenerations > 100000)
            throw new IllegalArgumentException("Number of generations must be bigger than 0 and less or equal to 100000.");


        this.crossingProbability = crossingProbability != null ? crossingProbability : 0.6;
        this.swapProbability = swapProbability != null ? swapProbability : 0.2;
        this.mutationProbability = mutationProbability != null ? mutationProbability : 0.2;

        this.rouletteSelection = rouletteSelection != null ? rouletteSelection : false;

        if (starts.isEmpty())
            throw new IllegalArgumentException("Starts can not be empty.");

        if (ends.isEmpty())
            throw new IllegalArgumentException("Ends can not be empty.");

        if (starts.size() != ends.size())
            throw new IllegalArgumentException("Starts' size and ends' size must be equal");

        List<Point> points = new ArrayList<>();
        List<Point> concatenated = new ArrayList<>();
        concatenated.addAll(starts);
        concatenated.addAll(ends);

        for (var point : concatenated) {
            if (point.getX() >= sizeX || point.getX() < 0 || point.getY() >= sizeY || point.getY() < 0)
                throw new IllegalArgumentException("Coordinates must be bigger or equal to 0 and less than size in their dimension.");
            if (points.contains(point))
                throw new IllegalArgumentException("Points must have unique coordinate pairs");
            points.add(point);
        }

        this.starts = starts;
        this.ends = ends;
    }

    public Pcb(PcbJsonData pjd) {
        this(pjd.sizeX, pjd.sizeY, pjd.starts, pjd.ends, pjd.crossingPenalty, pjd.pathLengthPenalty, pjd.numberOfSectionsPenalty,
                pjd.pathsOutOfPcbPenalty, pjd.pathsOutOfPcbLengthPenalty, pjd.populationSize, pjd.tournamentSize,
                pjd.numberOfGenerations, pjd.crossingProbability, pjd.swapProbability, pjd.mutationProbability, pjd.rouletteSelection);
    }

    public Pcb() {
        this.sizeX = 10;
        this.sizeY = 10;
        this.starts = new ArrayList<>();
        this.ends = new ArrayList<>();

        starts.add(new Point(1, 2));
        starts.add(new Point(2, 2));

        this.crossingPenalty = 1000;
        this.pathLengthPenalty = 20;
        this.numberOfSectionsPenalty = 1;
        this.pathsOutOfPcbPenalty = 2000;
        this.pathsOutOfPcbLengthPenalty = 1500;

        this.populationSize = 100;
        this.tournamentSize = 10;
        this.numberOfGenerations = 1000;

        this.crossingProbability = 0.6;
        this.swapProbability = 0.2;
        this.mutationProbability = 0.2;

        this.rouletteSelection = false;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public List<Point> getStarts() {
        return starts;
    }

    public List<Point> getEnds() {
        return ends;
    }
}

