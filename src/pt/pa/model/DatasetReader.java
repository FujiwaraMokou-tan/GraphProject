package pt.pa.model;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.pa.graph.Graph;
import pt.pa.graph.GraphAdjacencyList;
import pt.pa.graph.GraphEdgeList;
import pt.pa.graph.Vertex;
import pt.pa.graphicInterface.Alerts;
import pt.pa.graphicInterface.GraphicalUI;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DatasetReader {

    
    /**
     * @param filepath String variable which contains the filepath to read the name.txt
     * @param size Size of the Array of Strings which will store all the names of each future vertex's Hub
     * @return returns an array of Strings which will contain all the names
     */
    public String[] readNameFile(String filepath, int size) throws Alerts {
        String[] names = new String[size];
        File file = new File(filepath + "/name.txt");
        int counter = 0;
        Scanner reader = null;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new Alerts("Please choose one of the existing datasets.", "Non-existent dataset!");
        }
        while (reader.hasNextLine()){
            names[counter] = reader.nextLine();
            counter++;
        }
        return names;
    }

    /**
     * @param filepath String variable which contains the filepath to read the weight.txt
     * @param size Size of the Array of ints which will store all the weights of each future vertex's Hub
     * @return returns an array of ints which will contain all the weights
     */
    public int[] readWeightFile(String filepath, int size) throws Alerts{
        int[] weights = new int[size];
        File file = new File(filepath + "/weight.txt");
        Scanner reader = null;
        int counter = 0;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new Alerts("Please choose one of the existing datasets.", "Non-existent dataset!");
        }
        while (reader.hasNextLine()){
            weights[counter] =  Integer.parseInt(reader.nextLine());
            counter++;
        }
        return weights;
    }

    /**
     * @param filepath String variable which contains the filepath to read the xy.txt
     */
    public Positions[] readXY(String filepath, int size) throws Alerts{
        Positions[] positions = new Positions[size];
        File file = new File(filepath + "/xy.txt");
        Scanner reader = null;
        int counter = 0;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new Alerts("Please choose one of the existing datasets.", "Non-existent dataset!");
        }
        while (reader.hasNextLine()){
            String[] split = reader.nextLine().split("\\s+");
            positions[counter] = new Positions(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
            counter++;
        }
        return positions;
    }

    /**
     * @param filepath - String variable which contains the filepath to read the routes.txt
     *                 This method will gather the entire list of routes into a double array so we can later say which
     *                 vertex is connected to which
     */
    public int[][] readRoutesFile(String filepath, int size) throws Alerts{
        int[][] routes = new int[size][size];
        File file = new File(filepath);
        String num = "";
        int counter = 0;
        int xPosition = 0;
        String line = "";
        Scanner reader = null;
        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new Alerts("Please choose one of the existing datasets.", "Non-existent dataset!");
        }
        while (reader.hasNextLine()){
            line = reader.nextLine();
            num = "";
            for(int i = 0; i < line.length(); i++){
                if(i == line.length()-1){
                    num += line.charAt(i);
                    routes[counter][xPosition] = Integer.parseInt(num);
                }
                else if(line.charAt(i) != ' ')
                    num += line.charAt(i);
                else{
                    routes[counter][xPosition] = Integer.parseInt(num);
                    num = "";
                    xPosition++;
                }
            }
            counter++;
            xPosition = 0;
        }
        return routes;
    }
}
