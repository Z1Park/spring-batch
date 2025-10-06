package com.batch.practice.batchlistener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobExecutionListener
import org.springframework.batch.core.annotation.AfterJob
import org.springframework.batch.core.annotation.BeforeJob
import org.springframework.stereotype.Component

@Component
class MyJobExecutionListener {
//class MyJobExecutionListener: JobExecutionListener {
	private val log = LoggerFactory.getLogger(this::class.java)

//	override fun beforeJob(jobExecution: JobExecution) {
//		log.info("JobExecutionListener - beforeJob")
//	}
//
//	override fun afterJob(jobExecution: JobExecution) {
//		log.info("JobExecutionListener - afterJob")
//	}

	@BeforeJob
	fun beforeJob(jobExecution: JobExecution) {
		log.info("JobExecutionListener - beforeJob")
	}

	@AfterJob
	fun afterJob(jobExecution: JobExecution) {
		log.info("JobExecutionListener - afterJob")
	}
}