package BarronGA;

import java.util.Random;

/**
	class Chromosome
		class to hold chromosome structure
	**/

public class Chromosome implements Comparable<Chromosome> {
	private static Random random = new Random();
	private int fitness;
	private int[] X;
	private int[] Y;
	private boolean[] color;
	private int[] folds;
	private int size;
	private String sequence;

	public Chromosome(String sequence) {
		this.size = sequence.length();
		this.X = new int[this.size];
		this.Y = new int[this.size];
		this.color = new boolean[this.size];
		//holds directional history
		this.folds = new int[this.size];
		this.sequence = sequence;
		this.colorSequence();
	}

	public int getX(int index) {
		return this.X[index];
	}

	public int getY(int index) {
		return this.Y[index];
	}

	public boolean getColor(int index) {
		return this.color[index];
	}

	public int getSize() {
		return this.size;
	}

	public int[] getFolds() {
		return this.folds;
	}

	public int getFitness() {
		return this.fitness;
	}

	//Method to give chromosome initial legal structure and then calls compute fitness
	public void initializeChrom() {
		//always initialize to (0,0)
		X[0] = 0;
		Y[0] = 0;
		folds[0] = 0;
		//then (0,1)
		X[1] = 0;
		X[1] = 1;
		folds[1] = 1;
		//integers to hold last and next directions
		int lastDirection = 1;
		int nextDirection = 0;

		do{
			//Valid next direction depends on previous direction
			for(int i = 2; i < size; i++) {
				if(lastDirection == 1) {
					int choice = random.nextInt(3);
					switch(choice) {
						case 0: nextDirection = 1;
								break;
						case 1: nextDirection = 3;
								break;
						case 2: nextDirection = 4;
								break;
					} //end switch		
				} else if(lastDirection == 2) {
					int choice = random.nextInt(3);
					switch(choice) {
						case 0: nextDirection = 2;
								break;
						case 1: nextDirection = 3;
								break;
						case 2: nextDirection = 4;
								break;
					} //end switch

				} else if(lastDirection == 3) {
					int choice = random.nextInt(3);
					switch(choice) {
						case 0: nextDirection = 1;
								break;
						case 1: nextDirection = 2;
								break;
						case 2: nextDirection = 3;
								break;
					} //end switch
				} else if(lastDirection == 4) {
					int choice = random.nextInt(3);
					switch(choice) {
						case 0: nextDirection = 1;
								break;
						case 1: nextDirection = 2;
								break;
						case 2: nextDirection = 4;
								break;
					}//end switch	
				} //end if else
				//switch for next direction
				//System.out.println(nextDirection);
				folds[i] = nextDirection;
				switch(nextDirection) {
					case 1: this.X[i] = (this.X[i-1]) + 1;
							this.Y[i] = this.Y[i-1];
							break;
					case 2: this.X[i] = (this.X[i-1]) - 1;
							this.Y[i] = this.Y[i-1];
							break;
					case 3: this.Y[i] = (this.Y[i-1]) + 1;
							this.X[i] = this.X[i-1];
							break;
					case 4: this.Y[i] = (this.Y[i-1]) - 1;
							this.X[i] = this.X[i-1];
							break;
				} //end switch for next direction
				//set previous direction to next direction
				lastDirection = nextDirection;
			} //end for
		}while(!validate());//end while
		this.computeFitness();
	} //end initializeChrom

	/*
		validate
		@returns isValid whether or not the structure is legal
	*/
	private boolean validate() {
		boolean isValid = true;
		for(int i = 0; i < size; i++) {	
			for(int j = i+1; j < size; j++) {
				if(X[i] == X[j] && Y[i] == Y[j]) {
					isValid = false;
					//System.out.println("invalid structure! REPEATING.");
					break;
				}
			}//end inner for
		} //end outer for
		return isValid;
	}
	/*
	method: computeFitness
	@returns fitness of this chromosome (between 0 and -2147483648)
	*/
	public int computeFitness() {
		//compute initial fitness, assuming valid folds
		for(int i = 0; i < this.sequence.length()-1; i++) {
			if((this.sequence.charAt(i) == 'h') && (this.sequence.charAt(i+1) == 'h')) {
				//fitness is negative and bonded black amino acids are guaranteed to reduce fitness.
				this.fitness++;
			}
		}
		//construct matrix structure
		boolean[][] chromosomeGraph = new boolean[this.size*2][this.size*2];
		for(int i = 0; i < this.size; i++) {
			if(color[i]) {
				chromosomeGraph[X[i]+size][Y[i]+size] = true;
			}
		}

		// //scan matrix to look for adjacent unbonded blacks
		for(int i = 0; i < (size*2) - 1; i++){
			for(int j = 0; j < size*2 - 1; j++) {
				//if current amino acid is black
				if(chromosomeGraph[i][j]) {
					//if 1 forward or 1 down is also black decrement fitness
					if(chromosomeGraph[i+1][j])
						this.fitness--;
					if(chromosomeGraph[i][j+1])
						this.fitness--;
				}
			}
		}
		//for testing
		//visualizeChromosome();
		return fitness;
	}//end computeFitness

	private void colorSequence() {
		for(int i = 0; i < this.size; i++) {
			color[i] = (this.sequence.charAt(i) == 'h');
		}
	}

	@Override
	public String toString() {
		String string = "";
		for(int c = 0; c < this.size; c++) {
		string = string + String.format("| (%d, %d, %s) |\n", this.getX(c), this.getY(c), (this.getColor(c) ? "black" : "white"));
		}
		return string;
	}

	public void visualizeChromosome() {
		//construct matrix structure for print testing vvvvvvvvvvvvvvvv
		int[][] printableMatrix = new int[this.size*2][this.size*2];
		//construct testing matrix
		for(int i = 0; i < (size*2); i++){
			for(int j = 0; j < size*2; j++) {
				printableMatrix[i][j] = 2;
			}
		}

		for(int i = 0; i < this.size; i++) {
			if(color[i]) {
				printableMatrix[X[i]+size][Y[i]+size] = 1;
			} else {
				printableMatrix[X[i]+size][Y[i]+size] = 0;
			} 
		}	
		for (int i = 0; i < printableMatrix.length; i++) {
			System.out.print("|");
    		for (int j = 0; j < printableMatrix[i].length; j++) {
    			switch(printableMatrix[i][j]){
    				case 0: System.out.printf("%s|", "w");
    						break;
    				case 1: System.out.printf("%s|", "b");
    						break;
    				case 2: System.out.printf("%s|", " ");
    			}
        		//System.out.printf("%s|" ,chromosomeGraph[i][j] ? "b" : " ");
        		//System.out.printf("[%d, %d]", i, j);
    		}
    		System.out.print("\n");
		} 
	}//end visualizeChromosome

	public int compareTo(Chromosome other) {
		//ascending order
		return this.fitness - other.getFitness();
		//descending order
		//return other.getFitness() - this.fitness;
	}

}//end Chromsome