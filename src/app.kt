import kotlin.math.floor
import kotlin.math.min
import kotlin.random.Random

var terminalWork = true

// начальные средства пользователя
val capitalUser = mutableMapOf("RUB" to 1000000*100, "USD" to 0, "EUR" to 0, "USDT" to 0, "BTC" to 0)

// начальные средства терминала
val capitalTerminal = mutableMapOf("RUB" to 10000*100, "USD" to 1000*100, "EUR" to 1000*100, "USDT" to 1000*100, "BTC" to 15*100000)

// структура для хранения курсов
class Course(private val nameOne: String, private var nameTwo: String, private var course: Int) {
    fun getNameOne(): String {
        return this.nameOne
    }
    fun getNameTwo(): String {
        return this.nameTwo
    }
    fun getCourse(): Int {
        return this.course
    }
    fun setCourse(course: Int) {
        this.course = course
    }
}

// массив со всеми валютными парами
var courseClass = arrayOf(
    Course("RUB", "USD", 900),
    Course("RUB", "EUR", 950),
    Course("USD", "EUR", 110),
    Course("USD", "USDT", 100),
    Course("USD", "BTC", 400))

// вспомогательный класс - полиморфная пара
class Pair<T>(private var first: T, private var second: T) {
    fun getFirst(): T {
        return this.first
    }
    fun getSecond(): T {
        return this.second
    }
    fun setFirst(first: T) {
        this.first = first
    }
    fun setSecond(second: T) {
        this.second = second
    }
}

// наименование валют
val nameCurrency = arrayOf("RUB", "USD", "EUR", "USDT", "BTC")

// печать счета пользователя
fun printCapitalUser(n: Int) {
    if (n == 0)
        print("До свидания! Сессия завершена. ")
    println("На вашем счету:")
    for (i in nameCurrency.indices) {
        if (i == 4)
            println("${i+1}. ${nameCurrency[i]} = ${capitalUser[nameCurrency[i]]}")
        else
            println("${i+1}. ${nameCurrency[i]} = ${capitalUser[nameCurrency[i]]}")
    }
}

// печать счета терминала
fun printCapitalTerminal() {
    println("Счет терминала:")
    for (i in nameCurrency.indices) {
        if (i == 4)
            println("${i+1}. ${nameCurrency[i]} = ${capitalTerminal[nameCurrency[i]]}")
        else
            println("${i+1}. ${nameCurrency[i]} = ${capitalTerminal[nameCurrency[i]]}")
    }
}

// печать актуальных курсов
fun printCourse(n: Int) {
    if (n == 0) {
        printCapitalTerminal()
        println("--------------------")
    }
    println("Актуальные обменные курсы:")
    for (i in courseClass.indices) {
        println("${i+1}. ${courseClass[i].getNameOne()}/${courseClass[i].getNameTwo()} = ${courseClass[i].getCourse()}")
    }
}

// печать меню
fun printMenu(n: Int) {
    println("""
        МЕНЮ
        1. Для завершения сессии напишите 'выйти'
        2. Для возвращения к предыдущему шагу напишите 'назад'
        3. Для просмотра баланса напишите 'баланс'
        4. Для просмотра актуального курса напишите 'курс'
        5. Для просмотра баланса терминала напишите 'терминал'""".trimIndent())
    if (n == 0)
        println("--------------------")
}

// проверка ввода на пункты меню
fun checkInput(str: String): String {
    var ans = str
    when(str) {
        "баланс" -> {
            printCapitalUser(1)
            ans = "end"
        }
        "курс" -> {
            printCourse(1)
            ans = "end"
        }
        "терминал" -> {
            printCapitalTerminal()
            ans = "end"
        }
    }
    return ans
}

// генерация изменений курса для валютной пары
fun getRandomNum() : Int {
    val number = Random.nextInt(0, 50)
    return if (Random.nextInt(0, 2) == 0)
        number
    else
        -number
}

// изменение курса валютных пар
fun updateCourse() {
    for (i in courseClass.indices) {
        courseClass[i].setCourse(courseClass[i].getCourse() + (courseClass[i].getCourse().toDouble() * getRandomNum().toDouble() / 1000).toInt())
    }
}

