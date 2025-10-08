package com.batch.practice.config

import org.springframework.batch.core.converter.JobParametersConverter
import org.springframework.batch.core.converter.JsonJobParametersConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JsonConfig {

	@Bean
	fun jobParametersConverter(): JobParametersConverter {
		return JsonJobParametersConverter()
	}
}