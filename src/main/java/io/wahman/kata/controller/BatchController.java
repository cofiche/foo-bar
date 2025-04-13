package io.wahman.kata.controller;

import io.wahman.kata.Constants.Constants;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/batch-api")
public class BatchController {

    private static final Logger log = Logger.getLogger(BatchController.class.getName());

    private final JobExplorer jobExplorer;

    private final JobLauncher jobLauncher;

    @Qualifier(Constants.JOB_NAME)
    private final Job fooBarJob;

    public BatchController(JobExplorer jobExplorer, JobLauncher jobLauncher, Job fooBarJob) {
        this.jobExplorer = jobExplorer;
        this.jobLauncher = jobLauncher;
        this.fooBarJob = fooBarJob;
    }

    // to run batch job from with a http call
    @PostMapping("/start")
    public ResponseEntity<String> startBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder(jobExplorer)
                    .getNextJobParameters(fooBarJob)
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(fooBarJob, jobParameters);

            log.info("job started with id:" + jobExecution.getJobId());
            return ResponseEntity.accepted()
                    .body("Job successfully started with Id: " + jobExecution.getJobId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not start job: " + e.getMessage());
        }
    }

    // to track status of a job using job id
    @GetMapping("/status/{executionId}")
    public ResponseEntity<String> getJobStatus(@PathVariable Long executionId) {
        var jobExecution = jobExplorer.getJobExecution(executionId);
        if (jobExecution == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobExecution.getStatus().toString());
    }
}
