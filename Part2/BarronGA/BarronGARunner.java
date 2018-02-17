package BarronGA;

import java.util.Scanner;
import java.io.*;

/**
	@Author Jake Barron
	@Version 1.0
	A genetic algorithm based structural search program.
**/

/**
TODO:
	0. data structure for chromosome \
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

	public static void main(String[] args) {

	String sequence = args[0];
		for(int i = 0; i < 100; i++) {
			Chromosome testChrome = new Chromosome(sequence);
			testChrome.initializeChrom();
			System.out.print("size: " + testChrome.getSize());
			System.out.print(testChrome);

			testChrome.computeFitness();
		}
	}

}
