package com.batch.practice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "orders")
class Order(
	@Column(nullable = false)
	private var quantity: Int,
	@Column(nullable = false)
	private var status: String,

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private val id: Long = 0L,

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id")
	private val item: Item
) {

	fun process() {
		status = "DELIVERING"
	}

	override fun toString(): String {
		return "Order(id=$id, quantity=$quantity, status='$status')"
	}
}