// сообщение покупка/продажа в зависимости от валютной пары
fun printBuy(numCourse : Int) {
    println("Обменять ${courseClass[numCourse-1].getNameTwo()} на ${courseClass[numCourse-1].getNameOne()}, введите '1'")
    println("Обменять ${courseClass[numCourse-1].getNameOne()} на ${courseClass[numCourse-1].getNameTwo()}, введите '2'")
}

// проверка на максимально возможный обмен валюты
fun checkCourse(numCourse: Int, exchange: Int): Int {
    return if (numCourse in 1..courseClass.size) {
        if (exchange == 1) {
            min(capitalTerminal[courseClass[numCourse-1].getNameOne()]!! / courseClass[numCourse - 1].getCourse(), capitalUser[courseClass[numCourse-1].getNameTwo()]!!)
        } else {
            min(capitalTerminal[courseClass[numCourse-1].getNameTwo()]!!, capitalUser[courseClass[numCourse-1].getNameOne()]!! / courseClass[numCourse - 1].getCourse())
        }
    } else {
        -1
    }
}

// сообщение объем обмена
fun printExchange(numCourse: Int, exchange: Int): Int {
    val a = if (numCourse in 1..courseClass.size) {
        if (exchange == 1) {
            courseClass[numCourse - 1].getNameOne()
        } else {
            courseClass[numCourse - 1].getNameTwo()
        }
    } else {
        ""
    }
    val checkCourse = checkCourse(numCourse, exchange)
    if (checkCourse == -1 || checkCourse == 0) {
        return -1
    }
    println("Введите сумму от 1 $a до $checkCourse $a")
    return 1
}

// совершение обмена валюты
fun exchange(exchange: Int, numCourse: Int, count: Int): Pair<Boolean> {
    val pairBool = Pair(first = true, second = true)
    if (exchange == 1) {
        val delta = floor(count / courseClass[numCourse - 1].getCourse().toDouble()).toInt()
        if (capitalUser[courseClass[numCourse - 1].getNameTwo()]!! >= delta) {
            if (capitalTerminal[courseClass[numCourse - 1].getNameOne()]!! >= count) {
                capitalUser[courseClass[numCourse - 1].getNameTwo()] = capitalUser[courseClass[numCourse - 1].getNameTwo()]!! - delta
                capitalTerminal[courseClass[numCourse - 1].getNameTwo()] = capitalTerminal[courseClass[numCourse - 1].getNameTwo()]!! + delta
                capitalTerminal[courseClass[numCourse - 1].getNameOne()] = capitalTerminal[courseClass[numCourse - 1].getNameOne()]!! + count
                capitalUser[courseClass[numCourse - 1].getNameOne()] = capitalUser[courseClass[numCourse - 1].getNameOne()]!! - count
                println("Продано $delta ${courseClass[numCourse - 1].getNameTwo()} за $count ${courseClass[numCourse - 1].getNameOne()}")
            } else {
                pairBool.setSecond(false)
            }
        } else {
            pairBool.setFirst(false)
        }
    } else {
        if (capitalTerminal[courseClass[numCourse - 1].getNameTwo()]!! >= count) {
            val delta = count*courseClass[numCourse - 1].getCourse()
            if (capitalUser[courseClass[numCourse - 1].getNameOne()]!! >= delta) {
                capitalTerminal[courseClass[numCourse - 1].getNameTwo()] = capitalTerminal[courseClass[numCourse - 1].getNameTwo()]!! - count
                capitalUser[courseClass[numCourse - 1].getNameTwo()] = capitalUser[courseClass[numCourse - 1].getNameTwo()]!! + count
                capitalUser[courseClass[numCourse - 1].getNameOne()] = capitalUser[courseClass[numCourse - 1].getNameOne()]!! - delta
                capitalTerminal[courseClass[numCourse - 1].getNameOne()] = capitalTerminal[courseClass[numCourse - 1].getNameOne()]!! + delta
                println("Куплено $count ${courseClass[numCourse - 1].getNameTwo()} за $delta ${courseClass[numCourse - 1].getNameOne()}")
            } else {
                pairBool.setFirst(false)
            }
        } else {
            pairBool.setSecond(false)
        }
    }
    return pairBool
}

