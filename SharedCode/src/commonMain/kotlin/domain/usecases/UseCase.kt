import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.kotlin.mpp.mobile.domain.model.Either
import org.kotlin.mpp.mobile.domain.uiDispatcher

abstract class UseCase<out Type, in Params> where Type : Any {

    /**
     * Runs the actual logic of the use case.
     */
    abstract suspend fun run(params: Params): Either<Exception, Type>

    suspend operator fun invoke(params: Params, onSuccess: (Type) -> Unit, onFailure: (Exception) -> Unit) {
        val result = run(params)
        coroutineScope {
            launch(uiDispatcher) {
                result.fold(
                    failed = { onFailure(it) },
                    succeeded = { onSuccess(it) }
                )
            }
        }
    }

    /**
     * Placeholder for a use case that doesn't need any input parameters.
     */
    object None
}