package com.amazon.ata.kindlepublishingservice.mastery.one.design;

import com.amazon.ata.kindlepublishingservice.helpers.TctIntrospectionTest;
import com.amazon.ata.kindlepublishingservice.helpers.TctResult;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class MasteryTaskOneDesignTests extends TctIntrospectionTest {

    private static final String TEST_SUITE_ID = "MT1-Design";

    @Override
    protected String getTctSuiteId() {
        return TEST_SUITE_ID;
    }

    @Test(dataProvider = "TctResults")
    public void masteryTaskOne_runIntrospectionSuite_reportResults(TctResult result) {
        assertTrue(result.isPassed(), result.getErrorMessage());
    }
}
