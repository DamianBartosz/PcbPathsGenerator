package com.example.pcbgenerator.genetic_elements;

import com.example.pcbgenerator.pcb.Pcb;

public class GeneticAlgorithm {
    public static Individual ga(Pcb pcb) {
        Population actPopulation = new Population(pcb);
        actPopulation.generateRandomPopulation();
        for (int i = 0; i < pcb.numberOfGenerations; i++) {
            actPopulation = actPopulation.evolve(true);
        }
        return actPopulation.getBestIndividual();
    }
}
