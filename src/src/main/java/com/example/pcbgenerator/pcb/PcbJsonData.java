package com.example.pcbgenerator.pcb;

import java.util.Arrays;

public class PcbJsonData {
    public int sizeX;
    public int sizeY;
    public int[] coordinates;
    public Integer crossingPenalty;
    public Integer pathLengthPenalty;
    public Integer numberOfSectionsPenalty;
    public Integer pathsOutOfPcbPenalty;
    public Integer pathsOutOfPcbLengthPenalty;
    public Integer populationSize;
    public Integer tournamentSize;
    public Integer numberOfGenerations;
    public Double crossingProbability;
    public Double swapProbability;
    public Double mutationProbability;
    public boolean rouletteSelection;

    public String validate() {
        if (sizeX <= 0 || sizeX > 100)
            return "SizeX must be bigger than 0 and less or equal to 100.";
        if (sizeY <= 0 || sizeY > 100)
            return "SizeY must be bigger than 0 and less or equal to 100.";

        if (crossingPenalty!=null && crossingPenalty < 0)
            return "Crossing penalty must be bigger or equal to 0.";

        if (pathLengthPenalty!=null && pathLengthPenalty < 0)
            return "Path length penalty must be bigger or equal to 0.";

        if (numberOfSectionsPenalty!=null && numberOfSectionsPenalty < 0)
            return "Number of sections penalty must be bigger or equal to 0.";

        if (pathsOutOfPcbPenalty!=null && this.pathsOutOfPcbPenalty < 0)
            return "Paths out of PCB penalty must be bigger or equal to 0.";

        if (pathsOutOfPcbLengthPenalty!=null && this.pathsOutOfPcbLengthPenalty < 0)
            return "Paths out of PCB length penalty must be bigger or equal to 0.";


        if (populationSize!=null && (this.populationSize < 1 || this.populationSize > 1000))
            return "Population size must be bigger than 0 and less or equal to 1000";

        if (tournamentSize!=null && (this.tournamentSize < 1 || this.tournamentSize > this.populationSize))
            return "Tournament size must be bigger than 0 and less or equal to population size.";

        if (numberOfGenerations!=null && (this.numberOfGenerations < 1 || this.numberOfGenerations > 100000))
            return "Number of generations must be bigger than 0 and less or equal to 100000.";

        if (coordinates.length % 4 != 0)
            return "The number of coordinates must be divisible by 4.";

        if(coordinates.length==0)
            return "Coordinates can not be empty.";

        var iterator = Arrays.stream(coordinates).iterator();
        while (iterator.hasNext()) {
            int x = iterator.next();
            int y = iterator.next();
            if (x >= sizeX || x < 0 || y >= sizeY || y < 0)
                return "Coordinates must be bigger or equal to 0 and less than size in their dimension.";
        }

        return null;
    }
}
