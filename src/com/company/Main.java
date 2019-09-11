package com.company;


import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Driver[] driverArray = new Driver[args.length];
        System.out.println(args[0]);
        for(int i = 0 ; i < args.length; i++) {
            driverArray[i] = new Driver();
           File file = new File(args[i]);
            System.out.println(file);

            try{
                Scanner scanner = new Scanner(file);
                Driver driver = new Driver();
                String type = scanner.next();
                double wx = Double.parseDouble(scanner.next());
                double wy = Double.parseDouble(scanner.next());
                double wz = Double.parseDouble(scanner.next());
                driver.setAxisAngle(wx,wy,wz);
                double angle = Double.parseDouble(scanner.next());
                driver.setAngle(angle);
                double scale = Double.parseDouble(scanner.next());
                driver.setScale(scale);
                double tx = Double.parseDouble(scanner.next());
                double ty = Double.parseDouble(scanner.next());
                double tz = Double.parseDouble(scanner.next());
                driver.translate(tx,ty,tz);
                String modelName = scanner.next();
                Path modelPath = Paths.get(args[i]);
                System.out.println(modelPath);
                driver.setModel(modelName);
                driverArray[i] = driver;
                System.out.println(driver.toString());

                Model model = new Model(driver);
                scanner.close();



            }catch( IOException e){
                System.exit(1);
            }


        }
    }
}
