package ai.koog.prompt.executor.clients.grok

import ai.koog.prompt.executor.clients.LLModelDefinitions
import ai.koog.prompt.llm.LLMCapability
import ai.koog.prompt.llm.LLMProvider
import ai.koog.prompt.llm.LLModel

/**
 *
 */
public object GrokModels : LLModelDefinitions {

    /**
     * Grok-4: general-purpose chat model.
     */
    public val Grok4: LLModel = LLModel(
        provider = LLMProvider.Grok,
        id = "grok-4-0709",
        capabilities = listOf(
            LLMCapability.Completion,
            LLMCapability.Temperature,
            LLMCapability.Tools,
            LLMCapability.ToolChoice,
            LLMCapability.Schema.JSON.Basic,
            LLMCapability.Schema.JSON.Standard,
        ),
        contextLength = 256_000,
    )

    /**
     * Grok-code-fast-1: faster cost-optimized variant.
     */
    public val GrokCodeFast1: LLModel = LLModel(
        provider = LLMProvider.Grok,
        id = "grok-code-fast-1",
        capabilities = listOf(
            LLMCapability.Completion,
            LLMCapability.Temperature,
            LLMCapability.Tools,
            LLMCapability.ToolChoice,
            LLMCapability.Schema.JSON.Basic,
            LLMCapability.Schema.JSON.Standard,
        ),
        contextLength = 256_000,
    )

    /**
     * Grok-code-fast-1: faster cost-optimized variant.
     */
    public val Grok4Fast: LLModel = LLModel(
        provider = LLMProvider.Grok,
        id = "grok-4-fast-non-reasoning",
        capabilities = listOf(
            LLMCapability.Completion,
            LLMCapability.Temperature,
            LLMCapability.Tools,
            LLMCapability.ToolChoice,
            LLMCapability.Schema.JSON.Basic,
            LLMCapability.Schema.JSON.Standard,
        ),
        contextLength = 2_000_000,
    )

    /**
     * Grok-code-fast-1: faster cost-optimized variant.
     */
    public val Grok4FastReasoning: LLModel = LLModel(
        provider = LLMProvider.Grok,
        id = "grok-4-fast-reasoning",
        capabilities = listOf(
            LLMCapability.Completion,
            LLMCapability.Temperature,
            LLMCapability.Tools,
            LLMCapability.ToolChoice,
            LLMCapability.Schema.JSON.Basic,
            LLMCapability.Schema.JSON.Standard,
        ),
        contextLength = 2_000_000,
    )
}
