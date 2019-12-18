package com.b3nedikt.restring.repository

import com.b3nedikt.restring.PluralKeyword
import com.b3nedikt.restring.StringRepository
import java.util.*

class CachedStringRepository(val cacheRepository: StringRepository,
                             val persistentRepository: StringRepository
) : StringRepository {

    override var supportedLocales: Set<Locale> = persistentRepository.supportedLocales
        set(value) {
            field = value
            persistentRepository.supportedLocales = value
            cacheRepository.supportedLocales = value
        }

    init {
        cacheRepository.supportedLocales = persistentRepository.supportedLocales

        supportedLocales.forEach {
            cacheRepository.setStrings(it, persistentRepository.getStrings(it))
        }
        supportedLocales.forEach {
            cacheRepository.setQuantityStrings(it, persistentRepository.getQuantityStrings(it))
        }
        supportedLocales.forEach {
            cacheRepository.setStringArrays(it, persistentRepository.getStringArrays(it))
        }
    }

    override fun setStrings(locale: Locale, strings: Map<String, CharSequence>) {
        cacheRepository.setStrings(locale, strings)
        persistentRepository.setStrings(locale, strings)
        supportedLocales = supportedLocales + locale
    }

    override fun setString(locale: Locale, key: String, value: CharSequence) {
        cacheRepository.setString(locale, key, value)
        persistentRepository.setString(locale, key, value)
        supportedLocales = supportedLocales + locale
    }

    override fun getString(locale: Locale, key: String): CharSequence? =
            cacheRepository.getString(locale, key)

    override fun getStrings(locale: Locale): Map<String, CharSequence> =
            cacheRepository.getStrings(locale)

    override fun getQuantityString(locale: Locale, key: String) =
            cacheRepository.getQuantityString(locale, key)

    override fun setQuantityString(locale: Locale, key: String, quantityString: Map<PluralKeyword,
            CharSequence>) {
        cacheRepository.setQuantityString(locale, key, quantityString)
        persistentRepository.setQuantityString(locale, key, quantityString)
        supportedLocales = supportedLocales + locale
    }

    override fun getQuantityStrings(locale: Locale): Map<String, Map<PluralKeyword, CharSequence>> =
            cacheRepository.getQuantityStrings(locale)

    override fun setQuantityStrings(locale: Locale, quantityStrings: Map<String, Map<PluralKeyword,
            CharSequence>>) {
        cacheRepository.setQuantityStrings(locale, quantityStrings)
        persistentRepository.setQuantityStrings(locale, quantityStrings)
        supportedLocales = supportedLocales + locale
    }

    override fun getStringArray(locale: Locale, key: String): Array<CharSequence>? =
            cacheRepository.getStringArray(locale, key)

    override fun setStringArray(locale: Locale, key: String, stringArray: Array<CharSequence>) {
        cacheRepository.setStringArray(locale, key, stringArray)
        persistentRepository.setStringArray(locale, key, stringArray)
        supportedLocales = supportedLocales + locale
    }

    override fun getStringArrays(locale: Locale): Map<String, Array<CharSequence>> =
            cacheRepository.getStringArrays(locale)

    override fun setStringArrays(locale: Locale, stringArrays: Map<String, Array<CharSequence>>) {
        cacheRepository.setStringArrays(locale, stringArrays)
        persistentRepository.setStringArrays(locale, stringArrays)
        supportedLocales = supportedLocales + locale
    }
}
