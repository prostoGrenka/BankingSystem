import system.BankingSystem
import ui.BankingApp

/**
 * Главный класс приложения BankSystem
 * Точка входа в программу
 */
fun main() {
    // Создаем экземпляр банковской системы
    val bankingSystem = BankingSystem()

    // Создаем и запускаем приложение
    val app = BankingApp(bankingSystem)

    // Добавляем тестовые данные для демонстрации
    initializeTestData(bankingSystem)

    // Запускаем главный цикл приложения
    app.start()
}

/**
 * Инициализирует тестовые данные для демонстрации работы системы
 */
private fun initializeTestData(system: BankingSystem) {
    try {
        // Создаем тестовые банки
        val sber = system.addBank("Сбербанк России", "Сбер", 1.5)
        val vtb = system.addBank("Банк ВТБ", "ВТБ", 1.2)
        val tinkoff = system.addBank("Тинькофф Банк", "Тинькофф", 1.8)

        // Создаем тестовых клиентов
        val ivanov = system.addPerson("Иванов Иван Иванович", "123456789012", "123456", "1234")
        val petrova = system.addPerson("Петрова Мария Сергеевна", "234567890123", "234567", "2345")
        val sidorov = system.addPerson("Сидоров Алексей Петрович", "345678901234", "345678", "3456")

        // Создаем тестовые счета
        system.createCurrentAccount(ivanov, sber, 15000.0)
        system.createCurrentAccount(petrova, vtb, 25000.0)
        system.createDepositAccount(
            sidorov, tinkoff, 100000.0, 12, 50000.0,
            isWithdrawable = false, 20.0,
            enums.InterestCalculation.MONTHLY, true
        )

        println("✅ Тестовые данные успешно загружены!")
        println("🏦 Доступно банков: ${system.getAllBanks().size}")
        println("👤 Зарегистрировано клиентов: ${system.getAllPersons().size}")
        println("💳 Создано счетов: ${system.getTotalAccounts()}")

    } catch (e: Exception) {
        // Игнорируем ошибки, если данные уже существуют
        println("ℹ️  Тестовые данные уже загружены или произошла ошибка: ${e.message}")
    }
}