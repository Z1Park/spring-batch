package com.batch.practice.batchjob

import com.batch.practice.entity.Item
import jakarta.persistence.EntityManagerFactory
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaCursorItemReader
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaTransactionManager

@Configuration
class JpaItemConfig(
	private val jobRepository: JobRepository,
	private val entityManagerFactory: EntityManagerFactory,
) {
	private val transactionManager = JpaTransactionManager(entityManagerFactory)
	private val log = LoggerFactory.getLogger(this::class.java)

	@Bean
	fun jpaItemJob(
		jpaItemStep: Step,
	): Job {
		return JobBuilder("jpaItemJob", jobRepository)
			.start(jpaItemStep)
			.build()
	}

	@Bean
	fun jpaItemStep(
		jpaCursorItemReader: JpaCursorItemReader<Item>,
//		jpaPagingItemReader: JpaPagingItemReader<Item>,
		jpaItemProcessor: ItemProcessor<Item, Item>,
		jpaItemWriter: ItemWriter<Item>,
	): Step {
		return StepBuilder("jpaItemStep", jobRepository)
			.chunk<Item, Item>(3, transactionManager)
			.reader(jpaCursorItemReader)
//			.reader(jpaPagingItemReader)
			.processor(jpaItemProcessor)
			.writer(jpaItemWriter)
			.allowStartIfComplete(true)
			.build()
	}

	@Bean
	fun jpaCursorItemReader(): JpaCursorItemReader<Item> {
		return JpaCursorItemReaderBuilder<Item>()
			.name("jpaCursorItemReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("""
				SELECT i FROM Item i
				LEFT JOIN FETCH i.orders o
				WHERE i.status = :status
			""".trimIndent())
			.parameterValues(mapOf("status" to "READY"))
			.build()
	}

	@Bean
	fun jpaPagingItemReader(): JpaPagingItemReader<Item> {
		return JpaPagingItemReaderBuilder<Item>()
			.name("jpaPagingItemReader")
			.entityManagerFactory(entityManagerFactory)
			.queryString("""
				SELECT i FROM Item i
				WHERE i.status = :status
				ORDER BY i.id ASC
			""".trimIndent())
			.parameterValues(mapOf("status" to "READY"))
			.pageSize(3)
			.transacted(false)
			.build()
	}

	@Bean
	fun jpaItemProcessor(): ItemProcessor<Item, Item> {
		log.info(">> jpaItemProcessor")
		return ItemProcessor { item ->
			log.info("before process >> item = $item")
			item.getOrders().forEachIndexed { index, order ->
				log.info("   >> order[$index] = $order")
			}
			item.process()
			item
		}
	}

//	@Bean
//	fun jpaItemWriter(): ItemWriter<Item> {
//		return ItemWriter { items ->
//			items.forEach { item ->
//				log.info("after process >> item = $item")
//				item.getOrders().forEachIndexed { index, order ->
//					log.info("   >> order[$index] = $order")
//				}
//			}
//		}
//	}

	@Bean
	fun jpaItemWriter(): ItemWriter<Item> {
		log.info(">> jpaItemWriter")
		return JpaItemWriterBuilder<Item>()
			.entityManagerFactory(entityManagerFactory)
			.usePersist(false) // merge 사용
			.build()
	}
}