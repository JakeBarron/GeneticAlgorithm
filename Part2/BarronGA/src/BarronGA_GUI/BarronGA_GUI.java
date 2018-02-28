package BarronGA_GUI;

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

import BarronGA.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
/**
 *
 * @author Jake Barron
 */
public class BarronGA_GUI extends Application {

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
            actiontarget.setFill(Color.RED);
            actiontarget.setText("Processing . . .");
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
        //these 3 constants should add up to 1.0
        final double CROSSOVER = 0.80;
        final double ELITES = 0.10;
        final double MUT_RATE = 0.10;
        
        final int POP_SIZE = 10;
        final int DF = desiredFitness;
 
        Population pop1 = new Population(POP_SIZE);
        Population pop2 = new Population(POP_SIZE);
        
        //initialize pop 1
        for(int i = 0; i < POP_SIZE; i++){
            Chromosome chrom = new Chromosome(sequence);
            chrom.initializeChrom();
            pop1.addChromosome(chrom);
        }
        
        //draw fittest
        //drawChromosome(gc, pop1.getFittest());

        //examine
        if(pop1.getFittest().getFitness() <= desiredFitness){
            int fitness = pop1.getFittest().getFitness();
            System.out.println("Fittest Found");
            System.out.printf("Fittest chromosome fitness: %d", fitness);
            System.out.println(pop1.getFittest());
            System.exit(0);
        }
        
        Chromosome test = pop1.RouletteSelect().crossover(pop1.RouletteSelect());
        drawChromosome(gc, test);
        
//        //put elites from pop1
//        int index = 0;
//        while(index < (int)(POP_SIZE * ELITES)){
//            pop2.addChromosome(pop1.getChromosome(index));
//            index++;
//        }  //end elite loop
//        //crossover
//        while(index < (int)(POP_SIZE * ELITES) + (int)(POP_SIZE * CROSSOVER)){
//            //choose two mates with RouletteSelection and cross them over
//            pop2.addChromosome(pop1.RouletteSelect().crossover(pop1.RouletteSelect()));
//            index++;
//        } //end crossover while loop
        
        //fillup pop 2 with remaining
//        while(index < POP_SIZE) {
//            pop2.addChromosome(pop1.getChromosome(index));
//        }
//        drawChromosome(gc, pop2.getChromosome(POP_SIZE-1));
        //mutate pop 2 non elite
        
        //pop1 now equals pop2 gen = gen+1
        
        //go to step 2
        
    }
    
}
// (0,0) (Canvas.Width, Canvas.Height)