fun main() {
    println("Для просмотра напишите 'меню'")
    printMenu(1)
    printCourse(1)

    var ans = ""
    var transactionSuccessful = false
    while (terminalWork || transactionSuccessful) {
        if (transactionSuccessful) {
            transactionSuccessful = false
            terminalWork = true
        }
        if (ans == "выйти" || ans == "назад") {
            printCapitalUser(0)
            terminalWork = false
            continue
        }
        if (ans.isEmpty() || ans == "end") {
            println("Выберите валютную пару, написав ее номер в консоль:")
        } else if (ans == "меню") {
            printMenu(1)
            println("Выберите валютную пару, написав ее номер в консоль:")
        } else {
            println("Некорректный ввод. Повторите попытку")
        }
        ans = checkInput(readlnOrNull()?.lowercase()!!.replace(',', '.'))

        var numCourse: Int
        try {
            numCourse = ans.toInt()
            if (numCourse !in 1..courseClass.size) continue
        } catch (e: Exception) {
            continue
        }
        ans = ""

        var yesORno = ""

        while (terminalWork) {
            when(yesORno) {
                "выйти" -> {
                    printCapitalUser(0)
                    terminalWork = false
                    break
                }
                "назад" -> {
                    break
                }
                "меню" -> {
                    printMenu(1)
                    printBuy(numCourse)
                }
                "", "end" -> {
                    printBuy(numCourse)
                }
                else -> println("Некорректный ввод. Повторите попытку")
            }
            yesORno = checkInput(readlnOrNull()?.lowercase()!!.replace(',', '.'))

            val exchange = if (yesORno == "1") 1
            else if (yesORno == "2") 2
            else continue
            yesORno = ""

            var cnt = ""
            var nowExchange = true
            while (terminalWork) {
                when(cnt) {
                    "выйти" -> {
                        printCapitalUser(0)
                        terminalWork = false
                        break
                    }
                    "назад" -> break
                    "меню" -> {
                        printMenu(1)
                        if (printExchange(numCourse, exchange) == -1) nowExchange = false
                    }
                    "", "end" -> if (printExchange(numCourse, exchange) == -1) nowExchange = false
                    else -> {
                        when(cnt) {
                            "string" -> println("Некорректный ввод. Необходимо ввести число")
                            "negative" -> println("Сумма должна быть положительным числом")
                            "small" -> println("Введена сумма менее допустимой")
                            "person" -> println("У вас недостаточно средств для совершения обмена")
                            "terminal" -> println("Терминалу не хватает средств для совершения обмена")
                            "fraction" -> println("Необходимо ввести целое число")
                            "fail" -> {
                                println("Невозможно провести обмен")
                                break
                            }
                        }
                        println("Введите новую сумму")
                    }
                }
                if (nowExchange) {
                    cnt = checkInput(readlnOrNull()?.lowercase()!!.replace(',', '.'))
                } else {
                    cnt = "fail"
                    nowExchange = true
                }

                var count: Int
                try {
                    count = cnt.toInt()
                    if (count <= 0) {
                        cnt = "negative"
                        continue
                    }
                } catch (e: Exception) {
                    if (cnt.substringAfter('.').isEmpty()) {
                        cnt = "fraction"
                        continue
                    }
                    if (cnt == "end" || cnt == "меню" || cnt == "назад" || cnt == "выйти" || cnt == "fail")
                        continue
                    cnt = "string"
                    continue
                }
                cnt = ""

                val pair = exchange(exchange, numCourse, count)
                if (pair.getFirst() && pair.getSecond()) {
                    updateCourse()
                    printCourse(1)
                    terminalWork = false
                    transactionSuccessful = true
                } else {
                    cnt = if (!pair.getFirst())
                        "person"
                    else
                        "terminal"
                }
            }
        }
        if (!terminalWork) continue
        ans = ""
    }
}