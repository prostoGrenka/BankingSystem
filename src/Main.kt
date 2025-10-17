import java.time.LocalDate
import java.util.UUID

fun main() {
    val system = BankingSystem()
    val app = BankingApp(system)

    app.start()
}

class BankingApp(private val system: BankingSystem) {

    fun start() {
        println("=== БАНКОВСКАЯ СИСТЕМА ===")

        while (true) {
            showMainMenu()
            when (readLine()?.trim()) {
                "1" -> bankMenu()
                "2" -> personMenu()
                "3" -> accountMenu()
                "4" -> operationMenu()
                "0" -> {
                    println("Выход из системы...")
                    return
                }
                else -> println("Неверный выбор!")
            }
        }
    }

    private fun showMainMenu() {
        println("\n=== ГЛАВНОЕ МЕНЮ ===")
        println("1. Управление банками")
        println("2. Управление клиентами")
        println("3. Управление счетами")
        println("4. Операции со счетами")
        println("0. Выход")
        print("Выберите пункт: ")
    }

    private fun bankMenu() {
        while (true) {
            println("\n=== УПРАВЛЕНИЕ БАНКАМИ ===")
            println("1. Создать банк")
            println("2. Список банков")
            println("3. Найти банк")
            println("0. Назад")
            print("Выберите пункт: ")

            when (readLine()?.trim()) {
                "1" -> createBank()
                "2" -> showAllBanks()
                "3" -> findBank()
                "0" -> return
                else -> println("Неверный выбор!")
            }
        }
    }

    private fun createBank() {
        println("\n--- Создание банка ---")

        try {
            print("Полное название: ")
            val fullName = readLine()?.trim() ?: ""

            print("Краткое название: ")
            val shortName = readLine()?.trim() ?: ""

            print("Процентная ставка (0.1-2.0): ")
            val rate = readLine()?.toDoubleOrNull() ?: 0.0

            val bank = system.addBank(fullName, shortName, rate)
            println("✅ Банк '${bank.shortName}' успешно создан!")

        } catch (e: Exception) {
            println("❌ Ошибка: ${e.message}")
        }
    }

    private fun showAllBanks() {
        println("\n--- Список банков ---")
        val banks = system.getAllBanks()
        if (banks.isEmpty()) {
            println("Банки не найдены")
        } else {
            banks.forEachIndexed { index, bank ->
                println("${index + 1}. ${bank.fullName} (${bank.shortName}) - ${bank.interestRate}%")
            }
        }
    }

    private fun findBank() {
        print("Введите название банка: ")
        val name = readLine()?.trim() ?: ""
        val bank = system.findBankByName(name)

        if (bank != null) {
            println("✅ Найден банк: ${bank.fullName} (${bank.shortName})")
        } else {
            println("❌ Банк не найден")
        }
    }

    private fun personMenu() {
        while (true) {
            println("\n=== УПРАВЛЕНИЕ КЛИЕНТАМИ ===")
            println("1. Создать клиента")
            println("2. Список клиентов")
            println("3. Найти клиента по ИНН")
            println("0. Назад")
            print("Выберите пункт: ")

            when (readLine()?.trim()) {
                "1" -> createPerson()
                "2" -> showAllPersons()
                "3" -> findPerson()
                "0" -> return
                else -> println("Неверный выбор!")
            }
        }
    }

    private fun createPerson() {
        println("\n--- Создание клиента ---")

        try {
            print("ФИО: ")
            val fio = readLine()?.trim() ?: ""

            print("ИНН (12 цифр): ")
            val inn = readLine()?.trim() ?: ""

            print("Серия паспорта (4 цифры): ")
            val serPas = readLine()?.trim() ?: ""

            print("Номер паспорта (6 цифр): ")
            val numPas = readLine()?.trim() ?: ""

            val person = system.addPerson(fio, inn, numPas, serPas)
            println("✅ Клиент '${person.fio}' успешно создан!")

        } catch (e: Exception) {
            println("❌ Ошибка: ${e.message}")
        }
    }

    private fun showAllPersons() {
        println("\n--- Список клиентов ---")
        val persons = system.getAllPersons()
        if (persons.isEmpty()) {
            println("Клиенты не найдены")
        } else {
            persons.forEachIndexed { index, person ->
                println("${index + 1}. ${person.fio} (ИНН: ${person.inn})")
            }
        }
    }

