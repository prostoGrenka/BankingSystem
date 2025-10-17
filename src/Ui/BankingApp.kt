package ui

import system.BankingSystem
import entities.Bank
import entities.PhysicalPerson
import enums.InterestCalculation

/**
 * Класс пользовательского интерфейса для взаимодействия с банковской системой
 * Реализует консольное меню для управления банками, клиентами и счетами
 */
class BankingApp(private val system: BankingSystem) {

    /**
     * Запускает главный цикл приложения
     */
    fun start() {
        println("=== БАНКОВСКАЯ СИСТЕМА ===")
        println("Добро пожаловать в систему управления банками!")

        while (true) {
            showMainMenu()
            when (readLine()?.trim()) {
                "1" -> bankMenu()
                "2" -> personMenu()
                "3" -> accountMenu()
                "4" -> operationMenu()
                "5" -> showStatistics()
                "0" -> {
                    println("Выход из системы...")
                    return
                }
                else -> println("❌ Неверный выбор! Попробуйте снова.")
            }
        }
    }

    // region Главное меню

    private fun showMainMenu() {
        println("\n=== ГЛАВНОЕ МЕНЮ ===")
        println("1. 🏦 Управление банками")
        println("2. 👤 Управление клиентами")
        println("3. 💳 Управление счетами")
        println("4. 💰 Операции со счетами")
        println("5. 📊 Статистика системы")
        println("0. 🚪 Выход")
        print("Выберите пункт: ")
    }

    // endregion

    // region Меню управления банками

    private fun bankMenu() {
        while (true) {
            println("\n=== 🏦 УПРАВЛЕНИЕ БАНКАМИ ===")
            println("1. Создать банк")
            println("2. Показать все банки")
            println("3. Найти банк по названию")
            println("4. Счета в банке")
            println("0. Назад")
            print("Выберите пункт: ")

            when (readLine()?.trim()) {
                "1" -> createBank()
                "2" -> showAllBanks()
                "3" -> findBank()
                "4" -> showBankAccounts()
                "0" -> return
                else -> println("❌ Неверный выбор!")
            }
        }
    }

    private fun createBank() {
        println("\n--- Создание банка ---")

        try {
            print("Введите полное название банка: ")
            val fullName = readLine()?.trim() ?: ""

            print("Введите краткое название банка: ")
            val shortName = readLine()?.trim() ?: ""

            print("Введите процентную ставку по текущим счетам (0.1-2.0): ")
            val rate = readLine()?.toDoubleOrNull() ?: 0.0

            val bank = system.addBank(fullName, shortName, rate)
            println("✅ Банк '${bank.shortName}' успешно создан!")

        } catch (e: Exception) {
            println("❌ Ошибка при создании банка: ${e.message}")
        }
    }

