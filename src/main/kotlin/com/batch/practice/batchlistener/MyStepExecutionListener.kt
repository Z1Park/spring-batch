package com.batch.practice.batchlistener

import org.slf4j.LoggerFactory
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.batch.core.annotation.AfterStep
import org.springframework.batch.core.annotation.BeforeStep
import org.springframework.stereotype.Component

@Component
//class MyStepExecutionListener: StepExecutionListener {
class MyStepExecutionListener {
	private val log = LoggerFactory.getLogger(this::class.java)

//	override fun beforeStep(stepExecution: StepExecution) {
//		log.info("StepExecutionListener - beforeStep")
//	}
//
//	override fun afterStep(stepExecution: StepExecution): ExitStatus? {
//		log.info("StepExecutionListener - afterStep")
//		return ExitStatus.COMPLETED
//	}

	@BeforeStep
	fun beforeStep(stepExecution: StepExecution) {
		log.info("StepExecutionListener - beforeStep")
	}

	@AfterStep
	fun afterStep(stepExecution: StepExecution): ExitStatus? {
		log.info("StepExecutionListener - afterStep")
		return ExitStatus.COMPLETED
	}
}