package com.dd.callmonitor.data.server

import kotlinx.coroutines.flow.MutableStateFlow

class ServerStateProvider {

    /**
     * Note for code reviewers:
     *
     * You might argue that it's better to keep MutableStateFlow private and expose only a StateFlow
     * to avoid accidental overrides. However, we would still have to expose a way to emit a value,
     * let's say, by some sort of `setState` function.
     *
     * So, instead of having two fields and a function it's less code if we expose a
     * MutableStateFlow directly. Because anyway, anyone could mutate the value if it has this
     * ServerStateProvider instance.
     *
     * You could also argue that it would be easier to track who emits by looking at the usages of
     * the setter function if we had one. Indeed, however, hot sure if it's worth the extra code.
     *
     * You might also argue that having a getter and setter is a good idea because you could add
     * some verification or other logic here. Indeed you could.
     *
     * And you might also argue that having a getter and setter would mean this state provider could
     * easily replace the logic from, let's say, runtime scope to persistent storage, or could be
     * converted to an interface with something totally else. Yes, it could. However, I would prefer
     * to keep it simple here (KISS).
     */
    val state = MutableStateFlow<ServerState>(ServerState.Idle)
}
