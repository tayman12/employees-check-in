package com.virgingates.context

class test {
    val num: Int = 5

    fun inc(num: Int) {
        val num = num

        if (num > 0) {
            val num = 3
            println("num: " + num)

        }

        println("num: " + num)
    }
}

fun main(args: Array<String>) {
    test().inc(4)
}

