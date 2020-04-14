package com.ecw.xmlcomparison;

import org.junit.jupiter.api.Test;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.ElementSelectors;

import static org.assertj.core.api.Assertions.assertThat;

class XmlComparatorFeatures {
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

    @Test
    void documents_considered_identical_with_different_attribute_order() {
        //language=XML
        String original = "<typeId extension=\"POCD_HD000040\" root=\"2.16.840.1.113883.1.3\"/>";
        //language=XML
        String swapped = "<typeId root=\"2.16.840.1.113883.1.3\" extension=\"POCD_HD000040\"/>";

        XmlAssert.assertThat(original).and(swapped).areIdentical();
    }

    @Test
    void documents_considered_identical_with_different_order_of_same_tag_elements() {
        //language=XML
        String original = "<root>\n" +
                "<templateId root=\"2.16.840.1.113883.10.20.22.1.2\" extension=\"2015-08-01\"/>\n" +
                "<templateId root=\"2.16.840.1.113883.10.20.22.1.2\"/>\n" +
                "</root>";

        //language=XML
        String swapped = "<root>\n" +
                "<templateId root=\"2.16.840.1.113883.10.20.22.1.2\"/>\n" +
                "<templateId root=\"2.16.840.1.113883.10.20.22.1.2\" extension=\"2015-08-01\"/>\n" +
                "</root>";

        XmlAssert.assertThat(original).and(swapped)
                .ignoreChildNodesOrder()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndAllAttributes))
                .areIdentical();
    }

    @Test
    void documents_considered_identical_with_different_order_of_individual_tag_elements() {
        //language=XML
        String original = "<addr use=\"HP\">\n" +
                "<streetAddressLine>2222 Home Street</streetAddressLine>\n" +
                "<city>Beaverton</city>\n" +
                "<state>OR</state>\n" +
                "<postalCode>97867</postalCode>\n" +
                "<country>US</country>\n" +
                "</addr>";

        //language=XML
        String swapped = "<addr use=\"HP\">\n" +
                "<streetAddressLine>2222 Home Street</streetAddressLine>\n" +
                "<country>US</country>\n" +
                "<city>Beaverton</city>\n" +
                "<state>OR</state>\n" +
                "<postalCode>97867</postalCode>\n" +
                "</addr>";

        XmlAssert.assertThat(original).and(swapped)
                .ignoreChildNodesOrder()
                .areIdentical();
    }

    @Test
    void documents_considered_identical_with_different_order_of_sections() {
        //language=XML
        String original =
                "<structuredBody>\n" +
                "<component>\n" +
                "        <section>\n" +
                "            <!-- *** Allergies and Intolerances Section (entries required) (V3) *** -->\n" +
                "            <templateId root=\"2.16.840.1.113883.10.20.22.2.6.1\" extension=\"2015-08-01\"/>\n" +
                "            <templateId root=\"2.16.840.1.113883.10.20.22.2.6.1\"/>\n" +
                "        </section>\n" +
                "    " +
                "</component>\n" +
                "    " +
                "<component>\n" +
                "        <section>\n" +
                "            <!-- *** Encounters section (entries required) (V3) *** -->\n" +
                "            <templateId root=\"2.16.840.1.113883.10.20.22.2.22.1\" extension=\"2015-08-01\"/>\n" +
                "            <templateId root=\"2.16.840.1.113883.10.20.22.2.22.1\"/>\n" +
                "        </section>\n" +
                "    " +
                "</component>\n" +
                "</structuredBody>";

        //language=XML
        String swapped =
                "<structuredBody>\n" +
                        "<component>\n" +
                        "        <section>\n" +
                        "            <!-- *** Encounters section (entries required) (V3) *** -->\n" +
                        "            <templateId root=\"2.16.840.1.113883.10.20.22.2.22.1\" extension=\"2015-08-01\"/>\n" +
                        "            <templateId root=\"2.16.840.1.113883.10.20.22.2.22.1\"/>\n" +
                        "        </section>\n" +
                        "    " +
                        "</component>\n" +
                        "    <component>\n" +
                        "        <section>\n" +
                        "            <!-- *** Allergies and Intolerances Section (entries required) (V3) *** -->\n" +
                        "            <templateId root=\"2.16.840.1.113883.10.20.22.2.6.1\" extension=\"2015-08-01\"/>\n" +
                        "            <templateId root=\"2.16.840.1.113883.10.20.22.2.6.1\"/>\n" +
                        "        </section>\n" +
                        "    </component>\n" +
                        "</structuredBody>";

        XmlAssert.assertThat(original).and(swapped)
                .ignoreChildNodesOrder()
                .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byXPath(".//templateId", ElementSelectors.byNameAndAllAttributes)))
                .areIdentical();
    }
}