    private fun findPerson() {
        print("Введите ИНН клиента: ")
        val inn = readLine()?.trim() ?: ""
        val person = system.findPersonByINN(inn)

        if (person != null) {
            println("✅ Найден клиент: ${person.fio}")
        } else {
            println("❌ Клиент не найден")
        }
    }

    private fun accountMenu() {
        while (true) {
            println("\n=== УПРАВЛЕНИЕ СЧЕТАМИ ===")
            println("1. Открыть текущий счет")
            println("2. Открыть депозитный счет")
            println("3. Открыть кредитный счет")
            println("4. Найти счет по номеру")
            println("0. Назад")
            print("Выберите пункт: ")

            when (readLine()?.trim()) {
                "1" -> createCurrentAccount()
                "2" -> createDepositAccount()
                "3" -> createCreditAccount()
                "4" -> findAccount()
                "0" -> return
                else -> println("Неверный выбор!")
            }
        }
    }

    private fun createCurrentAccount() {
        println("\n--- Открытие текущего счета ---")

        try {
            val person = selectPerson()
            val bank = selectBank()

            print("Начальный баланс: ")
            val balance = readLine()?.toDoubleOrNull() ?: 0.0

            val account = system.createCurrentAccount(person, bank, balance)
            println("✅ Текущий счет открыт! Номер: ${account.accountNumber}")

        } catch (e: Exception) {
            println("❌ Ошибка: ${e.message}")
        }
    }

    private fun createDepositAccount() {
        println("\n--- Открытие депозитного счета ---")

        try {
            val person = selectPerson()
            val bank = selectBank()

            print("Сумма вклада: ")
            val balance = readLine()?.toDoubleOrNull() ?: 0.0

            print("Срок (месяцев, мин. 3): ")
            val term = readLine()?.toIntOrNull() ?: 3

            print("Минимальный остаток: ")
            val minBalance = readLine()?.toDoubleOrNull() ?: 0.0

            print("Можно снимать? (да/нет): ")
            val isWithdrawable = readLine()?.trim()?.equals("да", ignoreCase = true) ?: false

            print("Годовая ставка (18-25%): ")
            val rate = readLine()?.toDoubleOrNull() ?: 18.0

            print("Начисление процентов (daily/monthly): ")
            val interestType = if (readLine()?.trim()?.equals("daily", ignoreCase = true) == true) {
                InterestCalculation.DAILY
            } else {
                InterestCalculation.MONTHLY
            }

            print("Пролонгация? (да/нет): ")
            val isProlongable = readLine()?.trim()?.equals("да", ignoreCase = true) ?: false

            val account = system.createDepositAccount(
                person, bank, balance, term, minBalance,
                isWithdrawable = isWithdrawable,
                annualRate = rate,
                interestCalculation = interestType,
                isProlongable = isProlongable
            )
            println("✅ Депозитный счет открыт! Номер: ${account.accountNumber}")

        } catch (e: Exception) {
            println("❌ Ошибка: ${e.message}")
        }
    }

    private fun createCreditAccount() {
        // Аналогично createDepositAccount
        println("Реализуй этот метод по аналогии!")
    }

    private fun operationMenu() {
        while (true) {
            println("\n=== ОПЕРАЦИИ СО СЧЕТАМИ ===")
            println("1. Пополнить счет")
            println("2. Снять средства")
            println("3. Информация о счете")
            println("0. Назад")
            print("Выберите пункт: ")

            when (readLine()?.trim()) {
                "1" -> depositOperation()
                "2" -> withdrawOperation()
                "3" -> accountInfoOperation()
                "0" -> return
                else -> println("Неверный выбор!")
            }
        }
    }

    private fun depositOperation() {
        print("Введите номер счета: ")
        val accountNumber = readLine()?.trim() ?: ""

        print("Сумма пополнения: ")
        val amount = readLine()?.toDoubleOrNull() ?: 0.0

        if (system.depositToAccount(accountNumber, amount)) {
            println("✅ Счет пополнен на $amount")
        } else {
            println("❌ Ошибка пополнения счета")
        }
    }

    private fun withdrawOperation() {
        print("Введите номер счета: ")
        val accountNumber = readLine()?.trim() ?: ""

        print("Сумма снятия: ")
        val amount = readLine()?.toDoubleOrNull() ?: 0.0

        if (system.withdrawFromAccount(accountNumber, amount)) {
            println("✅ Со счета снято $amount")
        } else {
            println("❌ Ошибка снятия средств")
        }
    }

