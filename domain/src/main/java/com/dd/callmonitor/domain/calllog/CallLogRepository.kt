package com.dd.callmonitor.domain.calllog

import com.dd.callmonitor.domain.util.Either
import kotlinx.coroutines.flow.Flow

/**
 * Because we don't want the `domain` layer to depend on `data` layer, we need this interface to
 * a repository in the data layer. This would be a part of "Interface Adapters" layer in Uncle Bob's
 * architecture.
 */
interface CallLogRepository {

    fun observeCallLog(): Flow<Either<CallLogError, List<CallLogEntry>>>
}
