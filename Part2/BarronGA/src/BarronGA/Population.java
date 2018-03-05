/*@author Jake Barron
  @version 04/05/2018
   Class population
    this object is used to manage a variable population of chromosomes.
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
    
    public void sortPop() {
        this.pop.sort(null);
    }
    //set chromosome based on an index, overwriting previous element
    public void setChromosome(int index, Chromosome newChrom) {
        this.pop.add(index, newChrom);
        this.pop.sort(null);
    }
    //appends a chromosome to the end of the population
    public void addChromosome(Chromosome chrom) {
        pop.add(chrom);
        this.totalFitness += chrom.getFitness();
    } 
    /*
    @returns 1st element in population that is also fittest as the arraylist
    is ordered in ascending order of fitness
    */
    public Chromosome getFittest() {
        pop.sort(null);
        return pop.get(0);
    }
    
    public Chromosome getChromosome(int index) {
        return pop.get(index);
    }
    
    //@returns sum of fitness of all members of this population
    public int calculateTotalFitness() {
        int temp = 0;
        for(int i = 0; i <this.pop.size(); i++) {
            temp += this.pop.get(i).getFitness();
        }
        this.totalFitness = temp;
        return temp;
    }
    /*
    returns a chromosome in the population based on roulette wheel selection
    the roulette wheel favors fitter individuals
    */
    public Chromosome RouletteSelect() {
        this.calculateTotalFitness();
        int roulette = random.nextInt(Math.abs(this.totalFitness)-1);
        int index = 0;
        while(roulette > 0) {
            roulette = roulette + pop.get(index).getFitness();
            index++;
        }   
        return this.pop.get(index);
    }
    //returns a random chromosome
    public Chromosome getRandomChromosome() {
        return pop.get(random.nextInt(this.pop.size()));
    }
    
    
}
