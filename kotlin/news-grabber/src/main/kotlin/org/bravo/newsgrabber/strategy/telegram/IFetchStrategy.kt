package org.bravo.newsgrabber.strategy.telegram

interface IFetchStrategy<in T, out R> {
    fun fetch(from: T): R
}
