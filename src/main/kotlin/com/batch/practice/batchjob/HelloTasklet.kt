package com.batch.practice.batchjob

import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class HelloTasklet: Tasklet {
	private val log = LoggerFactory.getLogger(this::class.java)

	private val totalCount = 10
	private var count = 0

	override fun execute(
		contribution: StepContribution,
		chunkContext: ChunkContext
	): RepeatStatus {
		count++
		log.info("HelloTasklet is executing. $count / $totalCount")

		if (count < totalCount) {
			// 더 처리해야할 프로세스가 남아있음
			return RepeatStatus.CONTINUABLE
		}
		log.info("HelloTasklet has finished.")
		return RepeatStatus.FINISHED
	}
}