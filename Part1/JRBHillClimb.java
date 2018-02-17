import java.util.BitSet;
import java.util.Random;
import java.util.PriorityQueue;
import java.util.Comparator;

/**********************************************
	@Author Jake Barron
	@Version 01
	Machine Learning II: Homework 1 : Part 1
	JRBHillClimb is a hill climbing algorithm 
	to find the maximum-value of a function f, 
	where f = |12 * one (v) -160|
	Here, v is the input binary variable of 
	40 bits and the one counts the number of ‘1’s
	in v. Set MAX = 100, and thus reset algorithm 
	100 times for the global maximum and print 
	the found maximum-value for each reset 
	separated by comma.
***********************************************/

public class JRBHillClimb {
	//number of times to repeat
	private static final Integer MAX = 100;
	//random generator
	private static final Random randomGenerator = new Random();

	public static void main(String[] args) {

		//procedure iterated hillclimber
		//begin
			//repeat MAX times
			for(int i = 0; i < MAX; i++) {
				//local = false
				boolean local = false;
				//select a current string vc at random
				BitSet v = generateV();
				//until local = true
				while(!local) {
					//priority queue to hold neighborhood
					PriorityQueue<BitSet> neighborhood = new PriorityQueue<BitSet>(40, new Comparator<BitSet>() {
						public int compare(BitSet a, BitSet b) {
							//Max priority queue as opposed to min
							return (generateHeuristic(b.cardinality()) - generateHeuristic(a.cardinality()));
						}
					});
					//select 30 new strings in the neighborhood of vc by flipping single bits
					for(int c = 0; c < 40; c++) {
						BitSet neighbor = flipRandomBit(v);
						neighborhood.add(neighbor);
					}
					//select the string vnew from the set of new strings with the largest heuristic
					BitSet VNew = neighborhood.poll();
					//if f(vc) < f(vn)
					if( generateHeuristic(v.cardinality()) < generateHeuristic(VNew.cardinality()) ) {
						//then vc = vn
						v = VNew;
					}
					//else local = true
					else{
						local = true;
					}
				} //end while
				System.out.printf("%d,", generateHeuristic(v.cardinality()));
			} //end for MAX
		//end
	} //end main


	private static BitSet generateV() {
		//initialize BitSet with 40 initial 0s
		BitSet v = new BitSet(40);

		for(int i = 0; i < 40; i++) {
			//flip a random bit;
			v.set(i, randomGenerator.nextBoolean());
		}

		return v;
	} //end Method generateV

	//flip one random bit
	private static BitSet flipRandomBit(BitSet v) {
		BitSet modified = (BitSet)v.clone();
		//flip one random bit in v to its complement
		modified.flip(randomGenerator.nextInt(40));
		return modified;
	}

	//generate heuristic
	private static int generateHeuristic(int cardinality) {
		return Math.abs(12 * cardinality - 160);
	}

} //end JRBHillClimb class