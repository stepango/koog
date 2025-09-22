package ai.koog.prompt.executor.clients.grok.models

import ai.koog.prompt.executor.clients.openai.base.models.OpenAIBaseLLMRequest
import ai.koog.prompt.executor.clients.openai.base.models.OpenAIBaseLLMResponse
import ai.koog.prompt.executor.clients.openai.base.models.OpenAIBaseLLMStreamResponse
import ai.koog.prompt.executor.clients.openai.base.models.OpenAIChoice
import ai.koog.prompt.executor.clients.openai.base.models.OpenAIMessage
import ai.koog.prompt.executor.clients.openai.base.models.OpenAIResponseFormat
import ai.koog.prompt.executor.clients.openai.base.models.OpenAIStreamChoice
import ai.koog.prompt.executor.clients.openai.base.models.OpenAITool
import ai.koog.prompt.executor.clients.openai.base.models.OpenAIToolChoice
import ai.koog.prompt.executor.clients.openai.base.models.OpenAIUsage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Grok (xAI) Chat Completions API Request
 */
@Serializable
internal class GrokChatCompletionRequest(
    val messages: List<OpenAIMessage>,
    override val model: String,
    override val stream: Boolean? = null,
    override val temperature: Double? = null,
    val tools: List<OpenAITool>? = null,
    val toolChoice: OpenAIToolChoice? = null,
    override val topP: Double? = null,
    override val topLogprobs: Int? = null,
    val maxTokens: Int? = null,
    val frequencyPenalty: Double? = null,
    val presencePenalty: Double? = null,
    val responseFormat: OpenAIResponseFormat? = null,
    val stop: List<String>? = null,
    val logprobs: Boolean? = null,
) : OpenAIBaseLLMRequest

/**
 * Grok Chat Completion Response
 */
@Serializable
public class GrokChatCompletionResponse(
    public val choices: List<OpenAIChoice>,
    override val created: Long,
    override val id: String,
    override val model: String,
    public val systemFingerprint: String? = null,
    @SerialName("object")
    public val objectType: String = "chat.completion",
    public val usage: OpenAIUsage? = null,
) : OpenAIBaseLLMResponse

/**
 * Grok Chat Completion Streaming Response
 */
@Serializable
public class GrokChatCompletionStreamResponse(
    public val choices: List<OpenAIStreamChoice>,
    override val created: Long,
    override val id: String,
    override val model: String,
    public val systemFingerprint: String? = null,
    @SerialName("object")
    public val objectType: String = "chat.completion.chunk",
    public val usage: OpenAIUsage? = null,
) : OpenAIBaseLLMStreamResponse