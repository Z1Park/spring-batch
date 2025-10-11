package com.batch.practice.batchjob

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JdbcCursorItemReader
import org.springframework.batch.item.database.JdbcPagingItemReader
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class JdbcItemReadConfig(
	private val jobRepository: JobRepository,
	private val transactionManager: PlatformTransactionManager,
	private val dataSource: DataSource,
) {
	private val log = LoggerFactory.getLogger(this::class.java)

	@Bean
	fun jdbcItemReadJob(jdbcItemReadStep: Step): Job {
		return JobBuilder("jdbcItemReadJob", jobRepository)
			.incrementer(RunIdIncrementer())
			.start(jdbcItemReadStep)
			.build()
	}

	@Bean
	fun jdbcItemReadStep(
		jdbcCursorItemReader: JdbcCursorItemReader<MutableItem>,
		jdbcPagingItemReader: JdbcPagingItemReader<MutableItem>,
		jdbcItemWriter1: ItemWriter<MutableItem>,
	): Step {
		return StepBuilder("jdbcItemReadStep", jobRepository)
			.writer(jdbcItemWriter1)
			.chunk<MutableItem, MutableItem>(3, transactionManager)
//			.reader(jdbcCursorItemReader)
			.reader(jdbcPagingItemReader)
			.allowStartIfComplete(true)
			.build()
	}

	@Bean
	fun jdbcCursorItemReader(): JdbcCursorItemReader<MutableItem> {
		log.info(">> jdbcItemReader1")
		return JdbcCursorItemReaderBuilder<MutableItem>()
			.name("jdbcItemReader")
			.dataSource(dataSource)
			.sql("SELECT id, name, status FROM items WHERE status = ?")
			.queryArguments("READY")
			.beanRowMapper(MutableItem::class.java)
//			.beanRowMapper(ImmutableItem::class.java)) // Immutable class 지원
//			// 커스텀 매핑 지원
//			.rowMapper { rs, rowNum ->
//				Item(
//					id = rs.getLong("id"),
//					name = rs.getString("name"),
//					status = rs.getString("status"),
//				)
//			}
			.build()
	}

	@Bean
	fun jdbcPagingItemReader(): JdbcPagingItemReader<MutableItem> {
		log.info(">> jdbcPagingItemReader")
		return JdbcPagingItemReaderBuilder<MutableItem>()
			.name("jdbcPagingItemReader")
			.dataSource(dataSource)
			.pageSize(3)
			.selectClause("SELECT id, name, status")
			.fromClause("FROM items")
			.whereClause("WHERE status = :status")
			.sortKeys(mapOf("id" to Order.ASCENDING))
			.parameterValues(mapOf("status" to "READY"))
			.beanRowMapper(MutableItem::class.java)
			.build()
	}

	@Bean
	fun jdbcItemWriter1(): ItemWriter<MutableItem> {
		log.info(">> jdbcItemWriter1")
		return ItemWriter { items ->
			log.info("items size = {}", items.size())
			items.forEach { item ->
				log.info("item = {}", item)
			}
		}
	}
}

class MutableItem(
	var id: Long? = null,
	var name: String = "",
	var status: String = "",
) {
	override fun toString(): String {
		return "MutableItem(id=$id, name='$name', status='$status')"
	}
}

data class ImmutableItem(
	private val id: Long? = null,
	private val name: String,
	private val status: String
)