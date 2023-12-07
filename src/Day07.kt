import kotlin.Comparator


fun main() {
    fun part1(input: List<String>): Int {
        return input.map(::Hand).score()
    }

    fun part2(input: List<String>): Int {
        return input.map { Hand(it, true) }.score()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput).also(::println) == 6440)
    val testInput2 = readInput("Day07_test")
    check(part2(testInput2).also(::println) == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

fun List<Hand>.score(): Int = this.sortedWith(weakToStrongComparator())
    .mapIndexed { index, hand -> hand.bid * (index + 1) }
    .sum()

data class Hand(val cards: List<PlayingCard>, val bid: Int) {
    val handType: HandType = findHandType()

    private fun findHandType(): HandType {
        val jokers = cards.count { it == PlayingCard.JOKER }
        if (jokers == 5) return HandType.FIVE_OF_KIND
        return cards.filterNot { it == PlayingCard.JOKER }
            .groupBy { it.symbol }
            .map { (_, list) -> list.size }
            .let { instancesOfCards ->
                when (instancesOfCards.max() + jokers) {
                    5 -> HandType.FIVE_OF_KIND
                    4 -> HandType.FOUR_OF_KIND
                    3 -> if (instancesOfCards.min() == 2) HandType.FULL_HOUSE else HandType.THREE_OF_KIND
                    2 -> if (instancesOfCards.count { it == 2 } == 2) HandType.TWO_PAIR else HandType.PAIR
                    else -> HandType.HIGH
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
    return Hand(cards.map { fromSymbol(it, useJokers) }, bid.toInt())
}

fun handTypeComparator(): Comparator<Hand> = compareBy { it.handType.ordinal }

fun cardsComparator(): Comparator<Hand> = Comparator { h1, h2 ->
    h1.cards.zip(h2.cards)
        .map { (c1, c2) -> c1.ordinal - c2.ordinal }
        .first { it != 0 }
}

fun weakToStrongComparator(): Comparator<Hand> = handTypeComparator().then(cardsComparator())