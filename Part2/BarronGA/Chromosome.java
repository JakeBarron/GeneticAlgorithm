package BarronGA;

import java.util.Random;

/**
	class Chromosome
		class to hold chromosome structure
	**/

public class Chromosome {
	private static Random random = new Random();
	private int Fitness;
	private int[] X;
	private int[] Y;
	private boolean[] color;
	private int size;

	public Chromosome(int size) {
		this.X = new int[size];
		this.Y = new int[size];
		this.color = new boolean[size];
		this.size = size;
	}

	public int getX(int index) {
		return X[index];
	}

	public int getY(int index) {
		return Y[index];
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
		int fitness = 0;



		return fitness;
	}

	@Override
	public String toString() {
		String string = "";
		for(int c = 0; c < this.size; c++) {
		string = string + String.format("| (%d, %d) |\n", getX(c), getY(c));
		}
		return string;
	}

}//end Chromsome