    // Вспомогательные методы для выбора
    private fun selectPerson(): PhysicalPerson {
        showAllPersons()
        print("Выберите клиента (введите ИНН): ")
        val inn = readLine()?.trim() ?: ""
        return system.findPersonByINN(inn) ?: throw IllegalArgumentException("Клиент не найден")
    }

    private fun selectBank(): Bank {
        showAllBanks()
        print("Выберите банк (введите название): ")
        val name = readLine()?.trim() ?: ""
        return system.findBankByName(name) ?: throw IllegalArgumentException("Банк не найден")
    }

    private fun findAccount() {
        print("Введите номер счета: ")
        val number = readLine()?.trim() ?: ""
        val account = system.findAccountByNumber(number)

        if (account != null) {
            println("✅ Найден счет: ${account.getAccountInfo()}")
        } else {
            println("❌ Счет не найден")
        }
    }

    private fun accountInfoOperation() {
        findAccount()
    }
}

class BankingSystem {
    private val banks = mutableListOf<Bank>()
    private val persons = mutableListOf<PhysicalPerson>()
    private val accounts = mutableListOf<BankAccount>()

    // Банки
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

    fun findBankByName(name: String): Bank? {
        return banks.find {
            it.fullName.equals(name, ignoreCase = true) ||
                    it.shortName.equals(name, ignoreCase = true)
        }
    }

    fun getAllBanks(): List<Bank> = banks.toList()

    // Физические лица
    fun addPerson(fio: String, inn: String, numPas: String, serPas: String): PhysicalPerson {
        val existingPerson = persons.find { it.inn == inn }
        require(existingPerson == null) { "Человек с ИНН $inn уже существует" }

        val person = PhysicalPerson(fio, inn, numPas, serPas)
        persons.add(person)
        return person
    }

    fun findPersonByINN(inn: String): PhysicalPerson? {
        return persons.find { it.inn == inn }
    }

    fun getAllPersons(): List<PhysicalPerson> = persons.toList()

    // Текущие счета
    fun createCurrentAccount(person: PhysicalPerson, bank: Bank, initialBalance: Double = 0.0): CurrentAccount {
        require(person in persons) { "Физическое лицо не зарегистрировано в системе" }
        require(bank in banks) { "Банк не зарегистрирован в системе" }

        val personAccountsInBank = accounts.filter {
            it.owner == person && it.bank == bank && it is CurrentAccount
        }

        require(personAccountsInBank.size < 3) {
            "Нельзя иметь более 3 текущих счетов в одном банке"
        }

        val accountNumber = "CUR${UUID.randomUUID().toString().take(8).uppercase()}"
        val account = CurrentAccount(accountNumber, person, bank, initialBalance)
        accounts.add(account)
        return account
    }

    // Депозитные счета
    fun createDepositAccount(
        person: PhysicalPerson,
        bank: Bank,
        initialBalance: Double,
        termMonths: Int,
        minBalance: Double,
        isReplenishable: Boolean = true, // По ТЗ: всегда пополняемый
        isWithdrawable: Boolean,
        annualRate: Double,
        interestCalculation: InterestCalculation,
        isProlongable: Boolean
    ): DepositAccount {
        require(person in persons) { "Физическое лицо не зарегистрировано в системе" }
        require(bank in banks) { "Банк не зарегистрирован в системе" }

        // Проверка: не более 1 депозитного счета в банке
        val existingDeposit = accounts.find {
            it.owner == person && it.bank == bank && it is DepositAccount
        }
        require(existingDeposit == null) {
            "Нельзя иметь более 1 депозитного счета в одном банке"
        }

        // Проверка минимального баланса
        require(initialBalance >= minBalance) {
            "Начальный баланс должен быть не меньше минимального: $minBalance"
        }

        val accountNumber = "DEP${UUID.randomUUID().toString().take(8).uppercase()}"
        val account = DepositAccount(
            accountNumber, person, bank, initialBalance,
            termMonths, minBalance, isReplenishable, isWithdrawable,
            annualRate, interestCalculation, isProlongable
        )
        accounts.add(account)
        return account
    }

