fun main(args: Array<String>) {
    println(wrapString("Hello World!"))
    println(wrapStringWithTypeCheck("Hello World!"))

    buildString {
        println(wrapStringBuilder(this))
        println(wrapStringBuilderWithTypeCheck(this))
    }
}

fun wrapString(impl: String) = ifaceHandler(impl)

fun wrapStringBuilder(impl: CharSequence) = ifaceHandler(impl)

fun wrapStringWithTypeCheck(impl: String) = assertingIfaceHandler<String>(impl)

fun wrapStringBuilderWithTypeCheck(impl: CharSequence) = assertingIfaceHandler<CharSequence>(impl)

inline fun ifaceHandler(seq: CharSequence): Int {
    var sum = 0
    for (idx in 0 ..< seq.length) {
        sum += seq[idx].code
    }
    return sum
}

@Suppress("USELESS_IS_CHECK")
inline fun <reified T: CharSequence> assertingIfaceHandler(seq: T): Int {
    seq is T
    var sum = 0
    for (idx in 0 ..< seq.length) {
        sum += seq[idx].code
    }
    return sum
}
