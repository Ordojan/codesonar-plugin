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
import org.jenkinsci.plugins.codesonar.conditions.PercentageOfWariningsIncreasedInCasesBellowCertainRankCondition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Andrius
 */
public class PercentageOfWariningsIncreasedInCasesBellowCertainRankConditionIT extends ConditionIntegrationTestBase {

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void percentageOfWariningsBellowTheDesignatedRankIsAboveTheThreshold_BuildIsSetToWarrantedResult() throws Exception {
        // arrange
        final Result EXPECTED_RESULT = Result.FAILURE;
        final String WARRANTED_RESULT = Result.FAILURE.toString();

        final int RANK_OF_WARNINGS = 30;
        final float WARNING_PERCENTAGE = 50.0f;

        PercentageOfWariningsIncreasedInCasesBellowCertainRankCondition condition
                = new PercentageOfWariningsIncreasedInCasesBellowCertainRankCondition(RANK_OF_WARNINGS, WARNING_PERCENTAGE);
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
    public void percentageOfWariningsBellowTheDesignatedRankIsBellowTheThreshold_BuildIsSuccessful() throws Exception {
        // arrange
        final Result EXPECTED_RESULT = Result.SUCCESS;
        final String WARRANTED_RESULT = Result.FAILURE.toString();

        final int RANK_OF_WARNINGS = 30;
        final float WARNING_PERCENTAGE = 70.0f;

        PercentageOfWariningsIncreasedInCasesBellowCertainRankCondition condition
                = new PercentageOfWariningsIncreasedInCasesBellowCertainRankCondition(RANK_OF_WARNINGS, WARNING_PERCENTAGE);
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
