package entities.accounts

import entities.Bank
import entities.PhysicalPerson

/**
 * Класс текущего счета для повседневных операций
 * Наследует: BankAccount
 */
class CurrentAccount(
    accountNumber: String,
    owner: PhysicalPerson,
    bank: Bank,
    balance: Double = 0.0
) : BankAccount(accountNumber, owner, bank, balance) {

    override fun canClose(): Boolean {
        return balance == 0.0
    }

    override fun getAccountInfo(): String {
        return "Текущий счет $accountNumber, баланс: $balance, банк: ${bank.shortName}"
    }
}