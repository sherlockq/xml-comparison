package com.ecw.xmlcomparison;

import org.junit.jupiter.api.Test;
import org.xmlunit.assertj.XmlAssert;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.*;

import static org.assertj.core.api.Assertions.assertThat;

class CcdaDiffShould {

    @Test
    void compareTwoFiles() {

        Diff diff = CcdaDiff.of(Input.fromURL(CcdaDiffShould.class.getResource("/sample-1.xml"))
                , Input.fromURL(CcdaDiffShould.class.getResource("/sample-2.xml"))).getDiff();

        for (Difference difference : diff.getDifferences()) {
            System.out.println(difference.toString());
        }

        assertThat(diff.hasDifferences()).as(diff.toString()).isTrue();
    }

    @Test
    void documents_considered_identical_with_different_attribute_order() {
        //language=XML
        String original = "<typeId extension=\"POCD_HD000040\" root=\"2.16.840.1.113883.1.3\"/>";
        //language=XML
        String swapped = "<typeId root=\"2.16.840.1.113883.1.3\" extension=\"POCD_HD000040\"/>";

        Diff diff = CcdaDiff.of(Input.fromString(original), Input.fromString(swapped)).getDiff();
        assertThat(diff.hasDifferences()).as(diff.toString()).isFalse();
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

        Diff diff = CcdaDiff.of(Input.fromString(original), Input.fromString(swapped)).getDiff();
        assertThat(diff.hasDifferences()).as(diff.toString()).isFalse();
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

        Diff diff = CcdaDiff.of(Input.fromString(original), Input.fromString(swapped)).getDiff();
        assertThat(diff.hasDifferences()).as(diff.toString()).isFalse();
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

        Diff diff = CcdaDiff.of(Input.fromString(original), Input.fromString(swapped)).getDiff();
        assertThat(diff.hasDifferences()).as(diff.toString()).isFalse();
    }

    @Test
    void documents_considered_identical_with_different_order_of_nested_id_tags() {
        //language=XML
        String original = "<parent>\n" +
                "    <section>\n" +
                "        <templateId root=\"child1\"/>\n" +
                "    </section>\n" +
                "    <templateId root=\"parent1\"/>\n" +
                "</parent>";
        //language=XML
        String swapped = "<parent>\n" +
                "    <templateId root=\"parent1\"/>\n" +
                "    <section>\n" +
                "        <templateId root=\"child1\"/>\n" +
                "    </section>\n" +
                "</parent>";

        Diff diff = CcdaDiff.of(Input.fromString(original), Input.fromString(swapped)).getDiff();
        assertThat(diff.hasDifferences()).as(diff.toString()).isFalse();
    }

    @Test
    void documents_considered_identical_with_different_order_of_sections_in_full_context() {
        //language=XML
        String original =
                "<ClinicalDocument>\n" +
                        "    <component>\n" +
                        "        " +
                        "<structuredBody>\n" +
                        "            " +
                        "<component>\n" +
                        "                <section>\n" +
                        "                    <!-- *** Allergies and Intolerances Section (entries required) (V3) *** -->\n" +
                        "                    <templateId root=\"2.16.840.1.113883.10.20.22.2.6.1\" extension=\"2015-08-01\"/>\n" +
                        "                    <templateId root=\"2.16.840.1.113883.10.20.22.2.6.1\"/>\n" +
                        "                    <placeholder/>\n" +
                        "                </section>\n" +
                        "            " +
                        "</component>\n" +
                        "            " +
                        "<component>\n" +
                        "                <section>\n" +
                        "                    <!-- *** Encounters section (entries required) (V3) *** -->\n" +
                        "                    <templateId root=\"2.16.840.1.113883.10.20.22.2.22.1\" extension=\"2015-08-01\"/>\n" +
                        "                    <templateId root=\"2.16.840.1.113883.10.20.22.2.22.1\"/>\n" +
                        "                </section>\n" +
                        "            " +
                        "</component>\n" +
                        "        " +
                        "</structuredBody>\n" +
                        "    </component>\n" +
                        "</ClinicalDocument>\n";

        //language=XML
        String swapped =
                "<ClinicalDocument>\n" +
                        "    <component>\n" +
                        "        " +
                        "<structuredBody>\n" +
                        "            " +
                        "<component>\n" +
                        "                <section>\n" +
                        "                    <!-- *** Encounters section (entries required) (V3) *** -->\n" +
                        "                    <templateId root=\"2.16.840.1.113883.10.20.22.2.22.1\" extension=\"2015-08-01\"/>\n" +
                        "                    <templateId root=\"2.16.840.1.113883.10.20.22.2.22.1\"/>\n" +
                        "                </section>\n" +
                        "            " +
                        "</component>\n" +
                        "            <component>\n" +
                        "                <section>\n" +
                        "                    <!-- *** Allergies and Intolerances Section (entries required) (V3) *** -->\n" +
                        "                    <templateId root=\"2.16.840.1.113883.10.20.22.2.6.1\" extension=\"2015-08-01\"/>\n" +
                        "                    <templateId root=\"2.16.840.1.113883.10.20.22.2.6.1\"/>\n" +
                        "                    <placeholder/>\n" +
                        "                </section>\n" +
                        "            </component>\n" +
                        "        " +
                        "</structuredBody>\n" +
                        "    </component>\n" +
                        "</ClinicalDocument>";

        Diff diff = CcdaDiff.of(Input.fromString(original), Input.fromString(swapped)).getDiff();
        assertThat(diff.hasDifferences()).as(diff.toString()).isFalse();
    }

}
