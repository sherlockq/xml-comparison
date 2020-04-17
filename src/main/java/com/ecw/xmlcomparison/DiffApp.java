package com.ecw.xmlcomparison;

import java.io.File;

public class DiffApp {
    public static void main(String[] args) {
        CcdaDiff diff = CcdaDiff.of(new File("samples/allergies-control.xml"),
                new File("samples/allergies-generated.xml"));
        diff.printDifferences();
    }
}
