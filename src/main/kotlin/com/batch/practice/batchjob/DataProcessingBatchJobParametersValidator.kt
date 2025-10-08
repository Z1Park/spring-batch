package com.batch.practice.batchjob

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersValidator
import org.springframework.stereotype.Component

@Component
class DataProcessingBatchJobParametersValidator: JobParametersValidator {

	override fun validate(parameters: JobParameters?) {
		if (parameters?.isEmpty == true) {
			throw IllegalArgumentException("배치 파라미터가 없습니다.")
		}

		val stringParam = parameters!!.getString("stringParam")
		if (stringParam.isNullOrBlank()) {
			throw IllegalArgumentException("stringParam 파라미터가 없습니다.")
		}

//		val intParam = parameters.getLong("intParam") // Int 타입으로 받는게 불가능
		val intParam = parameters.getParameter("intParam")?.let {
			if (it.type != Int::class.java && it.type != Integer::class.java) {
				throw IllegalArgumentException("intParam 파라미터 타입이 Int 타입이 아닙니다.")
			}
			it.value as Int
		}
		if (intParam == null || intParam <= 0) {
			throw IllegalArgumentException("intParam 파라미터가 없거나 0 이하입니다.")
		}
	}
}