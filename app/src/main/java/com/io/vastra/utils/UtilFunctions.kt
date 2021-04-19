package com.io.vastra.utils

inline fun <T:Any> ifLet(vararg elements: T?, closure: (List<T>) -> Unit) {
    if (elements.all { it !== null }) {
        closure(elements.filterNotNull());
    }
}
