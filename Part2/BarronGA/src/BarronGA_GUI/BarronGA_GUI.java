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
            Chromosome fittest = BarronGARunner.BeginGeneticAlgorithm(sequence, fitness);
            //clear and refill background of canvas
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            //draw fittest chromosome
            drawChromosome(gc, fittest);
               
           }
        });

        //gridPane.setGridLinesVisible(true);
        Scene scene = new Scene(borderPane, 600, 600, Color.RED);
        primaryStage.setScene(scene);
        primaryStage.show();      
    }//end start
    
    public void drawChromosome(GraphicsContext gc, Chromosome chromosome) {
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
                double xPos = (0 + (gc.getCanvas().getWidth())*.5 + chromosome.getX(i)*2*aminoSize);
                double yPos = (0 + (gc.getCanvas().getHeight())*.5 + chromosome.getY(i)*2*aminoSize);
                gc.strokeOval(xPos, yPos, aminoSize, aminoSize); 
                gc.setStroke(Color.RED);
                gc.strokeLine(lastXPos+aminoSize/2, lastYPos+aminoSize/2, xPos+aminoSize/2, yPos+aminoSize/2);
                lastXPos = xPos;
                lastYPos = yPos;
                
            } else {
                gc.setStroke(Color.WHITE);
                double xPos = (0 + (gc.getCanvas().getWidth())*.5 + chromosome.getX(i)*2*aminoSize);
                double yPos = (0 + (gc.getCanvas().getHeight())*.5 + chromosome.getY(i)*2*aminoSize);
                gc.strokeOval(xPos, yPos, aminoSize, aminoSize); 
                gc.setStroke(Color.RED);
                gc.strokeLine(lastXPos+aminoSize/2, lastYPos+aminoSize/2, xPos+aminoSize/2, yPos+aminoSize/2);
                lastXPos = xPos;
                lastYPos = yPos;
            }
        }
    }
    
}
// (0,0) (Canvas.Width, Canvas.Height)