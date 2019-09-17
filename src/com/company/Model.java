package com.company;

import org.apache.commons.math3.geometry.euclidean.threed.Rotation;
import org.apache.commons.math3.geometry.euclidean.threed.RotationConvention;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Vector;

public class Model {

    private ArrayList<double []> verticeList = new ArrayList<>();
    private ArrayList<String []> faces = new ArrayList< String []>();
    private String lineHeader0;
    private String lineHeader1;
    private String sLine;
    private RealMatrix startMatrix;
    private RealMatrix finalMatrix;
    private RealMatrix transformationMatrix;
    private RealMatrix inverseTransformationMatrix;

    public Model (Driver driver){

        File obj = new File(driver.getModelName());
        //System.out.println(obj);
        readFile(obj);
        double[][] verticeListForLibrary = convertVerticeListForLibrary();
        //System.out.println(Arrays.deepToString(verticeListForLibrary));
        RealMatrix matrix = MatrixUtils.createRealMatrix(verticeListForLibrary);
        startMatrix = MatrixUtils.createRealMatrix(verticeListForLibrary);
        startMatrix = startMatrix.transpose();
       // System.out.println(matrix.toString());
        matrix = matrix.transpose();
       // System.out.println("start" + matrix.toString());
        matrix = performManipulations(driver,matrix);
        finalMatrix = matrix;

    }
    private void readFile(File obj) {
        try {
            Scanner scanner = new Scanner(obj);
            lineHeader0 = scanner.nextLine();
            lineHeader1 = scanner.nextLine();
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
                if(lineToken.equals("s")){
                    sLine = scanner.next();
                    //System.out.println(sLine);
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
        transformationMatrix = MatrixUtils.createRealIdentityMatrix(4);
         RealMatrix rotateM = rotate(d,transformationMatrix);
         RealMatrix scaleM = scale(d,transformationMatrix);
        RealMatrix translateM = translate(d,transformationMatrix);
        transformationMatrix = rotateM.multiply(scaleM.multiply(translateM));
        inverseTransformationMatrix = MatrixUtils.inverse(transformationMatrix);


        matrix = rotate(d,matrix);
        matrix = scale(d, matrix);
        matrix = translate(d,matrix);
        //System.out.println("After everything" + matrix.toString());
        //System.out.println("transformation matrix: " + transformationMatrix.toString());
        //System.out.println("inverse transformation matrix: " + inverseTransformationMatrix.toString());


        return  matrix;
    }
    public String toString(){
        return Arrays.toString(faces.toArray());
    }
    private RealMatrix scale (Driver d, RealMatrix matrix){
        double scale = d.getScale();
        //System.out.println("scale factor:" + scale);
        RealMatrix scaleID = MatrixUtils.createRealIdentityMatrix(4);
        scaleID = scaleID.scalarMultiply(scale);
        double [] bottumRow = new double[] {0.0,0.0,0.0,1.0};
        scaleID.setRow(3,bottumRow);
       // System.out.println( " scale matrix : " + scaleID.toString());
        matrix = scaleID.multiply(matrix);
        //System.out.println( " After Scale : " + matrix.toString());
        return matrix;
    }
    private RealMatrix translate (Driver d, RealMatrix matrix){
        double [] translate = d.getTranslation();
        RealMatrix iD = MatrixUtils.createRealIdentityMatrix(4);

        iD.setColumn(iD.getColumnDimension() -1, translate);
        //System.out.println("id creation" + iD.toString());
        double [] test = iD.getColumn(iD.getColumnDimension()-1);
       // System.out.println(matrix.toString());
        matrix = iD.multiply(matrix);
       // System.out.println(matrix.toString());


        return matrix;
    }
    private RealMatrix rotate (Driver d, RealMatrix matrix){
        double[] axis = d.getAxis();
        Vector3D wVector = new Vector3D(axis);
        wVector = wVector.normalize();
       // System.out.println("normal axis: " + wVector.toString());
        Vector3D mVector = createMVector(wVector);
        //System.out.println("mvector: " + mVector.toString());
        Vector3D uVector = mVector.crossProduct(wVector);
        uVector = uVector.normalize();
        //System.out.println("uvector: " + uVector);
        Vector3D vVector = wVector.crossProduct(uVector);
       // System.out.println("vVector: " + vVector);

        double [][] rotation = new double[4][4];
        double [] homoRow = new double[]{0,0,0,1};
        double [] uRow = new double[]{uVector.getX(),uVector.getY(),uVector.getZ(),0.0};
        double [] vRow = new double[]{vVector.getX(),vVector.getY(),vVector.getZ(),0.0};
        double [] wRow = new double[]{wVector.getX(),wVector.getY(),wVector.getZ(),0.0};

        rotation[0] = uRow;
        rotation[1] = vRow;
        rotation[2] = wRow;
        rotation[3] = homoRow;

        RealMatrix rotationMatrix = MatrixUtils.createRealMatrix(rotation);
        //System.out.println("R: " + rotationMatrix.toString());

        double theta = d.getAngle();
        double thetaRadians = Math.toRadians(theta);
        double sinTheta =  Math.sin(thetaRadians);
        double cosTheta = Math.cos(thetaRadians);

        //System.out.println("sin: " + sinTheta);
        //System.out.println("cos: " + cosTheta);

        double [][] rotateAboutZ = {{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}};
        rotateAboutZ[0][0] = cosTheta;
        rotateAboutZ[1][1] = cosTheta;
        rotateAboutZ[0][1] = -sinTheta;
        rotateAboutZ[1][0] = sinTheta;
        rotateAboutZ[2][2] = 1;
        rotateAboutZ[3][3] = 1;
        RealMatrix rotateAroundZ = MatrixUtils.createRealMatrix(rotateAboutZ);
        //System.out.println(rotateAroundZ.toString());

        RealMatrix fullTransform = rotationMatrix.transpose().multiply(rotateAroundZ);
        fullTransform= fullTransform.multiply(rotationMatrix);
        //System.out.println(fullTransform);

        matrix = fullTransform.multiply(matrix);
        //System.out.println("after rotation: " + matrix.toString());

        return matrix;
    }

    private Vector3D createMVector(Vector3D normalAxis) {
        double min = normalAxis.getX();
        int place = 0;
        if(normalAxis.getY() <= min){min = normalAxis.getY(); place = 1;}
        if(normalAxis.getZ() <= min){min = normalAxis.getZ(); place = 2;}
        double[] mAxisArray = new double[] {normalAxis.getX(),normalAxis.getY(),normalAxis.getZ()};
        mAxisArray[place] = 1.0;
        Vector3D mAxis= new Vector3D(mAxisArray);

        return mAxis;
    }
    public RealMatrix getFinalMatrix(){
        return finalMatrix;
    }
    public String getsLine(){
        return sLine;
    }
    public ArrayList<String []> getFaces(){
        return faces;
    }

    public void outputFile(Driver d, File folder){

        File outfile = new File(folder,d.getModelName().substring(0,d.getModelName().length()-4) + "_mw0"+d.getModelNumber() + ".obj");
        if(!outfile.getParentFile().exists()) {
            outfile.getParentFile().mkdirs();
        }
        //System.out.println(outfile.getAbsolutePath());
        try {
            FileWriter stream = new FileWriter(outfile);
            stream.write(lineHeader0 + '\n');
            stream.write(lineHeader1 + '\n');
            for (int i = 0; i < finalMatrix.getColumnDimension(); i++) {
                stream.write('v' + " ");
                for(int j = 0; j < finalMatrix.getRowDimension() -1; j++){
                    double formatter = finalMatrix.getEntry(j,i);
                    String formatted = String.format("%.6f", formatter);
                    stream.write(formatted + " ");
                }
                stream.write('\n');
            }
            stream.write("s "+ sLine+ "\n");
            for(int i = 0 ;  i<faces.size(); i++ ){
                stream.write("f ");
                for(int j = 0; j < faces.get(i).length;j++){
                    stream.write(faces.get(i)[j] + " ");
                }
                stream.write('\n');
            }
            stream.close();


        }catch (IOException f){
            System.exit(3);
        }

        File transOutfile = new File(folder,d.getModelName().substring(0,d.getModelName().length()-4) + "_transform_mw0"+d.getModelNumber() + ".txt");
        try{
            FileWriter stream = new FileWriter(transOutfile);
            stream.write("# Transformation matrix \n");
            for (int i = 0; i < transformationMatrix.getColumnDimension(); i++) {
                for(int j = 0; j < transformationMatrix.getRowDimension() -1; j++){
                    double formatter = transformationMatrix.getEntry(j,i);
                    String formatted = String.format("%.3f", formatter);
                    stream.write(formatted + " ");
                }
                stream.write('\n');
            }
            stream.write("\n# Inverse transformation matrix \n");
            for (int i = 0; i < inverseTransformationMatrix.getColumnDimension(); i++) {
                for(int j = 0; j < inverseTransformationMatrix.getRowDimension() -1; j++){
                    double formatter = inverseTransformationMatrix.getEntry(j,i);
                    String formatted = String.format("%.3f", formatter);
                    stream.write(formatted + " ");
                }
                stream.write('\n');
            }
            stream.write("\n# Sum absolute translations from original to transformed \n");
             double totalSum = 0;
             double vertexSum = 0;
             for (int i = 0; i < startMatrix.getColumnDimension(); i++) {
                for(int j = 0; j < startMatrix.getRowDimension() -1; j++){
                    vertexSum += Math.abs(startMatrix.getEntry(j,i) - finalMatrix.getEntry(j,i));
                }
                totalSum += vertexSum;
             }
            String formatted = String.format("%.10f", vertexSum);
            stream.write(formatted + " ");
            stream.write('\n');

            stream.write("\n# Sum absolute translations from original to transformed to \"original\" \n");
             RealMatrix testTransform = transformationMatrix.multiply(startMatrix);

            //System.out.println("after transform 2"+testTransform.toString() );
            testTransform = inverseTransformationMatrix.multiply(testTransform);
            vertexSum = 0;
            totalSum = 0;
            //System.out.println("after inverse 2"+testTransform.toString() );
            for (int i = 0; i < startMatrix.getColumnDimension(); i++) {
                for(int j = 0; j < startMatrix.getRowDimension() -1; j++){
                    vertexSum += Math.abs(startMatrix.getEntry(j,i) - testTransform.getEntry(j,i));
                }
                totalSum += vertexSum;
            }
             formatted = String.format("%.10f", vertexSum);
            stream.write(formatted + " ");
            stream.write('\n');
            stream.close();

        }catch(IOException e){

        }

    }



}
