package com.ecw.xmlcomparison;

import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.*;

import java.io.File;

public class CcdaDiff {
    private final Diff diff;

    public Diff getDiff() {
        return diff;
    }

    private CcdaDiff(Input.Builder control, Input.Builder test) {
        this.diff = generateDiff(control, test);
    }

    public static CcdaDiff of(File control, File test) {
        return new CcdaDiff(Input.fromFile(control), Input.fromFile(test));
    }

    Diff generateDiff(Input.Builder control, Input.Builder test) {
        ElementSelector componentTemplateIdSelector = ElementSelectors.byXPath("./section/templateId[1]", ElementSelectors.byNameAndAllAttributes);
        ElementSelector elementSelector = ElementSelectors.conditionalBuilder()
                .whenElementIsNamed("component").thenUse(componentTemplateIdSelector)
                .whenElementIsNamed("templateId").thenUse(ElementSelectors.byNameAndAllAttributes)
                .elseUse(ElementSelectors.byName)
                .build();


        return DiffBuilder.compare(control).withTest(test)
                .ignoreComments()
                .ignoreWhitespace()
                .withNodeMatcher(new DefaultNodeMatcher(elementSelector))
                // difference in child order is considered a similarity
                .checkForSimilar()
                .build();
    }

    public static CcdaDiff of(Input.Builder control, Input.Builder test) {
        return new CcdaDiff(control, test);
    }

    public void printDifferences() {
        for (Difference difference : diff.getDifferences()) {
            System.out.println(difference.toString());
        }
    }
}