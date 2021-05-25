package com.example.pcbgenerator.genetic_elements;

import com.example.pcbgenerator.pcb.Pcb;
import com.example.pcbgenerator.pcb.PcbPath;
import com.example.pcbgenerator.pcb.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import org.springframework.data.util.Pair;

public class Individual {
    private Pcb pcb;
    private List<PcbPath> paths;
    private int fitness;

    public Individual(Pcb pcb) {
        this.pcb = pcb;
        this.paths = new ArrayList<>();
        fitness = 0;
    }

    public Individual(Individual other) {
        this.pcb = other.pcb;
        this.paths = new ArrayList<>();
        for (var path : other.paths) {
            this.paths.add(new PcbPath(path));
        }
        fitness = 0;
    }

    public void initIndividual() {
        for (int i = 0; i < pcb.getStarts().size(); i++) {
            PcbPath path = new PcbPath(pcb.getSizeX(), pcb.getSizeY(), pcb.getStarts().get(i), pcb.getEnds().get(i));
            path.generateRandomPath();
            this.paths.add(path);
        }
    }

    public int calcFitness() {
        fitness = 0;
        int[][] crossArray = new int[pcb.getSizeX()][pcb.getSizeY()];

        for (var path : paths) {
            fitness += path.getPathLength() * pcb.pathLengthPenalty;

            final Point start = path.getStart();
            int x = start.getX();
            int y = start.getY();
            boolean isPathOutOfPcb = false;
            crossArray[x][y]++;

            for (var section : path.getSections()) {
                fitness += pcb.numberOfSectionsPenalty;
                int stepsLeft = section.getStep();
                while (stepsLeft > 0) {
                    switch (section.getDir()) {
                        case UP -> y--;
                        case DOWN -> y++;
                        case LEFT -> x--;
                        case RIGHT -> x++;
                    }
                    if (x < pcb.getSizeX() && x >= 0 && y < pcb.getSizeY() && y >= 0) {
                        crossArray[x][y]++;
                    } else {
                        fitness += pcb.pathsOutOfPcbLengthPenalty;
                        isPathOutOfPcb = true;
                    }
                    stepsLeft--;
                }
            }
            if (isPathOutOfPcb) fitness += pcb.pathsOutOfPcbPenalty;
        }
        int crossPoints = 0;
        for (int i = 0; i < pcb.getSizeX(); i++) {
            for (int j = 0; j < pcb.getSizeY(); j++) {
                crossPoints += crossArray[i][j] > 0 ? crossArray[i][j] - 1 : 0;
            }
        }
        fitness += crossPoints * pcb.crossingPenalty;
        return fitness;
    }

    @Override
    public String toString() {
        return "Individual{" +
                " paths=" + paths +
                ", fitness=" + fitness +
                '}';
    }

    public Pcb getPcb() {
        return pcb;
    }

    public List<PcbPath> getPaths() {
        return paths;
    }

    public void mutate() {
        Random random = new Random();
        if (random.nextDouble() > pcb.mutationProbability) return;
        paths.get(random.nextInt(paths.size())).mutate();
    }

    public static Pair<Individual, Individual> cross(Individual parentA, Individual parentB) {
        Random random = new Random();
        double crossDecision = random.nextDouble();
        if (crossDecision < parentA.pcb.crossingProbability) return Pair.of(parentA, parentB);

        List<PcbPath> pathsA = new ArrayList<>();
        List<PcbPath> pathsB = new ArrayList<>();
        for (int i = 0; i < parentA.paths.size(); i++) {
            if (random.nextDouble() > parentA.pcb.swapProbability) {
                pathsA.add(new PcbPath(parentA.paths.get(i)));
                pathsB.add(new PcbPath(parentB.paths.get(i)));
            } else {
                pathsB.add(new PcbPath(parentA.paths.get(i)));
                pathsA.add(new PcbPath(parentB.paths.get(i)));
            }
        }
        Individual newA = new Individual(parentA.pcb);
        newA.paths = pathsA;
        Individual newB = new Individual(parentB.pcb);
        newB.paths = pathsB;
        return Pair.of(newA, newB);
    }
}
