package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Driver {
    private double wx, wy, wz;
    private double angle;
    private double scale;
    private double tx, ty, tz;
    private String model;

    public Driver (){
        wx = 0;
        wy = 0;
        wz = 0;
        angle = 0;
        scale = 0;
        tx = 0;
        ty = 0;
        tz = 0;

    }
    public void setAxisAngle (double wx, double wy, double wz) {
        this.wx = wx;
        this.wy = wy;
        this.wz = wz;
    }
    public void setAngle(double angle){
        this.angle = angle;
    }
    public void setScale( double scale) {
        this.scale = scale;
    }
    public void translate(double tx, double ty, double tz){
        this.tx = tx;
        this.ty = ty;
        this.tz = tz;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getModel() {
        return this.model;
    }

    @Override
    public String toString() {
        return wx + " " + wy + " " + wz + " " + angle +  " " + scale + " " + tx + " " + ty + " " + tz + " " + wz + " " + model;
    }
}
