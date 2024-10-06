import kotlin.math.floor
import kotlin.math.min
import kotlin.random.Random

// начальные средства пользователя
var capitalUser = mapOf("RUB" to 1000000*100, "USD" to 0, "EUR" to 0, "USDT" to 0, "BTC" to 0)

// начальные средства терминала
var capitalTerminal = mapOf("RUB" to 10000*100, "USD" to 1000*100, "EUR" to 1000*100, "USDT" to 1000*100, "BTC" to 15*1000000)

// курс валютных пар
var course = arrayOf(90*100, 95*100, 11*10, 1*100, 45000*100)

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
            println("${i+1}. ${nameCurrency[i]} = ${capitalUser[nameCurrency[i]]!!.toDouble()/10000000}")
        else
            println("${i+1}. ${nameCurrency[i]} = ${capitalUser[nameCurrency[i]]!!.toDouble()/100}")
    }
}

// печать счета терминала
fun printCapitalTerminal() {
    println("Счет терминала:")
    for (i in 0..4) {
        if (i == 4)
            println("${i+1}. ${nameCurrency[i]} = ${capitalTerminal[nameCurrency[i]]!!.toDouble()/10000000}")
        else
            println("${i+1}. ${nameCurrency[i]} = ${capitalTerminal[nameCurrency[i]]!!.toDouble()/100}")
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
        println("${i+1}. ${pairCourse[i].getFirst()}/${pairCourse[i].getSecond()} = ${course[i].toDouble()/100}")
    }
}

// печать меню
fun printMenu(n: Int) {
    println("""
        МЕНЮ
        1. Для завершения сессии напишите 'выход'
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
            min(capitalTerminal[pairCourse[numCourse-1].getFirst()]!!, capitalUser[pairCourse[numCourse-1].getSecond()]!! * course[numCourse-1])
        } else {
            min(capitalTerminal[pairCourse[numCourse-1].getSecond()]!!, capitalUser[pairCourse[numCourse-1].getFirst()] !!* course[numCourse-1])
        }
    } else {
        -1
    }
}

// сообщение объем обмена
fun printExchange(numCourse: Int, exchange: Int): Int {
    val a = if (numCourse in 1..5) {
        if (exchange == 1) {
            pairCourse[numCourse].getFirst()
        } else {
            pairCourse[numCourse].getSecond()
        }
    } else {
        ""
    }
    val checkCourse = checkCourse(numCourse, exchange)
    if (checkCourse == -1) {
        return -1
    }
    if (numCourse == 5 && exchange == 2) {
        println("Введите сумму от 0.001 $a до $checkCourse $a")
    } else {
        println("Введите сумму от 0.01 $a до $checkCourse $a")
    }
    return 1
}

// совершение обмена валюты
fun exchange(exchange: Int, numCourse: Int, count: Int): Pair<Boolean> {
    var pairBool = Pair(first = false, second = false)

    return pairBool
}

fun main() {

}
























