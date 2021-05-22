package com.example.pcbgenerator.genetic_elements;

import com.example.pcbgenerator.pcb.Pcb;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Population {
    private Pcb pcb;
    private List<Individual> individuals;
    private double[] rouletteProbabilities;
    private double rouletteProbabilitiesSum;

    public Population(Pcb pcb) {
        this.pcb = pcb;
        individuals = new ArrayList<>();
    }

    public void generateRandomPopulation() {
        individuals.clear();
        for (int i = 0; i < pcb.populationSize; i++) {
            Individual newIndividual = new Individual(pcb);
            newIndividual.initIndividual();
            individuals.add(newIndividual);
        }
    }

    public Individual tournamentSelection() {
        Random random = new Random();
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < pcb.tournamentSize; i++) {
            int randIndex = random.nextInt(pcb.populationSize);
            if (indexes.contains(randIndex)) i--;
            else indexes.add(randIndex);
        }

        int bestIndex = indexes.get(0);
        int bestFitness = individuals.get(bestIndex).calcFitness();
        for (int i = 1; i < pcb.tournamentSize; i++) {
            int nextFitness = individuals.get(indexes.get(i)).calcFitness();
            if (nextFitness < bestFitness) {
                bestIndex = indexes.get(i);
                bestFitness = nextFitness;
            }
        }
        return individuals.get(bestIndex);
    }

    public Individual rouletteSelection() {
        if (rouletteProbabilities == null) {
            int[] fitnessList = new int[pcb.populationSize];
            int bestFitness = individuals.get(0).calcFitness();
            for (int i = 0; i < pcb.populationSize; i++) {
                int actFit = individuals.get(i).calcFitness();
                fitnessList[i] = actFit;
                if (bestFitness > actFit) {
                    bestFitness = actFit;
                }
            }

            rouletteProbabilities = new double[pcb.populationSize];
            for (int i = 0; i < pcb.populationSize; i++) {
                double actProb = ((double) bestFitness) / ((double) fitnessList[i]);
                rouletteProbabilitiesSum += actProb;
                rouletteProbabilities[i] = rouletteProbabilitiesSum;
            }
        }
        Random random = new Random();
        double randValue = random.nextDouble() * rouletteProbabilitiesSum;
        for (int i = 0; i < pcb.populationSize; i++) {
            if (randValue < rouletteProbabilities[i]) {
                return individuals.get(i);
            }
        }
        return individuals.get(pcb.populationSize - 1);
    }


    public Individual getBestIndividual() {
        Individual bestIndividual = individuals.get(0);
        int bestFitness = bestIndividual.calcFitness();
        for (var ind : individuals) {
            int indFitness = ind.calcFitness();
            if (indFitness < bestFitness) {
                bestFitness = indFitness;
                bestIndividual = ind;
            }
        }
        return bestIndividual;
    }

    public int getBestFitness() {
        int bestFitness = individuals.get(0).calcFitness();
        for (var ind : individuals) {
            int indFitness = ind.calcFitness();
            if (indFitness < bestFitness) {
                bestFitness = indFitness;
            }
        }
        return bestFitness;
    }

    public Population evolve(boolean roulette) {
        Population newPopulation = new Population(pcb);
        for (int i = 0; i < pcb.populationSize / 2; ++i) {
            Individual parentA = roulette ? rouletteSelection() : tournamentSelection();
            Individual parentB = roulette ? rouletteSelection() : tournamentSelection();
            var newIndividuals = Individual.cross(parentA, parentB);
            newIndividuals.getFirst().mutate();
            newIndividuals.getSecond().mutate();
            newPopulation.individuals.add(newIndividuals.getFirst());
            newPopulation.individuals.add(newIndividuals.getSecond());
        }
        return newPopulation;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }
}
