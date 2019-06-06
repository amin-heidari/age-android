package com.aminheidari.age.utils

/**
 * Compares versions.
 * https://stackoverflow.com/a/18699668
 * @param other The version to compare against.
 * @return 0 if they are the same, 1 if other is lower than this, and -1 if other is higher than this.
 */
fun String.compareVersionTo(other: String): Int {
    var res = 0

    val oldNumbers = this.split("\\.".toRegex()).dropLastWhile{ it.isEmpty() }.toTypedArray()
    val newNumbers = other.split("\\.".toRegex()).dropLastWhile{ it.isEmpty() }.toTypedArray()

    // To avoid IndexOutOfBounds
    val maxIndex = Math.min(oldNumbers.size, newNumbers.size)

    for (i in 0 until maxIndex) {
        // Any non-integer in the version name will stop the comparision process.
        if (!oldNumbers[i].matches("-?\\d+".toRegex()) || !newNumbers[i].matches("-?\\d+".toRegex())) {
            break
        }

        val oldVersionPart = Integer.valueOf(oldNumbers[i])
        val newVersionPart = Integer.valueOf(newNumbers[i])

        if (oldVersionPart < newVersionPart) {
            res = -1
            break
        } else if (oldVersionPart > newVersionPart) {
            res = 1
            break
        }
    }

    // If versions are the same so far, but they have different length...
    if (res == 0 && oldNumbers.size != newNumbers.size) {
        res = if (oldNumbers.size > newNumbers.size) 1 else -1
    }

    return res
}