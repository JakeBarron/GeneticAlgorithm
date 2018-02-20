package BarronGA;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;

/**
	@Author Jake Barron
	@Version 1.0
	A genetic algorithm based structural search program.
**/

/**
TODO:
	1. Initialization and validation (Pop1) \
	2. Compute Fitness (Pop1, for all chromosome Ci)
	3. Sort (Pop1)
	4. Examine: C1/Progress or Max_gen, Exit()?
	5. Elite(5 to 10%); Pop2 <- Pop1
	6. Crossover (~80%); Pop2(remaining)  Pop1
	7. Fillup Pop2 randomly (remaining, if any)
	8. Mutation (Pop2(Non_Elite)); 5% to 50%
	9. Pop1 <- Pop2; gen = gen+1
	10. Go to Step #2.
**/

public class BarronGARunner {
	private static final String sequence = "hphpphhphpphphhpphph";
	private static final int DesiredFitness = -8;
	private static final int POPSIZE = 200;
	//sorted by fitness. 
	private static ArrayList<Chromosome> pop1;
	//2nd generation
	private static ArrayList<Chromosome> pop2;

	public static void main(String[] args) {
		//String sequence = args[0];
		pop1 = new ArrayList<Chromosome>();


		//fill population 1 with chromosomes
		//initialization and validation and compute fitness
		for(int i = 0; i < POPSIZE; i++) {
			Chromosome chromosome = new Chromosome(sequence);
			chromosome.initializeChrom();
			pop1.add(chromosome);
		}//end for
		//sort (pop1)
		pop1.sort(null);

		//examine C1 progress. if desired reached, exit.
		if((pop1.get(0)).getFitness() <= DesiredFitness) {
			System.out.println("Desired Fitness reached. Program ended.");
			System.out.println(pop1.get(0));
			System.exit(0);
		}

		for(Chromosome chromosome : pop1) {
			System.out.println(chromosome.getFitness());
		}



	} //end main

}//end class
