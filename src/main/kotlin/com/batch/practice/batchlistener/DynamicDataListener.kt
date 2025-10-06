package com.batch.practice.batchlistener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.annotation.BeforeJob
import org.springframework.stereotype.Component

@Component
class DynamicDataListener {
	private val log = LoggerFactory.getLogger(this::class.java)

	private val DYNAMIC_DATA_CANDIDATE1 = listOf("A", "B", "C")
	private val DYNAMIC_DATA_CANDIDATE2 = listOf("1", "2", "3")

	@BeforeJob
	fun beforeJob(jobExecution: JobExecution) {
		// 동적 데이터 주입
		val randomData1 = DYNAMIC_DATA_CANDIDATE1.shuffled().first()
		val randomData2 = DYNAMIC_DATA_CANDIDATE2.shuffled().first()
		jobExecution.executionContext.put("dynamicData1", randomData1)
		jobExecution.executionContext.put("dynamicData2", randomData2)

		log.info("DynamicDataListener - beforeStep : dynamic data injected - dynamicData1 = {}, dynamicData2 = {}", randomData1, randomData2)
	}
}