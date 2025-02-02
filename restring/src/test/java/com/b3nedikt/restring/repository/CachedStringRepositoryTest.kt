package com.b3nedikt.restring.repository

import android.os.Build
import androidx.test.core.app.ApplicationProvider
import com.b3nedikt.restring.PluralKeyword
import com.b3nedikt.restring.StringRepository
import junit.framework.TestCase.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class CachedStringRepositoryTest {

    private lateinit var stringRepository: StringRepository

    @Before
    fun setUp() {
        val persistentRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())

        stringRepository = CachedStringRepository(
                cacheRepository = MemoryStringRepository(),
                persistentRepository = persistentRepository
        )
    }

    @Test
    fun shouldSetAndGetLocales() {
        val locales = setOf(Locale.ENGLISH, Locale.FRENCH)

        stringRepository.supportedLocales = locales

        val persistentRepository = SharedPrefStringRepository(ApplicationProvider.getApplicationContext())

        val newRepository = CachedStringRepository(
                cacheRepository = MemoryStringRepository(),
                persistentRepository = persistentRepository
        )

        assertEquals(locales, newRepository.supportedLocales)
    }

    @Test
    fun shouldSetAndGetStringPairs() {
        val locale = Locale.ENGLISH
        val strings = generateStrings(10)

        stringRepository.setStrings(locale, strings)

        assertEquals(strings, stringRepository.getStrings(locale))
    }

    @Test
    fun shouldGetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)
        stringRepository.setStrings(locale, strings)

        for (i in 0 until stringCount) {
            assertEquals(stringRepository.getString(locale, "key$i"), "value$i")
        }
    }

    @Test
    fun shouldSetSingleString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStrings(stringCount)

        stringRepository.setStrings(locale, strings)
        stringRepository.setString(locale, "key5", "aNewValue")

        assertEquals(stringRepository.getString(locale, "key5"), "aNewValue")
    }

    @Test
    fun shouldGetSingleStringArray() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings: Map<String, Array<CharSequence>> = generateStringArrays(stringCount)
        stringRepository.setStringArrays(locale, strings)

        for (i in 0 until stringCount) {
            assertEquals(stringRepository.getStringArray(locale, "key$i")?.contentDeepToString(),
                    strings["key$i"]?.contentDeepToString())
        }
    }

    @Test
    fun shouldSetSingleStringArray() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateStringArrays(stringCount)

        stringRepository.setStringArrays(locale, strings)
        val stringArray: Array<CharSequence> = arrayOf("aNewValue")
        stringRepository.setStringArray(locale, "key5", stringArray)

        Assert.assertEquals(stringRepository.getStringArray(locale, "key5")?.contentDeepToString(),
                stringArray.contentDeepToString())
    }

    @Test
    fun shouldGetSingleQuantityString() {
        val locale = Locale.ENGLISH
        val stringCount = 10

        val strings = generateQuantityStrings(stringCount)
        stringRepository.setQuantityStrings(locale, strings)

        for (i in 0 until stringCount) {
            Assert.assertEquals(stringRepository.getQuantityString(locale, "key$i"), strings["key$i"])
        }
    }

    @Test
    fun shouldSetSingleQuantityString() {
        val locale = Locale.ENGLISH
        val stringCount = 10
        val strings = generateQuantityStrings(stringCount)

        stringRepository.setQuantityStrings(locale, strings)
        val quantityString = mapOf(PluralKeyword.ONE to "aNewValue")
        stringRepository.setQuantityString(locale, "key5", quantityString)

        Assert.assertEquals(stringRepository.getQuantityString(locale, "key5"), quantityString)
    }
}