    private fun showAllBanks() {
        println("\n--- Список банков ---")
        val banks = system.getAllBanks()
        if (banks.isEmpty()) {
            println("📭 Банки не найдены")
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

    private fun showBankAccounts() {
        val bank = selectBank() ?: return
        val accounts = system.findAccountsByBank(bank)

        println("\n--- Счета в банке ${bank.shortName} ---")
        if (accounts.isEmpty()) {
            println("📭 Счета не найдены")
        } else {
            accounts.forEachIndexed { index, account ->
                println("${index + 1}. ${account.getAccountInfo()}")
            }
        }
    }

    // endregion

    // region Меню управления клиентами

    private fun personMenu() {
        while (true) {
            println("\n=== 👤 УПРАВЛЕНИЕ КЛИЕНТАМИ ===")
            println("1. Создать клиента")
            println("2. Показать всех клиентов")
            println("3. Найти клиента по ИНН")
            println("4. Счета клиента")
            println("0. Назад")
            print("Выберите пункт: ")

            when (readLine()?.trim()) {
                "1" -> createPerson()
                "2" -> showAllPersons()
                "3" -> findPerson()
                "4" -> showPersonAccounts()
                "0" -> return
                else -> println("❌ Неверный выбор!")
            }
        }
    }

    private fun createPerson() {
        println("\n--- Создание клиента ---")

        try {
            print("Введите ФИО клиента: ")
            val fio = readLine()?.trim() ?: ""

            print("Введите ИНН (12 цифр): ")
            val inn = readLine()?.trim() ?: ""

            print("Введите серию паспорта (4 цифры): ")
            val serPas = readLine()?.trim() ?: ""

            print("Введите номер паспорта (6 цифр): ")
            val numPas = readLine()?.trim() ?: ""

            val person = system.addPerson(fio, inn, numPas, serPas)
            println("✅ Клиент '${person.fio}' успешно создан!")

        } catch (e: Exception) {
            println("❌ Ошибка при создании клиента: ${e.message}")
        }
    }

    private fun showAllPersons() {
        println("\n--- Список клиентов ---")
        val persons = system.getAllPersons()
        if (persons.isEmpty()) {
            println("📭 Клиенты не найдены")
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

    private fun showPersonAccounts() {
        val person = selectPerson() ?: return
        val accounts = system.findAccountsByPerson(person)

        println("\n--- Счета клиента ${person.fio} ---")
        if (accounts.isEmpty()) {
            println("📭 Счета не найдены")
        } else {
            accounts.forEachIndexed { index, account ->
                println("${index + 1}. ${account.getAccountInfo()}")
            }
        }
    }

    // endregion

    // region Меню управления счетами

    private fun accountMenu() {
        while (true) {
            println("\n=== 💳 УПРАВЛЕНИЕ СЧЕТАМИ ===")
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
                else -> println("❌ Неверный выбор!")
            }
        }
    }

    private fun createCurrentAccount() {
        println("\n--- Открытие текущего счета ---")

        try {
            val person = selectPerson() ?: return
            val bank = selectBank() ?: return

            print("Введите начальный баланс: ")
            val balance = readLine()?.toDoubleOrNull() ?: 0.0

            val account = system.createCurrentAccount(person, bank, balance)
            println("✅ Текущий счет открыт!")
            println("📋 Номер счета: ${account.accountNumber}")
            println("💳 Баланс: $balance")

        } catch (e: Exception) {
            println("❌ Ошибка при открытии счета: ${e.message}")
        }
    }

    private fun createDepositAccount() {
        println("\n--- Открытие депозитного счета ---")

        try {
            val person = selectPerson() ?: return
            val bank = selectBank() ?: return

            print("Введите сумму вклада: ")
            val balance = readLine()?.toDoubleOrNull() ?: 0.0

            print("Введите срок вклада (месяцев, минимум 3): ")
            val term = readLine()?.toIntOrNull() ?: 3

            print("Введите минимальный остаток: ")
            val minBalance = readLine()?.toDoubleOrNull() ?: 0.0

            print("Разрешить снятие средств? (да/нет): ")
            val isWithdrawable = readLine()?.trim()?.equals("да", ignoreCase = true) ?: false

            print("Введите годовую ставку (18-25%): ")
            val rate = readLine()?.toDoubleOrNull() ?: 18.0

            print("Выберите начисление процентов (daily/monthly): ")
            val interestType = if (readLine()?.trim()?.equals("daily", ignoreCase = true) == true) {
                InterestCalculation.DAILY
            } else {
                InterestCalculation.MONTHLY
            }

            print("Включить автопролонгацию? (да/нет): ")
            val isProlongable = readLine()?.trim()?.equals("да", ignoreCase = true) ?: false

            val account = system.createDepositAccount(
                person, bank, balance, term, minBalance,
                isWithdrawable = isWithdrawable,
                annualRate = rate,
                interestCalculation = interestType,
                isProlongable = isProlongable
            )
            println("✅ Депозитный счет открыт!")
            println("📋 Номер счета: ${account.accountNumber}")
            println("💰 Сумма: $balance")
            println("📅 Срок: $term месяцев")
            println("🎯 Ставка: $rate%")

        } catch (e: Exception) {
            println("❌ Ошибка при открытии депозита: ${e.message}")
        }
    }

    private fun createCreditAccount() {
        println("\n--- Открытие кредитного счета ---")

        try {
            val person = selectPerson() ?: return
            val bank = selectBank() ?: return

            print("Введите кредитный лимит: ")
            val limit = readLine()?.toDoubleOrNull() ?: 0.0

            print("Введите процентную ставку по кредиту: ")
            val rate = readLine()?.toDoubleOrNull() ?: 0.0

            print("Введите срок кредита (месяцев): ")
            val term = readLine()?.toIntOrNull() ?: 12

            val account = system.createCreditAccount(person, bank, limit, rate, term)
            println("✅ Кредитный счет открыт!")
            println("📋 Номер счета: ${account.accountNumber}")
            println("💳 Кредитный лимит: $limit")
            println("🎯 Ставка: $rate%")

        } catch (e: Exception) {
            println("❌ Ошибка при открытии кредитного счета: ${e.message}")
        }
    }

    private fun findAccount() {
        print("Введите номер счета: ")
        val number = readLine()?.trim() ?: ""
        val account = system.findAccountByNumber(number)

        if (account != null) {
            println("✅ Найден счет:")
            println("📋 ${account.getAccountInfo()}")
        } else {
            println("❌ Счет не найден")
        }
    }

    // endregion

    // region Меню операций со счетами

    private fun operationMenu() {
        while (true) {
            println("\n=== 💰 ОПЕРАЦИИ СО СЧЕТАМИ ===")
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
                else -> println("❌ Неверный выбор!")
            }
        }
    }

    private fun depositOperation() {
        print("Введите номер счета: ")
        val accountNumber = readLine()?.trim() ?: ""

        print("Введите сумму пополнения: ")
        val amount = readLine()?.toDoubleOrNull() ?: 0.0

        if (system.depositToAccount(accountNumber, amount)) {
            println("✅ Счет успешно пополнен на $amount")
        } else {
            println("❌ Ошибка при пополнении счета")
        }
    }

    private fun withdrawOperation() {
        print("Введите номер счета: ")
        val accountNumber = readLine()?.trim() ?: ""

        print("Введите сумму снятия: ")
        val amount = readLine()?.toDoubleOrNull() ?: 0.0

        if (system.withdrawFromAccount(accountNumber, amount)) {
            println("✅ Со счета успешно снято $amount")
        } else {
            println("❌ Ошибка при снятии средств")
        }
    }

    private fun accountInfoOperation() {
        findAccount()
    }

    // endregion

    // region Вспомогательные методы

    private fun selectPerson(): PhysicalPerson? {
        showAllPersons()
        print("Выберите клиента (введите ИНН): ")
        val inn = readLine()?.trim() ?: ""
        val person = system.findPersonByINN(inn)

        if (person == null) {
            println("❌ Клиент не найден")
            return null
        }
        return person
    }

    private fun selectBank(): Bank? {
        showAllBanks()
        print("Выберите банк (введите название): ")
        val name = readLine()?.trim() ?: ""
        val bank = system.findBankByName(name)

        if (bank == null) {
            println("❌ Банк не найден")
            return null
        }
        return bank
    }

    private fun showStatistics() {
        println("\n=== 📊 СТАТИСТИКА СИСТЕМЫ ===")
        println("🏦 Количество банков: ${system.getAllBanks().size}")
        println("👤 Количество клиентов: ${system.getAllPersons().size}")
        println("💳 Всего счетов: ${system.getTotalAccounts()}")
        println("✅ Активных счетов: ${system.getActiveAccounts()}")
        println("💰 Общий баланс: ${system.getTotalBalance()}")
    }

    // endregion
}