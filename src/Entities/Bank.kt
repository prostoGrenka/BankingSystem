package entities

import java.util.UUID

/**
 * Класс представляет банк в системе
 * @property fullName Полное название банка
 * @property shortName Краткое название банка
 * @property interestRate Процент на остаток по текущим счетам (0.1-2.0%)
 */
class Bank(
    val fullName: String,
    val shortName: String,
    val interestRate: Double
) {
    val id: UUID = UUID.randomUUID()

    init {
        require(fullName.trim().isNotBlank()) { "Полное название не может быть пустым" }
        require(shortName.trim().isNotBlank()) { "Краткое название не может быть пустым" }
        require(interestRate in 0.1..2.0) { "Процент должен быть от 0.1 до 2.0" }
    }

    override fun toString(): String {
        return "$shortName ($fullName) - $interestRate%"
    }
}