package entities.accounts

import entities.Bank
import entities.PhysicalPerson
import java.time.LocalDate

/**
 * Абстрактный базовый класс для всех типов банковских счетов
 * @property accountNumber Уникальный номер счета
 * @property owner Владелец счета
 * @property bank Банк, в котором открыт счет
 * @property balance Текущий баланс счета
 */
abstract class BankAccount(
    val accountNumber: String,
    val owner: PhysicalPerson,
    val bank: Bank,
    var balance: Double = 0.0
) {
    val openDate: LocalDate = LocalDate.now()
    var closeDate: LocalDate? = null
    val isActive: Boolean get() = closeDate == null

    /**
     * Проверяет, можно ли закрыть счет
     * @return true если счет можно закрыть
     */
    abstract fun canClose(): Boolean

    /**
     * Возвращает информацию о счете
     */
    open fun getAccountInfo(): String {
        return "Счет $accountNumber, баланс: $balance, банк: ${bank.shortName}"
    }

    /**
     * Пополняет счет на указанную сумму
     * @param amount Сумма пополнения
     */
    fun deposit(amount: Double) {
        require(amount > 0) { "Сумма должна быть положительной" }
        balance += amount
    }

    /**
     * Снимает средства со счета
     * @param amount Сумма снятия
     * @return true если операция успешна
     */
    open fun withdraw(amount: Double): Boolean {
        if (amount > 0 && balance >= amount) {
            balance -= amount
            return true
        }
        return false
    }
}