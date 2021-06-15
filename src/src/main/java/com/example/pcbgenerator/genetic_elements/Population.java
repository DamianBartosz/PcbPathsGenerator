package com.example.pcbgenerator.genetic_elements;

import com.example.pcbgenerator.pcb.Pcb;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Klasa Population reprezentuje pojedyńczą populację osobników. Liczba osobników w populacji jest równa parametrowi populationSize w instancji Pcb.
 */
public class Population {

    /**
     * Płytka drukowana w oparciu o którą generowane są rozwiązania.
     */
    private Pcb pcb;

    /**
     * Lista osobników (rozwiązań)
     */
    private List<Individual> individuals;

    /**
     * Tablica zawierająca szanse wyboru poszczególnych osobników dla selektora ruletki.
     */
    private double[] rouletteProbabilities;

    /**
     * Liczba będąca sumą szans wyboru wszystkich osobników dla selektora ruleki.
     */
    private double rouletteProbabilitiesSum;

    /**
     * Konstruktor populacji dla zadanego pcb
     *
     * @param pcb płytka drukowna w oparciu o którą będą generowane rozwiązania
     */
    public Population(Pcb pcb) {
        this.pcb = pcb;
        individuals = new ArrayList<>();
    }

    /**
     * Metoda generująca populację losowo utworzonych rozwiązań
     */
    public void generateRandomPopulation() {
        individuals.clear();
        for (int i = 0; i < pcb.populationSize; i++) {
            Individual newIndividual = new Individual(pcb);
            newIndividual.initIndividual();
            individuals.add(newIndividual);
        }
    }

    /**
     * Selektor turniejowy. Losuje grupę (jej wielkość zależna od parametru tournamentSize w instancji Pcb) osobników w populacji i wybiera z nich tego o najlepszej ocenie.
     *
     * @return Najlepszy osobnik sposród wybranych
     */
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

    /**
     * Selektor ruletki. Oblicza szanse (odwrotnie proporcjonalna do wartości funkcji oceniającej) wylosowania każdego osobnika i w oparciu o nie wykonuje losowanie
     *
     * @return wylosowany osobnik
     */
    public Individual rouletteSelection() {
        //Obliczanie szans odbywa się raz na populację
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

    /**
     * Wyszukuje osobnika o najniższej wartości funkcji oceniającej
     *
     * @return osobnik o najlepszym dopasowaniu
     */
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

    /**
     * Metoda do ewolucji populacji. Wybiera osobników rodziców i po ich skrzyżowaniu uzyskuje osobniki potomne, które następnie zostają poddane mutacji i dodane do nowej populacji
     *
     * @return populacja utworzona poprzez ewolucje tej populacji
     */
    public Population evolve() {
        Population newPopulation = new Population(pcb);
        for (int i = 0; i < (pcb.populationSize + 1) / 2; ++i) {
            Individual parentA = pcb.rouletteSelection ? rouletteSelection() : tournamentSelection();
            Individual parentB = pcb.rouletteSelection ? rouletteSelection() : tournamentSelection();
            var newIndividuals = Individual.cross(parentA, parentB);
            newIndividuals.getFirst().mutate();
            newIndividuals.getSecond().mutate();
            newPopulation.individuals.add(newIndividuals.getFirst());
            if (newPopulation.individuals.size() < pcb.populationSize)
                newPopulation.individuals.add(newIndividuals.getSecond());
        }
        return newPopulation;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }
}
