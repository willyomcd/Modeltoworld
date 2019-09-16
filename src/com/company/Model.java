package com.company;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Model {

    private ArrayList<double []> verticeList = new ArrayList<>();
    private ArrayList<String []> faces = new ArrayList< String []>();

    public Model (Driver driver){

        File obj = new File(driver.getModel());
        System.out.println(obj);
        readFile(obj);
        double[][] verticeListForLibrary = convertVerticeListForLibrary();
        //System.out.println(Arrays.deepToString(verticeListForLibrary));
        RealMatrix matrix = MatrixUtils.createRealMatrix(verticeListForLibrary);
       // System.out.println(matrix.toString());
        matrix = matrix.transpose();
        //System.out.println(matrix.toString());
        matrix = performManipulations(driver,matrix);
        outputFiles();

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
                    vertex[3] = 1.0;
                    verticeList.add(vertex);
                    //System.out.println(x + " " + y + " " + z);
                    //System.out.println(Arrays.deepToString(verticeList.toArray()));
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
                    //System.out.println(Arrays.deepToString(faces.toArray()));

                }
            }
        }catch (IOException e) {
            System.exit(2);
        }

    }
    //converts Arraylist of double [] to double[][] for library use
    private double[][] convertVerticeListForLibrary(){
        double [][] returnList = new double[verticeList.size()][4];
        for(int i = 0; i < returnList.length; i++){
            double[] vertex = verticeList.get(i);
            returnList[i] = vertex;
        }
        return returnList;
    }
    private RealMatrix performManipulations(Driver d, RealMatrix matrix){
        //matrix = rotate(d,matrix);
        //matrix = scale(d, matrix);
        //System.out.println(matrix.toString());
        matrix = translate(d,matrix);

        return  matrix;
    }
    public String toString(){
        return Arrays.toString(faces.toArray());
    }
    private RealMatrix scale (Driver d, RealMatrix matrix){
        double scale = d.getScale();
        System.out.println("scale factor:" + scale);
        RealMatrix scaleID = MatrixUtils.createRealIdentityMatrix(4);
        scaleID = scaleID.scalarMultiply(scale);
        double [] bottumRow = new double[] {0.0,0.0,0.0,1.0};
        scaleID.setRow(3,bottumRow);
        System.out.println( " scale matrix : " + scaleID.toString());
        matrix = scaleID.multiply(matrix);
        System.out.println( " After Scale : " + matrix.toString());
        return matrix;
    }
    private RealMatrix translate (Driver d, RealMatrix matrix){
        double [] translate = d.getTranslation();
        RealMatrix iD = MatrixUtils.createRealIdentityMatrix(4);

        iD.setColumn(iD.getColumnDimension() -1, translate);
        System.out.println("id creation" + iD.toString());
        double [] test = iD.getColumn(iD.getColumnDimension()-1);
        System.out.println(matrix.toString());
        matrix = iD.multiply(matrix);
        System.out.println(matrix.toString());


        return matrix;
    }
    private RealMatrix rotate (Driver d, RealMatrix matrix){
        return matrix;
    }
    private void outputFiles() {

    }


}
