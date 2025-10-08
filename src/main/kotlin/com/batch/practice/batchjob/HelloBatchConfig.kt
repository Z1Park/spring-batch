package com.batch.practice.batchjob

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class HelloBatchConfig(
	private val jobRepository: JobRepository,
	private val transactionManager: PlatformTransactionManager,
) {

	@Bean
	fun helloJob(): Job {
		return JobBuilder("helloJob", jobRepository)
			.start(taskletStep())
//			.next(chunkStep())
			.build()
	}

	@Bean
	fun taskletStep(): Step {
		return StepBuilder("taskletStep", jobRepository)
			// HelloTasklet 추가
			.tasklet(helloTasklet(), transactionManager)
			.build()
	}

//	@Bean
//	fun chunkStep(): Step {
//		return StepBuilder("chunkStep", jobRepository)
//			.chunk<String, String>(10, transactionManager)
//			.reader(itemReader())
//			.processor(itemProcessor())
//			.writer(itemWriter())
//			.build()
//	}

	@Bean
	fun helloTasklet() = HelloTasklet()

//	@Bean
//	fun itemReader() = HelloItemReader()
//
//	@Bean
//	fun itemProcessor() = HelloItemProcessor()
//
//	@Bean
//	fun itemWriter() = HelloItemWriter()
}