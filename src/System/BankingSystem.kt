package system

import entities.Bank
import entities.PhysicalPerson
import entities.accounts.BankAccount
import entities.accounts.CurrentAccount
import entities.accounts.DepositAccount
import entities.accounts.CreditAccount
import enums.InterestCalculation
import java.util.UUID

/**
 * Основной класс системы, управляющий всеми банками, клиентами и счетами
 * Реализует бизнес-логику приложения
 */
class BankingSystem {
    private val banks = mutableListOf<Bank>()
    private val persons = mutableListOf<PhysicalPerson>()
    private val accounts = mutableListOf<BankAccount>()

    // region Управление банками

    /**
     * Добавляет новый банк в систему
     * @param fullName Полное название банка
     * @param shortName Краткое название банка
     * @param interestRate Процентная ставка
     * @return Созданный банк
     * @throws IllegalArgumentException если банк с таким названием уже существует
     */
    fun addBank(fullName: String, shortName: String, interestRate: Double): Bank {
        val existingBank = banks.find {
            it.fullName.equals(fullName, ignoreCase = true) ||
                    it.shortName.equals(shortName, ignoreCase = true)
        }
        require(existingBank == null) { "Банк с таким названием уже существует" }

        val bank = Bank(fullName, shortName, interestRate)
        banks.add(bank)
        return bank
    }

    /**
     * Находит банк по названию
     * @param name Название банка (полное или краткое)
     * @return Найденный банк или null
     */
    fun findBankByName(name: String): Bank? {
        return banks.find {
            it.fullName.equals(name, ignoreCase = true) ||
                    it.shortName.equals(name, ignoreCase = true)
        }
    }

    /**
     * Возвращает список всех банков
     */
    fun getAllBanks(): List<Bank> = banks.toList()

    // endregion

    // region Управление клиентами

    /**
     * Добавляет нового клиента в систему
     * @param fio ФИО клиента
     * @param inn ИНН клиента
     * @param numPas Номер паспорта
     * @param serPas Серия паспорта
     * @return Созданный клиент
     * @throws IllegalArgumentException если клиент с таким ИНН уже существует
     */
    fun addPerson(fio: String, inn: String, numPas: String, serPas: String): PhysicalPerson {
        val existingPerson = persons.find { it.inn == inn }
        require(existingPerson == null) { "Человек с ИНН $inn уже существует" }

        val person = PhysicalPerson(fio, inn, numPas, serPas)
        persons.add(person)
        return person
    }

    /**
     * Находит клиента по ИНН
     * @param inn ИНН клиента
     * @return Найденный клиент или null
     */
    fun findPersonByINN(inn: String): PhysicalPerson? {
        return persons.find { it.inn == inn }
    }

    /**
     * Возвращает список всех клиентов
     */
    fun getAllPersons(): List<PhysicalPerson> = persons.toList()

    // endregion

    // region Управление счетами

    /**
     * Создает текущий счет для клиента
     * @param person Владелец счета
     * @param bank Банк счета
     * @param initialBalance Начальный баланс
     * @return Созданный текущий счет
     * @throws IllegalArgumentException если превышен лимит счетов (3 счета в одном банке)
     */
    fun createCurrentAccount(person: PhysicalPerson, bank: Bank, initialBalance: Double = 0.0): CurrentAccount {
        requirePersonAndBankRegistered(person, bank)

        val personAccountsInBank = accounts.filter {
            it.owner == person && it.bank == bank && it is CurrentAccount
        }

        require(personAccountsInBank.size < 3) {
            "Нельзя иметь более 3 текущих счетов в одном банке"
        }

        val accountNumber = "CUR${generateShortUUID()}"
        val account = CurrentAccount(accountNumber, person, bank, initialBalance)
        accounts.add(account)
        return account
    }

