fun main() {
    fun part1(input: List<String>): Int {
        return input.map(::Hand)
            .sortedWith(weakToStrongComparator())
//            .also(::println)
            .mapIndexed { index, hand -> hand.bid * (index + 1) }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input.map { Hand(it, true) }
            .sortedWith(weakToStrongComparator())
            .onEach { hand -> println("$hand ${hand.handType}") }
            .mapIndexed { index, hand -> hand.bid * (index + 1) }
            .also(::println)
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    val testInput2 = readInput("Day07_test")
    check(part2(testInput2).also(::println) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

data class Hand(val cards: List<PlayingCard>, val bid: Int, val useJokers: Boolean = false) {
    val handType: HandType = findHandType(useJokers)

    fun findHandType(useJokers: Boolean): HandType {
        return if (useJokers) {
            val jokers = cards.count { it == PlayingCard.JOKER }
            if (jokers == 5) return HandType.FIVE_OF_KIND
            cards.filterNot { it == PlayingCard.JOKER }
                .groupBy { it.symbol }
                .map { (_, list) -> list.size }
//                .also(::println)
                .let { instancesOfCards ->
                    when (instancesOfCards.max() + jokers) {
                        5 -> HandType.FIVE_OF_KIND
                        4 -> HandType.FOUR_OF_KIND
                        3 -> {
                            if ((instancesOfCards.contains(3) && instancesOfCards.contains(2))
                                || (instancesOfCards.count { it == 2 } == 2 && jokers == 1)
                            ) {
                                HandType.FULL_HOUSE
                            } else HandType.THREE_OF_KIND
                        }

                        2 -> {
                            if (instancesOfCards.count { it == 2 } == 2) HandType.TWO_PAIR else HandType.PAIR
                        }

                        else -> HandType.HIGH
                    }

                }
        } else {
            cards.groupBy { it.symbol }
                .map { (_, list) -> list.size }
//                .also(::println)
                .let { instancesOfCards ->
                    when (instancesOfCards.max()) {
                        5 -> HandType.FIVE_OF_KIND
                        4 -> HandType.FOUR_OF_KIND
                        3 -> {
                            if (instancesOfCards.contains(2)) HandType.FULL_HOUSE else HandType.THREE_OF_KIND
                        }

                        2 -> {
                            if (instancesOfCards.count { it == 2 } == 2) HandType.TWO_PAIR else HandType.PAIR
                        }

                        else -> HandType.HIGH
                    }
                }
        }
    }
}

enum class HandType {
    HIGH,
    PAIR,
    TWO_PAIR,
    THREE_OF_KIND,
    FULL_HOUSE,
    FOUR_OF_KIND,
    FIVE_OF_KIND
}

enum class PlayingCard(val symbol: Char) {
    JOKER('J'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
    TEN('T'),
    JACK('J'),
    QUEEN('Q'),
    KING('K'),
    ACE('A');
}

fun fromSymbol(symbol: Char, useJokers: Boolean = false): PlayingCard = when (symbol) {
    '2' -> PlayingCard.TWO
    '3' -> PlayingCard.THREE
    '4' -> PlayingCard.FOUR
    '5' -> PlayingCard.FIVE
    '6' -> PlayingCard.SIX
    '7' -> PlayingCard.SEVEN
    '8' -> PlayingCard.EIGHT
    '9' -> PlayingCard.NINE
    'T' -> PlayingCard.TEN
    'J' -> if (useJokers) PlayingCard.JOKER else PlayingCard.JACK
    'Q' -> PlayingCard.QUEEN
    'K' -> PlayingCard.KING
    'A' -> PlayingCard.ACE
    else -> error("Bad Symbol")
}

fun Hand(line: String, useJokers: Boolean = false): Hand {
    val (cards, bid) = line.split(" ")
    return Hand(cards.map { fromSymbol(it, useJokers) }, bid.toInt(), useJokers)
}

fun weakToStrongComparator(): Comparator<Hand> {
    return java.util.Comparator { h1, h2 ->
        if (h1.handType.ordinal > h2.handType.ordinal) {
            1
        } else if (h1.handType.ordinal < h2.handType.ordinal) {
            -1
        } else {
            h1.cards.zip(h2.cards)
                .map { (c1, c2) -> c1.ordinal - c2.ordinal }
                .first { it != 0 }
        }
    }
}