    // Кредитные счета
    fun createCreditAccount(
        person: PhysicalPerson,
        bank: Bank,
        creditLimit: Double,
        creditRate: Double,
        creditTerm: Int,
        initialBalance: Double = 0.0
    ): CreditAccount {
        require(person in persons) { "Физическое лицо не зарегистрировано в системе" }
        require(bank in banks) { "Банк не зарегистрирован в системе" }
        require(creditLimit > 0) { "Кредитный лимит должен быть положительным" }

        val accountNumber = "CRD${UUID.randomUUID().toString().take(8).uppercase()}"
        val account = CreditAccount(
            accountNumber, person, bank, initialBalance,
            creditLimit, creditRate, creditTerm
        )
        accounts.add(account)
        return account
    }

    // Поиск и операции
    fun findAccountByNumber(accountNumber: String): BankAccount? {
        return accounts.find { it.accountNumber == accountNumber }
    }

    fun findAccountsByPerson(person: PhysicalPerson): List<BankAccount> {
        return accounts.filter { it.owner == person }
    }

    fun findAccountsByBank(bank: Bank): List<BankAccount> {
        return accounts.filter { it.bank == bank }
    }

    fun findAccountsByPersonAndBank(person: PhysicalPerson, bank: Bank): List<BankAccount> {
        return accounts.filter { it.owner == person && it.bank == bank }
    }

    // Операции со счетами
    fun depositToAccount(accountNumber: String, amount: Double): Boolean {
        val account = findAccountByNumber(accountNumber)
        return if (account != null && account.isActive) {
            account.deposit(amount)
            true
        } else {
            false
        }
    }

    fun withdrawFromAccount(accountNumber: String, amount: Double): Boolean {
        val account = findAccountByNumber(accountNumber)
        return if (account != null && account.isActive) {
            account.withdraw(amount)
        } else {
            false
        }
    }

    fun closeAccount(accountNumber: String): Boolean {
        val account = findAccountByNumber(accountNumber)
        return if (account != null && account.isActive && account.canClose()) {
            // Логика закрытия счета (в реальной системе была бы сложнее)
            true
        } else {
            false
        }
    }

    // Статистика
    fun getTotalAccounts(): Int = accounts.size
    fun getActiveAccounts(): Int = accounts.count { it.isActive }
    fun getTotalBalance(): Double = accounts.sumOf { it.balance }
}

class Bank(
    val fullName: String,
    val shortName: String,
    val interestRate: Double //Процент на остаток
){
    val id: UUID = UUID.randomUUID()

    init {
        require(fullName.trim().isNotBlank()) { "Полное название не может быть пустым" }
        require(shortName.trim().isNotBlank()) { "Краткое название не может быть пустым" }
        require(interestRate in 0.1..2.0) { "Процент должен быть от 0.1 до 2.0" }
    }
}

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
}

abstract class BankAccount(
    val accountNumber: String,
    val owner: PhysicalPerson,
    val bank: Bank,
    var balance: Double = 0.0
) {
    val openDate: LocalDate = LocalDate.now()
    var closeDate: LocalDate? = null
    val isActive: Boolean get() = closeDate == null

    abstract fun canClose(): Boolean

    open fun getAccountInfo(): String = "Счет $accountNumber, баланс: $balance"

    fun deposit(amount: Double) {
        require(amount > 0) { "Сумма должна быть положительной" }
        balance += amount
    }

    open fun withdraw(amount: Double): Boolean {
        if (amount > 0 && balance >= amount) {
            balance -= amount
            return true
        }
        return false
    }
}

class CurrentAccount(
    accountNumber: String,
    owner: PhysicalPerson,
    bank: Bank,
    balance: Double = 0.0
) : BankAccount(accountNumber, owner, bank, balance) {

    override fun canClose(): Boolean = balance == 0.0
}

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

    override fun canClose(): Boolean = LocalDate.now() >= endDate && balance >= minBalance

    override fun withdraw(amount: Double): Boolean {
        if (!isWithdrawable) return false
        if (balance - amount >= minBalance) {
            balance -= amount
            return true
        }
        return false
    }

    override fun getAccountInfo(): String =
        "Депозитный счет $accountNumber, баланс: $balance, до $endDate"
}

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

    override fun canClose(): Boolean = balance == 0.0

    override fun withdraw(amount: Double): Boolean {
        if (amount > 0 && balance - amount >= -creditLimit) {
            balance -= amount
            return true
        }
        return false
    }

    fun getAvailableCredit(): Double = creditLimit + balance

    override fun getAccountInfo(): String =
        "Кредитный счет $accountNumber, баланс: $balance, доступно: ${getAvailableCredit()}"
}

enum class InterestCalculation {
    DAILY, MONTHLY
}