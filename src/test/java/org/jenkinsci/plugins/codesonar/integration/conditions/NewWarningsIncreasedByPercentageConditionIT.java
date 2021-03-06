package org.jenkinsci.plugins.codesonar.integration.conditions;

import hudson.model.Cause;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.model.queue.QueueTaskFuture;
import java.util.ArrayList;
import java.util.List;
import org.jenkinsci.plugins.codesonar.CodeSonarPublisher;
import org.jenkinsci.plugins.codesonar.conditions.Condition;
import org.jenkinsci.plugins.codesonar.conditions.NewWarningsIncreasedByPercentageCondition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NewWarningsIncreasedByPercentageConditionIT extends ConditionIntegrationTestBase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void percentageOfBrandNewWarningsIsAbovetheThreshold_BuildIsSetToWarrantedResult() throws Exception {
        // arrange
        final String WARRANTED_RESULT = Result.FAILURE.toString();
        final Result EXPECTED_RESULT = Result.fromString(WARRANTED_RESULT);

        final float PERCENTAGE = 10.0f;

        NewWarningsIncreasedByPercentageCondition condition
                = new NewWarningsIncreasedByPercentageCondition(PERCENTAGE);
        condition.setWarrantedResult(WARRANTED_RESULT);

        List<Condition> conditions = new ArrayList<Condition>();
        conditions.add(condition);

        CodeSonarPublisher codeSonarPublisher = new CodeSonarPublisher(conditions, VALID_HUB_ADDRESS, VALID_PROJECT_NAME);
        codeSonarPublisher.setAnalysisService(mockedAnalysisService);
        codeSonarPublisher.setMetricsService(mockedMetricsService);
        codeSonarPublisher.setProceduresService(mockedProceduresService);

        FreeStyleProject project = jenkinsRule.createFreeStyleProject();
        project.getPublishersList().add(codeSonarPublisher);

        // act
        QueueTaskFuture<FreeStyleBuild> queueTaskFuture = project.scheduleBuild2(0, new Cause.UserIdCause());
        FreeStyleBuild build = queueTaskFuture.get();

        // assert
        Assert.assertEquals(EXPECTED_RESULT, build.getResult());
    }

    @Test
    public void percentageOfBrandNewWarningsIsBellowtheThreshold_BuildIsSuccessful() throws Exception {
        // arrange
        final String WARRANTED_RESULT = Result.FAILURE.toString();
        final Result EXPECTED_RESULT = Result.SUCCESS;

        final float PERCENTAGE = 70.0f;

        NewWarningsIncreasedByPercentageCondition condition
                = new NewWarningsIncreasedByPercentageCondition(PERCENTAGE);
        condition.setWarrantedResult(WARRANTED_RESULT);

        List<Condition> conditions = new ArrayList<Condition>();
        conditions.add(condition);

        CodeSonarPublisher codeSonarPublisher = new CodeSonarPublisher(conditions, VALID_HUB_ADDRESS, VALID_PROJECT_NAME);
        codeSonarPublisher.setAnalysisService(mockedAnalysisService);
        codeSonarPublisher.setMetricsService(mockedMetricsService);
        codeSonarPublisher.setProceduresService(mockedProceduresService);

        FreeStyleProject project = jenkinsRule.createFreeStyleProject();
        project.getPublishersList().add(codeSonarPublisher);

        // act
        QueueTaskFuture<FreeStyleBuild> queueTaskFuture = project.scheduleBuild2(0, new Cause.UserIdCause());
        FreeStyleBuild build = queueTaskFuture.get();

        // assert
        Assert.assertEquals(EXPECTED_RESULT, build.getResult());
    }
}
