package com.batch.practice.api

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/batch")
class BatchController(
	private val jobLauncher: JobLauncher,
	@Qualifier("dataProcessingJob")
	private val dataProcessingJob: Job
) {

	@PostMapping("/data-processing/job-parameters-test")
	fun jobParametersBuilderTest() {
		val jobParameters = JobParametersBuilder()
			.addJobParameter("stringParam", "문자열1,문자열2,문자열3", String::class.java)
			.addJobParameter("intParam", 12345, Int::class.java)
			.toJobParameters()

		jobLauncher.run(dataProcessingJob, jobParameters)
	}
}