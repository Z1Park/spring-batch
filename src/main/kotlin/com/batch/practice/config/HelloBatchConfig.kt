package com.batch.practice.config

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
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
			.start(firstStep())
			.next(secondStep())
			.next(lastStep())
			.build()
	}

	@Bean
	fun firstStep(): Step {
		return StepBuilder("firstStep", jobRepository)
			.tasklet({ contribution, chunkContext ->
				println("This is my first step")
				RepeatStatus.FINISHED
			}, transactionManager)
			.build()
	}

	@Bean
	fun secondStep(): Step {
		return StepBuilder("secondStep", jobRepository)
			.tasklet({ contribution, chunkContext ->
				println("This is my second step.")
				RepeatStatus.FINISHED
			}, transactionManager)
			.build()
	}

	@Bean
	fun lastStep(): Step {
		return StepBuilder("lastStep", jobRepository)
			.tasklet({ contribution, chunkContext ->
				println("This is my last step")
				RepeatStatus.FINISHED
			}, transactionManager)
			.build()
	}
}