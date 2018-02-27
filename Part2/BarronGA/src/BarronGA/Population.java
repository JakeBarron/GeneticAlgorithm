/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BarronGA;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jake Barron
 */
public class Population {
    private static final Random random = new Random();
    private ArrayList<Chromosome> pop;
    private Chromosome fittest;
    private int totalFitness;
    
    //empty constructor
    public Population(int size) {
        this.pop = new ArrayList<>(size);
        this.fittest = null;
        this.totalFitness = 0;
    }
    
    public void addChromosome(Chromosome chrom) {
        pop.add(chrom);
        this.totalFitness += chrom.getFitness();
    } 
    
    public Chromosome getFittest() {
        pop.sort(null);
        return pop.get(0);
    }
    
    public Chromosome getChromosome(int index) {
        return pop.get(index);
    }
    //returns a chromosome in the population based on roulette wheel selection
    public Chromosome RouletteSelect() {
        int roulette = random.nextInt(Math.abs(this.totalFitness));
        int index = 0;
        while(roulette < 0) {
            roulette = pop.get(index).getFitness();
        }
        return null;
    }
    
    
}
