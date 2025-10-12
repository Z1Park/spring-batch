package com.batch.practice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "items")
class Item(
	@Column(nullable = false)
	private var name: String,
	@Column(nullable = false)
	private var status: String,
	@Column(name = "created_at", nullable = false, updatable = false)
	private val createdAt: LocalDateTime = LocalDateTime.now(),

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private val id: Long = 0L,

	// 도메인이 맞지도 않고 양방향 매핑도 하지 않지만, 배치 연습을 위해 JPA 방식으로 조회
	@OneToMany(mappedBy = "item")
	private val orders: MutableList<Order> = mutableListOf()
) {

	fun process() {
		status = "PROCESSING"
		orders.forEach { it.process() }
	}

	fun getOrders(): List<Order> = orders.toList()

	override fun toString(): String {
		return "Item(id=$id, name='$name', status='$status', createdAt=$createdAt)"
	}
}