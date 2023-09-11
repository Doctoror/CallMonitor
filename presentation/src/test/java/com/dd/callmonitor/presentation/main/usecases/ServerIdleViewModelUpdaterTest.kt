package com.dd.callmonitor.presentation.main.usecases

import android.content.res.Resources
import com.dd.callmonitor.presentation.main.servercontrol.ServerControlsViewModel
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ServerIdleViewModelUpdaterTest {

    private val resources: Resources = mockk()
    private val viewModel = ServerControlsViewModel()

    private val underTest = ServerIdleViewModelUpdater(resources)

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
    fun setsViewTypeToContent() {
        underTest(viewModel)

        assertEquals(ServerControlsViewModel.ViewType.CONTENT, viewModel.viewType.value)
    }
}
