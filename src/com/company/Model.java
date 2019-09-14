package com.company;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Model {

    private ArrayList<double []> verticeList = new ArrayList<>();
    private ArrayList<String []> faces = new ArrayList< String []>();

    public Model (Driver driver){

        double[][] matrixData = { {1d,2d,3d}, {2d,5d,3d}};
        File obj = new File(driver.getModel());
        System.out.println(obj);
        readFile(obj);
        double[][] verticeListForLibrary = convertVerticeListForLibrary();
        System.out.println(Arrays.deepToString(verticeListForLibrary));
        RealMatrix matrix = MatrixUtils.createRealMatrix(verticeListForLibrary);
        System.out.println(matrix.toString());
        matrix = matrix.transpose();
        System.out.println(matrix.toString());
        matrix = performManipulations(driver,matrix);

    }
    private void readFile(File obj) {
        try {

            Scanner scanner = new Scanner(obj);
            while(scanner.hasNext()){
                String lineToken = scanner.next();
                if(lineToken.equals("v")){
                    double[] vertex = new double[4];
                    double x = scanner.nextDouble();
                    double y = scanner.nextDouble();
                    double z = scanner.nextDouble();
                    vertex[0] = x;
                    vertex[1] = y;
                    vertex[2] = z;
                    vertex[3] = 0.0;
                    verticeList.add(vertex);
                    //System.out.println(x + " " + y + " " + z);
                    System.out.println(Arrays.deepToString(verticeList.toArray()));
                    continue;
                }
                if(lineToken.equals("f")){
                    String first = scanner.next();
                    String second = scanner.next();
                    String third = scanner.next();
                    String [] face = new String [3];
                    face[0] = first;
                    face[1] = second;
                    face[2] = third;
                    faces.add(face);
                    System.out.println(Arrays.deepToString(faces.toArray()));

                }
            }
        }catch (IOException e) {
            System.exit(2);
        }

    }
    //converts Arraylist of double [] to double[][] for library use
    public double[][] convertVerticeListForLibrary(){
        double [][] returnList = new double[verticeList.size()][4];
        for(int i = 0; i < returnList.length; i++){
            double[] vertex = verticeList.get(i);
            returnList[i] = vertex;
        }
        return returnList;
    }
    public RealMatrix performManipulations(Driver d, RealMatrix matrix){
       matrix = scale(d, matrix);
       matrix = translate(d,matrix);
       matrix = rotate(d,matrix);
        return  matrix;
    }
    public String toString(){
        return Arrays.toString(faces.toArray());
    }
    public RealMatrix scale (Driver d, RealMatrix matrix){
        return matrix;
    }
    public RealMatrix translate (Driver d, RealMatrix matrix){
        return matrix;
    }
    public RealMatrix rotate (Driver d, RealMatrix matrix){
        return matrix;
    }

}
