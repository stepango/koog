package ai.koog.prompt.executor.clients.grok

import ai.koog.prompt.params.LLMParams

internal fun LLMParams.toGrokParams(): GrokParams {
    if (this is GrokParams) return this
    return GrokParams(
        temperature = temperature,
        maxTokens = maxTokens,
        numberOfChoices = numberOfChoices,
        speculation = speculation,
        schema = schema,
        toolChoice = toolChoice,
        user = user,
        includeThoughts = includeThoughts,
    )
}

/**
 * Grok chat-completions parameters layered on top of [LLMParams].
 */
public class GrokParams(
    temperature: Double? = null,
    maxTokens: Int? = null,
    numberOfChoices: Int? = null,
    speculation: String? = null,
    schema: Schema? = null,
    toolChoice: ToolChoice? = null,
    user: String? = null,
    includeThoughts: Boolean? = null,
    thinkingBudget: Int? = null,
    public val frequencyPenalty: Double? = null,
    public val presencePenalty: Double? = null,
    public val logprobs: Boolean? = null,
    public val stop: List<String>? = null,
    public val topLogprobs: Int? = null,
    public val topP: Double? = null,
) : LLMParams(
    temperature, maxTokens, numberOfChoices,
    speculation, schema, toolChoice,
    user, includeThoughts, thinkingBudget
) {
    init {
        require(topP == null || topP in 0.0..1.0) {
            "topP must be in (0.0, 1.0], but was $topP"
        }
        if (topLogprobs != null) {
            require(logprobs == true) { "`topLogprobs` requires `logprobs=true`." }
            require(topLogprobs in 0..20) { "topLogprobs must be in [0, 20], but was $topLogprobs" }
        }
        require(frequencyPenalty == null || frequencyPenalty in -2.0..2.0) {
            "frequencyPenalty must be in [-2.0, 2.0], but was $frequencyPenalty"
        }
        require(presencePenalty == null || presencePenalty in -2.0..2.0) {
            "presencePenalty must be in [-2.0, 2.0], but was $presencePenalty"
        }
        if (stop != null) {
            require(stop.isNotEmpty()) { "stop must not be empty when provided." }
            require(stop.size <= 4) { "stop supports at most 4 sequences, but was ${stop.size}" }
            require(stop.all { it.isNotBlank() }) { "stop sequences must not be blank." }
        }
    }
}