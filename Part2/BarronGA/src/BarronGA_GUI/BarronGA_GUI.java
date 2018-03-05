package BarronGA_GUI;

//javafx imports
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

//Chromosome and population package
import BarronGA.*;
/**
 * @author Jake Barron
 * @version 04/05/2018
 */
public class BarronGA_GUI extends Application {

    /*
    sets up initial stage adds buttons, text, and fills empty canvas
    */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BarronGA");
        
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(15, 15, 15, 15)); 
        
        Label label1 = new Label("Sequence:");
        gridPane.add(label1, 0, 0);
        //Sequence input
        TextField sequenceText = new TextField();
        gridPane.add(sequenceText, 1, 0);
        
        Label label2 = new Label("Desired Fitness:");
        gridPane.add(label2, 2, 0);
        TextField fitnessText = new TextField();
        gridPane.add(fitnessText, 3, 0);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(gridPane);
        
        Canvas canvas = new Canvas(500, 500);
        GraphicsContext gc = canvas.getGraphicsContext2D();        
        borderPane.setCenter(canvas);   
        
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        Button startBtn = new Button("Start");
        final Text actiontarget = new Text();
        HBox buttonRow = new HBox(startBtn, actiontarget);
        buttonRow.setAlignment(Pos.TOP_CENTER);
        buttonRow.setMinWidth(canvas.getWidth());
        buttonRow.setSpacing(50);
        borderPane.setBottom(buttonRow);
        
        //ACTION HANDLE
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent e) {
            String sequence = sequenceText.getText();
            int fitness = Integer.parseInt(fitnessText.getText());
            GeneticAlgorithm(sequence, fitness, gc);
           }
        });

        //gridPane.setGridLinesVisible(true);
        Scene scene = new Scene(borderPane, 600, 600, Color.RED);
        primaryStage.setScene(scene);
        primaryStage.show();      
    }//end start
    
    public void drawChromosome(GraphicsContext gc, Chromosome chromosome) {
        //clear and redraw canvas before drawing
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        //write fitness to canvas
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1);
        gc.strokeText("Fitness: " + chromosome.getFitness(), 10, 10, 100);
        //set line with and amino size when drawn
        gc.setLineWidth(3);
        int aminoSize = 12;
        //draw first amino acid at origin
        gc.setStroke(chromosome.getColor(0)? Color.BLACK : Color.WHITE);
        //get origin position and save for use in bond drawing
        double lastXPos = (0 + (gc.getCanvas().getWidth())*.5 + chromosome.getX(0)*2*aminoSize);
        double lastYPos = (0 + (gc.getCanvas().getHeight())*.5 + chromosome.getY(0)*2*aminoSize);
        gc.strokeOval(lastXPos, lastYPos, aminoSize, aminoSize);
                
        for(int i = 1; i < chromosome.getSize(); i++) {
            if(chromosome.getColor(i)) {
                gc.setStroke(Color.BLACK);
                double xPos = (gc.getCanvas().getWidth()*.5) + (chromosome.getX(i)*2*aminoSize);
                double yPos = (gc.getCanvas().getHeight()*.5) - (chromosome.getY(i)*2*aminoSize);
                gc.strokeOval(xPos, yPos, aminoSize, aminoSize); 
                gc.setStroke(Color.RED);
                gc.strokeLine(lastXPos+aminoSize/2, lastYPos+aminoSize/2, xPos+aminoSize/2, yPos+aminoSize/2);
                lastXPos = xPos;
                lastYPos = yPos;
                
            } else {
                gc.setStroke(Color.WHITE);
                double xPos = (gc.getCanvas().getWidth()*.5) + (chromosome.getX(i)*2*aminoSize);
                double yPos = (gc.getCanvas().getHeight()*.5) - chromosome.getY(i)*2*aminoSize;
                gc.strokeOval(xPos, yPos, aminoSize, aminoSize); 
                gc.setStroke(Color.RED);
                gc.strokeLine(lastXPos+aminoSize/2, lastYPos+aminoSize/2, xPos+aminoSize/2, yPos+aminoSize/2);
                lastXPos = xPos;
                lastYPos = yPos;
            }
        }        
    }//end drawChromosome
    
    public void GeneticAlgorithm(String sequence, int desiredFitness, GraphicsContext gc) {
        //these 3 constants must add up to 1.0
        final double CROSS_RATE = 0.80;
        final double ELITES = 0.10;
        final double MUT_RATE = 0.10;
        
        final int ITERATIONS = 1000;
        final int POP_SIZE = 200;
        final int DF = desiredFitness;
 
        Population pop1 = new Population(POP_SIZE);
        Population pop2 = new Population(POP_SIZE);
        
        //initialize pop 1
        for(int i = 0; i < POP_SIZE; i++){
            Chromosome chrom = new Chromosome(sequence);
            chrom.initializeChrom();
            pop1.addChromosome(chrom);
        }
        
        //counter to ensure non-deterministic algorithm will halt if no fittest
        //can be reached
        int counter = 0;
        while(pop1.getFittest().getFitness() > desiredFitness && counter < ITERATIONS){
            int index = 0;
            //put elites from pop1 into pop2 number carried over is based on ELITES constant
            while(index < (int)(POP_SIZE * ELITES)){
                pop2.setChromosome(index, pop1.getChromosome(index));
                index++;
            }  //end elite loop
            //add crossover chromosomes based on crossover constant
            while(index < (int)(POP_SIZE * ELITES) + (int)(POP_SIZE * CROSS_RATE)){
                //choose two mates with RouletteSelection and cross them over
                pop2.setChromosome(index, pop1.RouletteSelect().crossover(pop1.RouletteSelect()));
                index++;
            } //end crossover while loop

            //add mutated chromosomes to pop2 based on mutation constant
            while( index < (int)(POP_SIZE * ELITES) + (int)(POP_SIZE * CROSS_RATE) + (int)(POP_SIZE * MUT_RATE) ){
                pop2.setChromosome(index, pop1.getChromosome(index).mutate());
                index++;
            }
            drawChromosome(gc, pop2.getChromosome(POP_SIZE-1));
            //compare fitness of fittest individuals in pop1 and 2 if pop2 is fitter or equal
            //set it to pop 1 and begin again.
            pop1.sortPop(); pop2.sortPop();
            if(pop1.getFittest().getFitness() >= pop2.getFittest().getFitness()) {
                //System.out.println("pop2 fitter");
                pop1 = pop2;
            }
            if(counter%100 == 0)
                System.out.println(counter);
            counter++;
        } //end while
        //print fittest chromosome info and draw
        int fitness = pop1.getFittest().getFitness();
        System.out.println("Fittest Found");
        System.out.printf("Fittest chromosome fitness: %d", fitness);
        System.out.println(pop1.getFittest());
        drawChromosome(gc, pop1.getFittest());
    }//end main loop
    
}
// (0,0) (Canvas.Width, Canvas.Height)