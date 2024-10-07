import kotlin.math.floor
import kotlin.math.min
import kotlin.random.Random

var f1 = true

// начальные средства пользователя
val capitalUser = mutableMapOf("RUB" to 1000000*100, "USD" to 0, "EUR" to 0, "USDT" to 0, "BTC" to 0)

// начальные средства терминала
val capitalTerminal = mutableMapOf("RUB" to 10000*100, "USD" to 1000*100, "EUR" to 1000*100, "USDT" to 1000*100, "BTC" to 15*100000)

// курс валютных пар
var course = arrayOf(900, 950, 110, 100, 400)

// наименование валютных пар
var pairCourse = arrayOf(Pair("RUB", "USD"), Pair("RUB", "EUR"), Pair("USD", "EUR"), Pair("USD", "USDT"), Pair("USD", "BTC"))

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
    for (i in 0..4) {
        if (i == 4)
            println("${i+1}. ${nameCurrency[i]} = ${capitalUser[nameCurrency[i]]}")
        else
            println("${i+1}. ${nameCurrency[i]} = ${capitalUser[nameCurrency[i]]}")
    }
}

// печать счета терминала
fun printCapitalTerminal() {
    println("Счет терминала:")
    for (i in 0..4) {
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
    for (i in 0..4) {
        println("${i+1}. ${pairCourse[i].getFirst()}/${pairCourse[i].getSecond()} = ${course[i]}")
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
    for (i in 0..4) {
        course[i] += (course[i].toDouble()*getRandomNum().toDouble()/1000).toInt()
    }
}

// сообщение покупка/продажа в зависимости от валютной пары
fun printBuy(numCourse : Int) {
    println("Обменять ${pairCourse[numCourse-1].getSecond()} на ${pairCourse[numCourse-1].getFirst()}, введите '1'")
    println("Обменять ${pairCourse[numCourse-1].getFirst()} на ${pairCourse[numCourse-1].getSecond()}, введите '2'")
}

// проверка на максимально возможный обмен валюты
fun checkCourse(numCourse: Int, exchange: Int): Int {
    return if (numCourse in 1..5) {
        if (exchange == 1) {
            min(capitalTerminal[pairCourse[numCourse-1].getFirst()]!! / course[numCourse - 1], capitalUser[pairCourse[numCourse-1].getSecond()]!!)
        } else {
            min(capitalTerminal[pairCourse[numCourse-1].getSecond()]!!, capitalUser[pairCourse[numCourse-1].getFirst()]!! / course[numCourse - 1])
        }
    } else {
        -1
    }
}

// сообщение объем обмена
fun printExchange(numCourse: Int, exchange: Int): Int {
    val a = if (numCourse in 1..5) {
        if (exchange == 1) {
            pairCourse[numCourse - 1].getFirst()
        } else {
            pairCourse[numCourse - 1].getSecond()
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
        val delta = floor(count / course[numCourse - 1].toDouble()).toInt()
        if (capitalUser[pairCourse[numCourse - 1].getSecond()]!! >= delta) {
            if (capitalTerminal[pairCourse[numCourse - 1].getFirst()]!! >= count) {
                capitalUser[pairCourse[numCourse - 1].getSecond()] = capitalUser[pairCourse[numCourse - 1].getSecond()]!! - delta
                capitalTerminal[pairCourse[numCourse - 1].getSecond()] = capitalTerminal[pairCourse[numCourse - 1].getSecond()]!! + delta
                capitalTerminal[pairCourse[numCourse - 1].getFirst()] = capitalTerminal[pairCourse[numCourse - 1].getFirst()]!! + count
                capitalUser[pairCourse[numCourse - 1].getFirst()] = capitalUser[pairCourse[numCourse - 1].getFirst()]!! - count
                println("Продано $delta ${pairCourse[numCourse - 1].getSecond()} за $count ${pairCourse[numCourse - 1].getFirst()}")
            } else {
                pairBool.setSecond(false)
            }
        } else {
            pairBool.setFirst(false)
        }
    } else {
        if (capitalTerminal[pairCourse[numCourse - 1].getSecond()]!! >= count) {
            val delta = count*course[numCourse - 1]
            if (capitalUser[pairCourse[numCourse - 1].getFirst()]!! >= delta) {
                capitalTerminal[pairCourse[numCourse - 1].getSecond()] = capitalTerminal[pairCourse[numCourse - 1].getSecond()]!! - count
                capitalUser[pairCourse[numCourse - 1].getSecond()] = capitalUser[pairCourse[numCourse - 1].getSecond()]!! + count
                capitalUser[pairCourse[numCourse - 1].getFirst()] = capitalUser[pairCourse[numCourse - 1].getFirst()]!! - delta
                capitalTerminal[pairCourse[numCourse - 1].getFirst()] = capitalTerminal[pairCourse[numCourse - 1].getFirst()]!! + delta
                println("Куплено $count ${pairCourse[numCourse - 1].getSecond()} за $delta ${pairCourse[numCourse - 1].getFirst()}")
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
    var trade = false
    while (f1 || trade) {
        if (trade) {
            trade = false
            f1 = true
        }
        if (ans.equals("выйти") || ans.equals("назад")) {
            printCapitalUser(0)
            f1 = false
            continue
        }
        if (ans.isEmpty() || ans.equals("end")) {
            println("Выберите валютную пару, написав ее номер в консоль:")
        } else if (ans.equals("меню")) {
            printMenu(1)
            println("Выберите валютную пару, написав ее номер в консоль:")
        } else {
            println("Некорректный ввод. Повторите попытку")
        }
        ans = checkInput(readlnOrNull()?.lowercase()!!.replace(',', '.'))

        var numCourse: Int
        try {
            numCourse = ans.toInt()
            if (numCourse !in 1..5) continue
        } catch (e: Exception) {
            continue
        }
        ans = ""

        var yN = ""

        while (f1) {
            when(yN) {
                "выйти" -> {
                    printCapitalUser(0)
                    f1 = false
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
            yN = checkInput(readlnOrNull()?.lowercase()!!.replace(',', '.'))

            val exchange = if (yN == "1") 1
            else if (yN == "2") 2
            else continue
            yN = ""

            var cnt = ""
            var nowExchange = true
            while (f1) {
                when(cnt) {
                    "выйти" -> {
                        printCapitalUser(0)
                        f1 = false
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
                    if (!cnt.substringAfter('.').isNotEmpty()) {
                        cnt = "fraction"
                        continue
                    }
                    if (cnt.equals("end") || cnt.equals("меню") || cnt.equals("назад") || cnt.equals("выйти") || cnt.equals("fail"))
                        continue
                    cnt = "string"
                    continue
                }
                cnt = ""

                val pair = exchange(exchange, numCourse, count)
                if (pair.getFirst() && pair.getSecond()) {
                    updateCourse()
                    printCourse(1)
                    f1 = false
                    trade = true
                } else {
                    cnt = if (!pair.getFirst())
                        "person"
                    else
                        "terminal"
                }
            }
        }
        if (!f1) continue
        ans = ""
    }
}