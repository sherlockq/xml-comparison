package com.ecw.xmlcomparison;

import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelector;
import org.xmlunit.diff.ElementSelectors;

public class CcdaDiff {
    public CcdaDiff() {
    }

    Diff generateCcdaDiff(Input.Builder control, Input.Builder test) {
        ElementSelector componentTemplateIdSelector = ElementSelectors.byXPath("./section/templateId", ElementSelectors.byNameAndAllAttributes);
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
}