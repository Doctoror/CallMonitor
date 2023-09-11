package com.dd.callmonitor.presentation.calllog

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Note for reviewers:
 *
 * Since I am using MVPVM (AKA MVVMP), the actual ViewModel does not have business logic and thus it
 * does not need to extend from [androidx.lifecycle.ViewModel].
 */
class CallLogViewModel {

    /**
     * Note for reviewers:
     *
     * You might argue that it's better to keep MutableStateFlow private and expose only a StateFlow
     * to avoid accidental overrides. However, we would still have to expose a way to emit a value,
     * let's say, by some sort of `setCallLog` function.
     *
     * So, instead of having two fields and a function it's less code if we expose a
     * MutableStateFlow directly. Because anyway, anyone could mutate the value if it has this
     * ViewModel instance.
     *
     * You could also argue that it would be easier to track who emits by looking at the usages of
     * the setter function if we had one. Indeed, however, given the limited scope of the view model
     * there will not be many usages of its fields so it would be easy to find the usages anyway.
     */
    val callLog = MutableStateFlow(emptyList<CallLogEntryViewModel>())
    val viewType = MutableStateFlow(ViewType.LOADING)

    enum class ViewType {

        LOADING,
        CONTENT,
        FAILURE
    }
}
