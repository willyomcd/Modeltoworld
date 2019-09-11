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

    private double[][] vertices = new double[4][];
    private ArrayList<String []> faces = new ArrayList< String []>();

    public Model (Driver driver){

        double[][] matrixData = { {1d,2d,3d}, {2d,5d,3d}};
        RealMatrix m = MatrixUtils.createRealMatrix(matrixData);

        File obj = new File(driver.getModel());
        System.out.println(obj);

        try {
            Scanner scanner = new Scanner(obj);
            int vCounter = 0;
            while(scanner.hasNext()){
                if(scanner.next().equals("v")){
                    System.out.println("seen v");
                    double x = scanner.nextDouble();
                    double y = scanner.nextDouble();
                    double z = scanner.nextDouble();
                    System.out.println(x +" " + y +" "+ z);
                    vertices[0][vCounter] = x;
                    vertices[1][vCounter] = y;
                    vertices[2][vCounter] = z;
                    vertices[3][vCounter] = 0;

                    vCounter++;
                }
                if(scanner.next() == "f"){
                    String first = scanner.next();
                    String second = scanner.next();
                    String third = scanner.next();
                    String [] face = new String [3];
                    face[0] = first;
                    face[1] = second;
                    face[2] = third;
                    faces.add(face);

                }
            }
            System.out.println(this.toString());
            performManipulations(driver);

        }catch (IOException e) {
            System.exit(2);
        }
    }
    public void performManipulations(Driver d){

    }
    public String toString(){
        return Arrays.toString(vertices) + faces.toString();
    }

}
