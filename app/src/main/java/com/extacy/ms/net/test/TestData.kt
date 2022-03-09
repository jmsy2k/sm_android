package com.extacy.ms.net.test


data class TestPhone(
    val mobile: String,
    val home: String,
    val office: String
)

data class TestContact(
    val id: String,
    val name: String,
    val email: String,
    val address: String,
    val gender: String,
    val phone: TestPhone
)

data class TestData (
    val contacts: List<TestContact>
)

