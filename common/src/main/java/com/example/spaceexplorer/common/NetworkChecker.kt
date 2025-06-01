package com.example.spaceexplorer.common

import android.content.Context

/**
 * Interface to check network availability.
 * It way make it easier to mock in tests.
 */
interface NetworkChecker {
    fun isNetworkAvailable(context: Context): Boolean
}