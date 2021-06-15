package com.example.pcbgenerator.genetic_elements;

import com.example.pcbgenerator.pcb.Pcb;


public class GeneticAlgorithm {

    /**
     * Metoda generuje losową populację dla zadanej płytki drukowanej. Nastepnie populacja jest poddawana ewolucji. Liczba ewolucji zapisana jest w parametrze "numberOfGenerations" (domyślnie 1000).
     * @param pcb parametr zawiera dane płytki drukowanej oraz parametrów algorytmu genetycznego, w oparciu o które generowane są osobniki.
     * @return Najlepsze uzyskane rozwiązanie w ostatnim pokoleniu.
     */
    public static Individual ga(Pcb pcb) {
        Population actPopulation = new Population(pcb);
        actPopulation.generateRandomPopulation();
        for (int i = 0; i < pcb.numberOfGenerations; i++) {
            actPopulation = actPopulation.evolve();
        }
        return actPopulation.getBestIndividual();
    }
}
