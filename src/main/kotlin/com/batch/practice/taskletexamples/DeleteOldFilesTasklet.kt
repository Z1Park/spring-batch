package com.batch.practice.taskletexamples

import org.slf4j.LoggerFactory
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import java.io.File

class DeleteOldFilesTasklet(
	private val filePath: String,
	private val daysOld: Int,
): Tasklet {
	private val log = LoggerFactory.getLogger(this::class.java)

	override fun execute(
		contribution: StepContribution,
		chunkContext: ChunkContext
	): RepeatStatus {
		val dir = File(filePath)
		val cutoff = System.currentTimeMillis() - daysOld * 24 * 60 * 60 * 1000L

		dir.listFiles()?.forEach { file ->
			if (file.lastModified() < cutoff) {
				if (file.delete()) {
					log.info("Deleted old file: ${file.name}")
				} else {
					log.warn("Failed to delete file: ${file.name}")
				}
			}
		}
		return RepeatStatus.FINISHED
	}
}

@Bean
fun deleteOldFilesTasklet() = DeleteOldFilesTasklet("/path/to/directory", 30)
