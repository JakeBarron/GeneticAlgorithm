package BarronGA;

import java.util.Random;

/**
	class Chromosome
		class to hold chromosome structure
	**/

public class Chromosome {
	private static Random random = new Random();
	private int fitness;
	private int[] X;
	private int[] Y;
	private boolean[] color;
	private int size;
	private String sequence;

	public Chromosome(String sequence) {
		this.size = sequence.length();
		this.X = new int[this.size];
		this.Y = new int[this.size];
		this.color = new boolean[this.size];
		this.sequence = sequence;
		this.fitness = 0;
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
	//Method to give chromosome initial legal structure
	public void initializeChrom() {
		//always initialize to (0,0)
		X[0] = 0;
		Y[0] = 0;
		//then (0,1)
		X[1] = 0;
		X[1] = 1;
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
	} //end initializeChrom

	private boolean validate() {
		boolean isValid = true;
		for(int i = 0; i < size; i++) {	
			for(int j = i+1; j < size-1; j++) {
				if(X[i] == X[j] && Y[i] == Y[j]) {
					isValid = false;
					System.out.println("invalid structure! REPEATING.");
					break;
				}
			}//end inner for
		} //end outer for
		return isValid;
	}

	public int computeFitness() {
		boolean[][] chromosomeGraph = new boolean[this.size*2][this.size*2];
		for(int i = 0; i < this.size; i++) {
			if(color[i]) {
				chromosomeGraph[X[i]+size][Y[i]+size] = true;
			}
		}
		//print
		for (int i = 0; i < chromosomeGraph.length; i++) {
    		for (int j = 0; j < chromosomeGraph[i].length; j++) {
        		System.out.print(chromosomeGraph[i][j] + " ");
    	}
    	System.out.println();
		}
		return fitness;
	}

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

}//end Chromsome