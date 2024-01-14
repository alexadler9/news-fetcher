package ru.alexadler9.newsfetcher.utility.junit5

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

/**
 * Implements TestDispatchers when using coroutines. TestDispatchers are CoroutineDispatcher implementations for testing purposes.
 * It is necessary to use TestDispatchers if new coroutines are created during the test (or coroutines are used inside test objects)
 * being tested to make the execution of the new coroutines predictable.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CoroutinesTestExtension(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : BeforeEachCallback, AfterEachCallback {

    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(dispatcher)
    }

    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}