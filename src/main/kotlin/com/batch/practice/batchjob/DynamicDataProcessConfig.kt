package com.batch.practice.batchjob

import com.batch.practice.batchlistener.DynamicDataListener
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.listener.ExecutionContextPromotionListener
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import kotlin.random.Random

@Configuration
class DynamicDataProcessConfig(
	private val jobRepository: JobRepository,
	private val transactionManager: PlatformTransactionManager,
) {
	private val log = LoggerFactory.getLogger(this::class.java)

	@Bean
	fun dynamicDataProcessJob(
		dynamicDataProcessFirstStep: Step,
		dynamicDataProcessSecondStep: Step,
//		dynamicDataListener: DynamicDataListener
	): Job {
		return JobBuilder("dynamicDataProcessJob", jobRepository)
//			.listener(dynamicDataListener)
			.start(dynamicDataProcessFirstStep)
			.next(dynamicDataProcessSecondStep)
			.next(dynamicDataProcessingThirdStep())
			.build()
	}

	@Bean
	fun dynamicDataProcessFirstStep(): Step {
		return StepBuilder("dynamicDataProcessFirstStep", jobRepository)
			.tasklet({ contribution, chunkContext ->
//				val data = chunkContext.stepContext.jobExecutionContext["dynamicData1"]
//				log.info("This is dynamic data process first step. dynamicData1 = {}", data)
				val randomData = Random.nextInt(1000)
				contribution.stepExecution.executionContext.put("randomData", randomData)
				log.info("This is dynamic data process first step. randomData = {}", randomData)

				return@tasklet RepeatStatus.FINISHED
			}, transactionManager)
			.listener(promotionListener())
			.build()
	}

	@Bean
	@JobScope
	fun dynamicDataProcessSecondStep(
//		@Value("#{jobExecutionContext['dynamicData2']}") dynamicData2: String?,
		@Value("#{jobExecutionContext['randomData']}") randomData: Int?,
	): Step {
		return StepBuilder("dynamicDataProcessSecondStep", jobRepository)
			.tasklet({ contribution, chunkContext ->
//				log.info("This is dynamic data process second step. dynamicData2 = {}", dynamicData2)
				log.info("This is dynamic data process second step. randomData = {}", randomData)

				return@tasklet RepeatStatus.FINISHED
			}, transactionManager)
			.build()
	}

	@Bean
	fun dynamicDataProcessingThirdStep(): Step {
		return StepBuilder("dynamicDataProcessingThirdStep", jobRepository)
			.tasklet({ contribution, chunkContext ->
				val data = chunkContext.stepContext.jobExecutionContext["randomData"]
				log.info("This is dynamic data process third step. randomData = {}", data)

				return@tasklet RepeatStatus.FINISHED
			}, transactionManager)
			.build()
	}

	@Bean
	fun promotionListener(): ExecutionContextPromotionListener {
		return ExecutionContextPromotionListener().apply {
			setKeys(arrayOf("randomData"))
		}
	}
}