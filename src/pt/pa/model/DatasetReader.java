package pt.pa.model;

import pt.pa.graphicInterface.Alerts;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class serves to read/access the Dataset
 */
public class DatasetReader {

    
    /**
     * This method reads the file name of our Dataset
     * @param filepath String variable which contains the filepath to read the name.txt
     * @param size Size of the Array of Strings which will store all the names of each future vertex's Hub
     * @return returns an array of Strings which will contain all the names
     * @throws Alerts - an Exception which will alert the user of the file isn't found
     */
    public String[] readNameFile(String filepath, int size) throws Alerts {
        String[] names = new String[size];
        File file = new File(filepath + "/name.txt");
        int counter = 0;
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()){
                names[counter] = reader.nextLine();
                counter++;
            }
        } catch (FileNotFoundException e) {
            throw new Alerts("Please choose one of the existing datasets.", "Non-existent dataset!");
        }
        return names;
    }

    /**
     * This method reads the Weight file of the Dataset
     * @param filepath String variable which contains the filepath to read the weight.txt
     * @param size Size of the Array of ints which will store all the weights of each future vertex's Hub
     * @return returns an array of ints which will contain all the weights in the text file
     * @throws Alerts - an Exception which will alert the user of the file isn't found
     */
    public int[] readWeightFile(String filepath, int size) throws Alerts{
        int[] weights = new int[size];
        File file = new File(filepath + "/weight.txt");
        int counter = 0;
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()){
                weights[counter] =  Integer.parseInt(reader.nextLine());
                counter++;
            }
        } catch (FileNotFoundException e) {
            throw new Alerts("Please choose one of the existing datasets.", "Non-existent dataset!");
        }
        return weights;
    }

    /**
     * This method reads the xy file of the Dataset
     * @param filepath String variable which contains the filepath to read the xy.txt
     * @param size Size of the Array of ints which will store all the Positions of each future vertex's Hub
     * @return returns an Array of Positions containing all the positions of the vertexes
     * @throws Alerts - an Exception which will alert the user of the file isn't found
     */
    public Positions[] readXY(String filepath, int size) throws Alerts{
        Positions[] positions = new Positions[size];
        File file = new File(filepath + "/xy.txt");
        int counter = 0;
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()){
                String[] split = reader.nextLine().split("\\s+");
                positions[counter] = new Positions(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
                counter++;
            }
        } catch (FileNotFoundException e) {
            throw new Alerts("Please choose one of the existing datasets.", "Non-existent dataset!");
        }
        return positions;
    }

    /**
     * This method reads the Route file of the Dataset
     * @param filepath String variable which contains the filepath to read the Route.txt
     * @param size Size of the Array of ints which will store all the routes of each future vertex's Hub
     * @return returns a bi-dimensional Array of ints containing all the connections/distances between the vertexes
     * @throws Alerts - an Exception which will alert the user of the file isn't found
     */
    public int[][] readRoutesFile(String filepath, int size) throws Alerts{
        int[][] routes = new int[size][size];
        File file = new File(filepath);
        String num;
        String line;
        int counter = 0;
        int xPosition = 0;
        try {
            Scanner reader = new Scanner(file);
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
        } catch (FileNotFoundException e) {
            throw new Alerts("Please choose one of the existing datasets.", "Non-existent dataset!");
        }
        return routes;
    }
}
