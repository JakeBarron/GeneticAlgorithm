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

	Integer test = 0;
	test++;
	System.out.println(test);

	Chromosome testChrome = new Chromosome(10);
	testChrome.initializeChrom();
	System.out.print(testChrome);
	}

}