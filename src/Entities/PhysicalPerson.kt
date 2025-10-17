package entities

import java.util.UUID

/**
 * Класс представляет физическое лицо - клиента банка
 * @property fio ФИО клиента (минимум 5 символов)
 * @property inn ИНН (12 цифр)
 * @property numPas Номер паспорта (6 цифр)
 * @property serPas Серия паспорта (4 цифры)
 */
class PhysicalPerson(
    val fio: String,
    val inn: String,
    val numPas: String,
    val serPas: String
) {
    val id: UUID = UUID.randomUUID()

    init {
        require(fio.trim().length >= 5) { "ФИО должно содержать хотя бы 5 символов" }
        require(inn.all { it.isDigit() } && inn.length == 12) { "ИНН должен содержать 12 цифр" }
        require(numPas.all { it.isDigit() } && numPas.length == 6) { "Номер паспорта должен содержать 6 цифр" }
        require(serPas.all { it.isDigit() } && serPas.length == 4) { "Серия паспорта должна содержать 4 цифры" }
    }

    override fun toString(): String {
        return "$fio (ИНН: $inn)"
    }
}