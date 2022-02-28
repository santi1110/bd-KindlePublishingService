package com.amazon.ata.kindlepublishingservice.mastery.four;

import com.amazon.ata.kindlepublishingservice.helpers.TctIntrospectionTest;
import com.amazon.ata.kindlepublishingservice.helpers.TctResult;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class MasteryTaskFourTests extends TctIntrospectionTest {
    private static final String TEST_SUITE_ID = "MT4";

    @Override
    protected String getTctSuiteId() {
        return TEST_SUITE_ID;
    }

    @Test(dataProvider = "TctResults")
    public void masteryTaskFour_runIntrospectionSuite_reportResults(TctResult result) {
        assertTrue(result.isPassed(), result.getErrorMessage());
    }
}
