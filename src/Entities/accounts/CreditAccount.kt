package entities.accounts

import entities.Bank
import entities.PhysicalPerson

/**
 * Класс кредитного счета для выплаты кредитов
 * Наследует: BankAccount
 */
class CreditAccount(
    accountNumber: String,
    owner: PhysicalPerson,
    bank: Bank,
    balance: Double = 0.0,
    val creditLimit: Double,
    val creditRate: Double,
    val creditTerm: Int
) : BankAccount(accountNumber, owner, bank, balance) {

    init {
        require(creditLimit > 0) { "Кредитный лимит должен быть положительным" }
        require(creditRate > 0) { "Процентная ставка должна быть положительной" }
    }

    override fun canClose(): Boolean {
        return balance == 0.0
    }

    override fun withdraw(amount: Double): Boolean {
        // Для кредитного счета баланс может быть отрицательным, но в пределах лимита
        if (amount > 0 && balance - amount >= -creditLimit) {
            balance -= amount
            return true
        }
        return false
    }

    /**
     * Рассчитывает доступный кредитный лимит
     * @return Доступная сумма для снятия
     */
    fun getAvailableCredit(): Double {
        return creditLimit + balance  // balance отрицательный при использовании кредита
    }

    override fun getAccountInfo(): String {
        return "Кредитный счет $accountNumber, баланс: $balance, " +
                "доступно: ${getAvailableCredit()}, лимит: $creditLimit"
    }
}