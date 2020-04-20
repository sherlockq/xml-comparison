package com.ecw.xmlcomparison;

import java.io.File;

public class DiffApp {
    public static void main(String[] args) {
        CcdaDiff diff = CcdaDiff.of(new File("samples/ccd-1-control.xml"),
                new File("samples/ccd-1-test.xml"));
        if(diff.hasDifferences()){
            diff.printDifferences();
        } else{
            System.out.println("No semantic difference found.");
        }
    }
}
