package BarronGA;

import java.util.Random;
/**
	class Chromosome
		class to hold chromosome structure
	**/

public class Chromosome implements Comparable<Chromosome> {
	private static  final Random random = new Random();
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
	}//end sequence constructor
        //copy constructor
        public Chromosome(Chromosome other) {
            this.size = other.getSize();
            this.X = new int[other.getSize()];
            this.Y = new int[other.getSize()];
            this.color = new boolean[other.getSize()];
            this.folds= new int[other.getSize()];
            this.sequence = other.getSequence();
            for(int i = 0; i < other.getSize(); i++) {
                this.X[i] = other.getX(i);
                this.Y[i] = other.getY(i);
                this.color[i] = other.getColor(i);
                this.folds[i] = other.getFold(i);
            }
            this.computeFitness();
        }//end copy constructor

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
        
        public int getFold(int index) {
            return this.folds[index];
        }

	public int getFitness() {
		return this.fitness;
	}
        
        public String getSequence() {
            return this.sequence;
        }
        
        public void setX(int index, int x) {
            X[index] = x;
        }
        
        public void setY(int index, int y) {
            X[index] = y;
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
	public boolean validate() {
		boolean isValid = true;
		for(int i = 0; i < size; i++) {	
			for(int j = i+1; j < size; j++) {
				if(X[i] == X[j] && Y[i] == Y[j]) {
                                    isValid = false;
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
                this.fitness = 0;
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
		return this.fitness;
	}//end computeFitness

	private void colorSequence() {
		for(int i = 0; i < this.size; i++) {
			color[i] = (this.sequence.charAt(i) == 'h');
		}
	}
      //CROSSOVER  
        public Chromosome crossover(Chromosome other) {
            Chromosome crossed = new Chromosome(this);
            int fixedPoint = random.nextInt(this.size-2) + 2;
            int direction = 0;
            do {
                if(direction == 0){
                    for(int p = fixedPoint+1; p < other.getSize(); p++) {
                        //move other points to origin
                        crossed.X[p] = other.X[p] - other.X[fixedPoint];
                        crossed.Y[p] = other.Y[p] - other.Y[fixedPoint];
                        
                        //move back to fixed point
                        crossed.X[p] += this.X[fixedPoint];
                        crossed.Y[p] += this.Y[fixedPoint];
                    }
                } else if(direction == 1) {
                    //rotate 90 degrees counter-clockwise
                    for(int p = fixedPoint+1; p < other.getSize(); p++) {
                        //move other points to origin
                        crossed.X[p] = other.X[p] - other.X[fixedPoint];
                        crossed.Y[p] = other.Y[p] - other.Y[fixedPoint];
                        //rotate
                        int tempX = crossed.Y[p]*-1;
                        int tempY = crossed.X[p];
                        crossed.X[p] = tempX;
                        crossed.Y[p] = tempY;
                        //move back to fixed point
                        crossed.X[p] += this.X[fixedPoint];
                        crossed.Y[p] += this.Y[fixedPoint];
                    } 
                } else if(direction == 2) {
                    //rotate 180 degrees counter-clockwise
                    for(int p = fixedPoint+1; p < other.getSize(); p++) {
                        //move other points to origin
                        crossed.X[p] = other.X[p] - other.X[fixedPoint];
                        crossed.Y[p] = other.Y[p] - other.Y[fixedPoint];
                        //rotate
                        int tempX = crossed.X[p]*-1;
                        int tempY = crossed.Y[p]*-1;
                        crossed.X[p] = tempX;
                        crossed.Y[p] = tempY;
                        //move back to fixed point
                        crossed.X[p] += this.X[fixedPoint];
                        crossed.Y[p] += this.Y[fixedPoint];
                    }
                } else if(direction == 3) {
                    //rotate 270 degrees
                    for(int p = fixedPoint+1; p < other.getSize(); p++) {
                        //move other points to origin
                        crossed.X[p] = other.X[p] - other.X[fixedPoint];
                        crossed.Y[p] = other.Y[p] - other.Y[fixedPoint];
                        //rotate
                        int tempX = crossed.Y[p];
                        int tempY = crossed.X[p]*-1;
                        crossed.X[p] = tempX;
                        crossed.Y[p] = tempY;
                        //move back to fixed point
                        crossed.X[p] += this.X[fixedPoint];
                        crossed.Y[p] += this.Y[fixedPoint];
                    }
                }
                if(crossed.validate()) {
                    crossed.computeFitness();
                    return crossed;
                }
                direction++;
            }while(direction > 4);
                return new Chromosome(this);
        } //end crossover
        
        public Chromosome mutate() {
            Chromosome mutated = new Chromosome(this);
            int fixedPoint = random.nextInt(this.size-2) + 2;
            int direction = 1;
            do {
                if(direction == 1) {
                    //rotate 90 degrees counter-clockwise
                    for(int p = fixedPoint+1; p < this.size; p++) {
                        //move other points to origin
                        mutated.X[p] = this.X[p] - this.X[fixedPoint];
                        mutated.Y[p] = this.Y[p] - this.Y[fixedPoint];
                        //rotate
                        int tempX = mutated.Y[p]*-1;
                        int tempY = mutated.X[p];
                        mutated.X[p] = tempX;
                        mutated.Y[p] = tempY;
                        //move back to fixed point
                        mutated.X[p] += this.X[fixedPoint];
                        mutated.Y[p] += this.Y[fixedPoint];
                    } 
                } else if(direction == 2) {
                    //rotate 180 degrees counter-clockwise
                    for(int p = fixedPoint+1; p < this.size; p++) {
                        //move other points to origin
                        mutated.X[p] = this.X[p] - this.X[fixedPoint];
                        mutated.Y[p] = this.Y[p] - this.Y[fixedPoint];
                        //rotate
                        int tempX = mutated.X[p]*-1;
                        int tempY = mutated.Y[p]*-1;
                        mutated.X[p] = tempX;
                        mutated.Y[p] = tempY;
                        //move back to fixed point
                        mutated.X[p] += this.X[fixedPoint];
                        mutated.Y[p] += this.Y[fixedPoint];
                    }
                } else if(direction == 3) {
                    //rotate 270 degrees
                    for(int p = fixedPoint+1; p < this.size; p++) {
                        //move other points to origin
                        mutated.X[p] = this.X[p] - this.X[fixedPoint];
                        mutated.Y[p] = this.Y[p] - this.Y[fixedPoint];
                        //rotate
                        int tempX = mutated.Y[p];
                        int tempY = mutated.X[p]*-1;
                        mutated.X[p] = tempX;
                        mutated.Y[p] = tempY;
                        //move back to fixed point
                        mutated.X[p] += this.X[fixedPoint];
                        mutated.Y[p] += this.Y[fixedPoint];
                    }
                }
                if(mutated.validate()) {
                    //if mutated gene is valid, compute its fitness and return it
                    mutated.computeFitness();
                    return mutated;
                }
                direction++;
            }while(direction > 4);
            //if no valid folding exists, return original chromosome
                return this;
        }//end mutate

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
        //natural order of chromosome is based on |fitness|
	public int compareTo(Chromosome other) {
		//ascending order
		return this.fitness - other.getFitness();
		//descending order
		//return other.getFitness() - this.fitness;
	}
}//end Chromsome