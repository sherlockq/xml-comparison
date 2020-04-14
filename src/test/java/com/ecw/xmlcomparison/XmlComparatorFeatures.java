package com.ecw.xmlcomparison;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.jupiter.api.Test;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlComparatorFeatures {
    @Test
    void compareIdenticalDocuments() {
        final String control = "<a><b attr=\"abc\"></b></a>";
        final String test = "<a><b attr=\"xyc\"></b></a>";

        Diff myDiff = DiffBuilder.compare(Input.fromString(control))
                .withTest(Input.fromString(test))
                .build();

//        myDiff.getDifferences().iterator().next().
        assertThat(myDiff.hasDifferences()).as(myDiff.toString()).isFalse();
    }

    @Test
    void compareTwoFiles() {
        Diff diff = DiffBuilder

                .compare(Input.fromURL(XmlComparatorFeatures.class.getResource("/sample-1.xml")))
                .withTest(Input.fromURL(XmlComparatorFeatures.class.getResource("/sample-2.xml")))

                .checkForSimilar().checkForIdentical() // [1]
                .ignoreComments() // [2]
                .ignoreWhitespace() // [3]
                .normalizeWhitespace() // [4]
//                .withComparisonController(ComparisonController) // [5]
//                .withComparisonFormatter(comparisonFormatter) // [6]
//                .withComparisonListeners(comparisonListeners) // [7]
//                .withDifferenceEvaluator(differenceEvaluator) // [8]
//                .withDifferenceListeners(comparisonListeners) // [9]
//                .withNodeMatcher(nodeMatcher) // [10]
//                .withAttributeFilter(attributeFilter) // [11]
//                .withNodeFilter(nodeFilter) // [12]
//                .withNamespaceContext(map) // [13]
//                .withDocumentBuilerFactory(factory) // [14]
//                .ignoreElementContentWhitespace() // [15]

                .build();
        for (Difference difference : diff.getDifferences()) {
            System.out.println(difference.toString());
        }

        assertThat(diff.hasDifferences()).as(diff.toString()).isFalse();
    }
    @Test
    void compareTwoFilesWithAssertXml() {
        Diff diff = DiffBuilder

                .compare(Input.fromURL(XmlComparatorFeatures.class.getResource("/sample-1.xml")))
                .withTest(Input.fromURL(XmlComparatorFeatures.class.getResource("/sample-2.xml")))

                .checkForSimilar().checkForIdentical() // [1]
                .ignoreComments() // [2]
                .ignoreWhitespace() // [3]
                .normalizeWhitespace() // [4]
//                .withComparisonController(ComparisonController) // [5]
//                .withComparisonFormatter(comparisonFormatter) // [6]
//                .withComparisonListeners(comparisonListeners) // [7]
//                .withDifferenceEvaluator(differenceEvaluator) // [8]
//                .withDifferenceListeners(comparisonListeners) // [9]
//                .withNodeMatcher(nodeMatcher) // [10]
//                .withAttributeFilter(attributeFilter) // [11]
//                .withNodeFilter(nodeFilter) // [12]
//                .withNamespaceContext(map) // [13]
//                .withDocumentBuilerFactory(factory) // [14]
//                .ignoreElementContentWhitespace() // [15]

                .build();

        XmlAssert.assertThat(Input.fromURL(XmlComparatorFeatures.class.getResource("/sample-1.xml")))
                .and(Input.fromURL(XmlComparatorFeatures.class.getResource("/sample-2.xml"))).areIdentical();
    }

    // Not working, didn't show "Click to see differences"
    @Test
    void testWithXmlUnitSamples() {
        final String control = "<a><b attr=\"abc\"></b></a>";
        final String test = "<a><b attr=\"xyz\"></b></a>";

        XmlAssert.assertThat(test).and(control).areIdentical();
        XmlAssert.assertThat(test).and(control).areNotIdentical();
        XmlAssert.assertThat(test).and(control).areSimilar();
        XmlAssert.assertThat(test).and(control).areNotSimilar();
    }
}
