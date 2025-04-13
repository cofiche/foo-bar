package io.wahman.kata.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class BatchControllerUnitTest {


    @Mock
    private JobExplorer jobExplorer;

    @Mock
    private JobLauncher jobLauncher;

    @Mock
    private Job fooBarJob;

    @InjectMocks
    private BatchController batchController;

    private JobExecution jobExecution;




    @Test
    void testStartBatchJob_Success() throws Exception {
        // Given
        jobExecution = new JobExecution(1L);

        JobExecution mockJobExecution = Mockito.mock(JobExecution.class);
        when(mockJobExecution.getJobId()).thenReturn(1L);

        when(fooBarJob.getJobParametersIncrementer()).thenReturn(new RunIdIncrementer());

        when(jobLauncher.run(any(Job.class), any(JobParameters.class)))
                .thenReturn(mockJobExecution);

        // When
        ResponseEntity<String> response = batchController.startBatchJob();

        // Then
        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Job successfully started with Id: 1", response.getBody());
        verify(jobLauncher).run(any(Job.class), any(JobParameters.class));
    }

    @Test
    void testStartBatchJob_Exception() throws Exception {
        // When
        ResponseEntity<String> response = batchController.startBatchJob();

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void testGetJobStatus_Found() {
        // Given
        jobExecution.setStatus(BatchStatus.COMPLETED);

        // When
        ResponseEntity<String> response = batchController.getJobStatus(1L);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("COMPLETED", response.getBody());
    }

    @Test
    void testGetJobStatus_NotFound() {
        // When
        ResponseEntity<String> response = batchController.getJobStatus(999L);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

}
