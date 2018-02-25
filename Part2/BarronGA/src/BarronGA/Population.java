/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BarronGA;

import java.util.ArrayList;

/**
 *
 * @author Jake Barron
 */
public class Population {
    private ArrayList<Chromosome> pop;
    private ArrayList<Chromosome> elites;
    private Chromosome fittest;
    
    //empty constructor
    public Population() {
        this.pop = new ArrayList<>();
        this.elites = new ArrayList<>();
        this.fittest = null;
    }
    
    public void addChromosome(Chromosome chrom) {
        pop.add(chrom);
    }
    
    
}
