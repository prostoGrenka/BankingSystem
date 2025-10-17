package entities.accounts

import entities.Bank
import entities.PhysicalPerson
import enums.InterestCalculation
import java.time.LocalDate

/**
 * Класс депозитного счета для хранения вкладов
 * Наследует: BankAccount
 */
class DepositAccount(
    accountNumber: String,
    owner: PhysicalPerson,
    bank: Bank,
    balance: Double = 0.0,
    val termMonths: Int,
    val minBalance: Double,
    val isReplenishable: Boolean,
    val isWithdrawable: Boolean,
    val annualRate: Double,
    val interestCalculation: InterestCalculation,
    val isProlongable: Boolean
) : BankAccount(accountNumber, owner, bank, balance) {

    val endDate: LocalDate = openDate.plusMonths(termMonths.toLong())

    init {
        require(termMonths >= 3) { "Срок депозита должен быть не менее 3 месяцев" }
        require(annualRate in 18.0..25.0) { "Годовая ставка должна быть от 18% до 25%" }
        require(balance >= minBalance) { "Начальный баланс должен быть не меньше минимального" }
    }

    override fun canClose(): Boolean {
        return LocalDate.now() >= endDate && balance >= minBalance
    }

    override fun withdraw(amount: Double): Boolean {
        if (!isWithdrawable) return false
        if (balance - amount >= minBalance) {
            balance -= amount
            return true
        }
        return false
    }

    override fun getAccountInfo(): String {
        return "Депозитный счет $accountNumber, баланс: $balance, до $endDate, ставка: $annualRate%"
    }

    fun isTermCompleted(): Boolean {
        return LocalDate.now() >= endDate
    }
}