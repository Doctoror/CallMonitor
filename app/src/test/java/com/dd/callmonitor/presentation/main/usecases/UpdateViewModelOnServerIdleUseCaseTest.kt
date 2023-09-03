package com.dd.callmonitor.presentation.main.usecases

import android.content.res.Resources
import com.dd.callmonitor.presentation.main.MainViewModel
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class UpdateViewModelOnServerIdleUseCaseTest {

    private val resources: Resources = mockk()
    private val viewModel = MainViewModel()

    private val underTest = UpdateViewModelOnServerIdleUseCase(resources)

    // TODO more tests
    @Test
    fun setsViewTypeToServerControls() {
        underTest(viewModel)

        assertEquals(MainViewModel.ViewType.SERVER_CONTROLS, viewModel.viewType.value)
    }
}
