package com.batch.practice.batchjob

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class ScopeBatchConfig(
	private val jobRepository: JobRepository,
	private val transactionManager: PlatformTransactionManager,
) {
	private val log = LoggerFactory.getLogger(this::class.java)

	@Bean
	fun scopeJob(): Job {
		return JobBuilder("scopeJob", jobRepository)
			.start(scopeStep("", ""))
			.build()
	}

	@Bean
	@JobScope
	fun scopeStep(
		@Value("#{jobParameters['stepParam']}") stepParam: String,
		@Value("#{jobExecutionContext['previousSystemState']}") previousSystemState: String,
	): Step {
		return StepBuilder("scopeStep", jobRepository)
			.tasklet({ contribution, chunkContext ->
				log.info("This is scope step. stepParam = {}", stepParam)
				org.springframework.batch.repeat.RepeatStatus.FINISHED
			}, transactionManager)
			.build()
	}

	@Bean
	@StepScope
	fun scopeTasklet(
		@Value("#{jobParameters['taskletParam']}") taskletParam: String,
		@Value("#{stepExecutionContext['currentSystemState']}") currentSystemState: String,
	) = Tasklet { contribution, chunkContext ->
		log.info("This is scope tasklet. taskletParam = {}", taskletParam)
		RepeatStatus.FINISHED
	}
}