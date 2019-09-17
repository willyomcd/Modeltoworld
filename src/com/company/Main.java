package com.company;


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        ArrayList<Driver> driverArray = new ArrayList<>();
        ArrayList<Model> modelArray = new ArrayList<>();
        System.out.println(args[0]);
        File file = new File(args[0]);


        try {
            Scanner scannerLine = new Scanner(file);
            while (scannerLine.hasNextLine()) {
                String line = scannerLine.nextLine();
                Scanner scanner = new Scanner(line);
                Driver driver = new Driver();
                String type = scanner.next();
                double wx = Double.parseDouble(scanner.next());
                double wy = Double.parseDouble(scanner.next());
                double wz = Double.parseDouble(scanner.next());
                driver.setAxisAngle(wx, wy, wz);
                double angle = Double.parseDouble(scanner.next());
                driver.setAngle(angle);
                double scale = Double.parseDouble(scanner.next());
                driver.setScale(scale);
                double tx = Double.parseDouble(scanner.next());
                double ty = Double.parseDouble(scanner.next());
                double tz = Double.parseDouble(scanner.next());
                driver.translate(tx, ty, tz);
                String modelName = scanner.next();
                Path modelPath = Paths.get(args[0]);
                System.out.println(modelPath);
                driver.setModel(modelName);

                System.out.println(driver.toString());
                Model model = new Model(driver);
                //assigns model number
                int modelsBefore = 0;
                for(int i = 0; i<driverArray.size(); i++) {
                    if(driver.getModelName().equals(driverArray.get(i).getModelName())){
                        modelsBefore++;
                    }
                }
                driver.setModelNumber(modelsBefore);
                modelArray.add(model);
                driverArray.add(driver);
                scanner.close();


            }
            scannerLine.close();
            System.out.println(driverArray.size());
            System.out.println(modelArray.size());
        } catch (IOException e) {
            System.exit(1);
        }
        File folder = new File (args[0].substring(0,args[0].length()-4));
        for (int i = 0; i < modelArray.size(); i++) {
            modelArray.get(i).outputFile(driverArray.get(i),folder);
        }

    }
}
