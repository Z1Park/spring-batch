package com.batch.practice.batchjob

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.DefaultJobParametersValidator
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDate
import java.time.LocalDateTime

@Configuration
class DataProcessingBatchConfig(
	private val jobRepository: JobRepository,
	private val transactionManager: PlatformTransactionManager,
	private val dataProcessingBatchJobParametersValidator: DataProcessingBatchJobParametersValidator,
) {
	private val log = LoggerFactory.getLogger(this::class.java)

	@Bean
	fun dataProcessingJob(dataProcessingStep: Step): Job {
		return JobBuilder("dataProcessingJob", jobRepository)
//			.validator(dataProcessingBatchJobParametersValidator)
			.validator(DefaultJobParametersValidator(
				arrayOf("stringParam"),	// 필수 파라미터
				arrayOf("intParam"),		// 선택 파라미터
			))
			.start(dataProcessingStep)
			.build()
	}

	@Bean
	fun dataProcessingStep(
//		dataProcessingTasklet: Tasklet,
//		timeProcessingTasklet: Tasklet,
//		enumProcessingTasklet: Tasklet,
//		pojoProcessingTasklet: Tasklet,
		jobExecutionParameterTasklet: Tasklet,
	): Step {
		return StepBuilder("dataProcessingStep", jobRepository)
//			.tasklet(dataProcessingTasklet, transactionManager)
//			.tasklet(timeProcessingTasklet, transactionManager)
//			.tasklet(enumProcessingTasklet, transactionManager)
//			.tasklet(pojoProcessingTasklet, transactionManager)
			.tasklet(jobExecutionParameterTasklet, transactionManager)
			.build()
	}

	@Bean
	@StepScope
	fun dataProcessingTasklet(
		@Value("#{jobParameters['dataId']}") dataId: String,
		@Value("#{jobParameters['targetCount']}") targetCount: Int,
	): Tasklet {
		return Tasklet { contribution, chunkContext ->
			log.info("data process start : dataId=$dataId")
			for (i in 1..targetCount) {
				log.info("data processing... $i / $targetCount")
				// data processing
			}

			RepeatStatus.FINISHED
		}
	}

	@Bean
	@StepScope
	fun timeProcessingTasklet(
		@Value("#{jobParameters['processTime']}") processTime: LocalDateTime,
		@Value("#{jobParameters['expectedCompletionDate']}") expectedCompletionDate: LocalDate,
	): Tasklet {
		return Tasklet { contribution, chunkContext ->
			log.info("time process start : processTime=$processTime, expectedCompletionDate=$expectedCompletionDate")
			// time processing logic here

			RepeatStatus.FINISHED
		}
	}

	@Bean
	@StepScope
	fun enumProcessingTasklet(
		@Value("#{jobParameters['processOption']}") processOption: MyEnum,
	): Tasklet {
		return Tasklet { contribution, chunkContext ->
			log.info("enum process start : processOption=$processOption")
			when (processOption) {
				MyEnum.OPTION1 -> log.info("Processing with OPTION1")
				MyEnum.OPTION2 -> log.info("Processing with OPTION2")
				MyEnum.OPTION3 -> log.info("Processing with OPTION3")
			}
			RepeatStatus.FINISHED
		}
	}

	@Bean
	fun pojoProcessingTasklet(pojoParameters: PojoParameters): Tasklet {
		return Tasklet { contribution, chunkContext ->
			val split = pojoParameters.stringParam.split(",")
			log.info("pojo process start : stringParam.size=${split.size} intParam=${pojoParameters.intParam}")
			split.forEach {  log.info(" - stringItem: $it") }
			// pojo processing logic here

			RepeatStatus.FINISHED
		}
	}

	@Bean
	fun jobExecutionParameterTasklet(): Tasklet {
		return Tasklet { contribution, chunkContext ->
			val jobParameters = chunkContext.stepContext.jobParameters
			log.info("Job Parameters:")
			(jobParameters["stringParam"] as String?)
				?.split(",")
				?.forEach { log.info(" - stringParam item: $it") }
			(jobParameters["intParam"] as Int?)?.let { log.info(" - intParam: $it") }
			RepeatStatus.FINISHED
		}
	}
}

enum class MyEnum {
	OPTION1,
	OPTION2,
	OPTION3
}

@Component
@StepScope
class PojoParameters(
	@Value("#{jobParameters['stringParam']}") val stringParam: String,
	@Value("#{jobParameters['intParam']}") val intParam: Int,
)