package com.dd.callmonitor.presentation.main.usecases

import android.content.res.Resources
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UpdateViewModelOnServerIdleUseCaseTest {

    private val resources: Resources = mockk()
    private val viewModel = MainViewModel()

    private val underTest = UpdateViewModelOnServerIdleUseCase(resources)

    @Before
    fun before() {
        every { resources.getString(any()) } answers {
            it.invocation.args[0].toString()
        }

        every { resources.getString(any(), any()) } answers {
            invocation.args.joinToString(":")
        }
    }

    // TODO more tests
    @Test
    fun setsViewTypeToServerControls() {
        underTest(viewModel)

        assertEquals(MainViewModel.ViewType.SERVER_CONTROLS, viewModel.viewType.value)
    }
}