    /**
     * Создает депозитный счет для клиента
     * @param person Владелец счета
     * @param bank Банк счета
     * @param initialBalance Начальный баланс
     * @param termMonths Срок депозита в месяцах
     * @param minBalance Минимальный остаток
     * @param isWithdrawable Можно ли снимать средства
     * @param annualRate Годовая процентная ставка
     * @param interestCalculation Способ начисления процентов
     * @param isProlongable Автопролонгация
     * @return Созданный депозитный счет
     * @throws IllegalArgumentException если нарушены условия депозита
     */
    fun createDepositAccount(
        person: PhysicalPerson,
        bank: Bank,
        initialBalance: Double,
        termMonths: Int,
        minBalance: Double,
        isWithdrawable: Boolean,
        annualRate: Double,
        interestCalculation: InterestCalculation,
        isProlongable: Boolean
    ): DepositAccount {
        requirePersonAndBankRegistered(person, bank)

        // Проверка: не более 1 депозитного счета в банке
        val existingDeposit = accounts.find {
            it.owner == person && it.bank == bank && it is DepositAccount
        }
        require(existingDeposit == null) {
            "Нельзя иметь более 1 депозитного счета в одном банке"
        }

        require(initialBalance >= minBalance) {
            "Начальный баланс должен быть не меньше минимального: $minBalance"
        }

        val accountNumber = "DEP${generateShortUUID()}"
        val account = DepositAccount(
            accountNumber, person, bank, initialBalance,
            termMonths, minBalance, true, isWithdrawable, // isReplenishable всегда true по ТЗ
            annualRate, interestCalculation, isProlongable
        )
        accounts.add(account)
        return account
    }

    /**
     * Создает кредитный счет для клиента
     * @param person Владелец счета
     * @param bank Банк счета
     * @param creditLimit Кредитный лимит
     * @param creditRate Процентная ставка по кредиту
     * @param creditTerm Срок кредита в месяцах
     * @param initialBalance Начальный баланс
     * @return Созданный кредитный счет
     */
    fun createCreditAccount(
        person: PhysicalPerson,
        bank: Bank,
        creditLimit: Double,
        creditRate: Double,
        creditTerm: Int,
        initialBalance: Double = 0.0
    ): CreditAccount {
        requirePersonAndBankRegistered(person, bank)
        require(creditLimit > 0) { "Кредитный лимит должен быть положительным" }

        val accountNumber = "CRD${generateShortUUID()}"
        val account = CreditAccount(
            accountNumber, person, bank, initialBalance,
            creditLimit, creditRate, creditTerm
        )
        accounts.add(account)
        return account
    }

    // endregion

    // region Поиск и операции

    /**
     * Находит счет по номеру
     * @param accountNumber Номер счета
     * @return Найденный счет или null
     */
    fun findAccountByNumber(accountNumber: String): BankAccount? {
        return accounts.find { it.accountNumber == accountNumber }
    }

    /**
     * Находит все счета клиента
     * @param person Владелец счетов
     * @return Список счетов клиента
     */
    fun findAccountsByPerson(person: PhysicalPerson): List<BankAccount> {
        return accounts.filter { it.owner == person }
    }

    /**
     * Находит все счета в указанном банке
     * @param bank Банк
     * @return Список счетов в банке
     */
    fun findAccountsByBank(bank: Bank): List<BankAccount> {
        return accounts.filter { it.bank == bank }
    }

    /**
     * Пополняет счет
     * @param accountNumber Номер счета
     * @param amount Сумма пополнения
     * @return true если операция успешна
     */
    fun depositToAccount(accountNumber: String, amount: Double): Boolean {
        val account = findAccountByNumber(accountNumber)
        return if (account != null && account.isActive) {
            account.deposit(amount)
            true
        } else {
            false
        }
    }

    /**
     * Снимает средства со счета
     * @param accountNumber Номер счета
     * @param amount Сумма снятия
     * @return true если операция успешна
     */
    fun withdrawFromAccount(accountNumber: String, amount: Double): Boolean {
        val account = findAccountByNumber(accountNumber)
        return if (account != null && account.isActive) {
            account.withdraw(amount)
        } else {
            false
        }
    }

    // endregion

    // region Вспомогательные методы

    private fun requirePersonAndBankRegistered(person: PhysicalPerson, bank: Bank) {
        require(person in persons) { "Физическое лицо не зарегистрировано в системе" }
        require(bank in banks) { "Банк не зарегистрирован в системе" }
    }

    private fun generateShortUUID(): String {
        return UUID.randomUUID().toString().take(8).uppercase()
    }

    /**
     * Возвращает общее количество счетов
     */
    fun getTotalAccounts(): Int = accounts.size

    /**
     * Возвращает количество активных счетов
     */
    fun getActiveAccounts(): Int = accounts.count { it.isActive }

    /**
     * Возвращает общий баланс всех счетов
     */
    fun getTotalBalance(): Double = accounts.sumOf { it.balance }

    // endregion
}