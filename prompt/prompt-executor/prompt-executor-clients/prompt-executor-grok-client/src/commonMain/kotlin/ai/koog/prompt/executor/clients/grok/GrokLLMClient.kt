package ai.koog.prompt.executor.clients.grok

import ai.koog.prompt.dsl.ModerationResult
import ai.koog.prompt.dsl.Prompt
import ai.koog.prompt.executor.clients.ConnectionTimeoutConfig
import ai.koog.prompt.executor.clients.LLMClient
import ai.koog.prompt.executor.clients.grok.models.GrokChatCompletionRequest
import ai.koog.prompt.executor.clients.grok.models.GrokChatCompletionResponse
import ai.koog.prompt.executor.clients.grok.models.GrokChatCompletionStreamResponse
import ai.koog.prompt.executor.clients.openai.base.AbstractOpenAILLMClient
import ai.koog.prompt.executor.clients.openai.base.OpenAIBasedSettings
import ai.koog.prompt.executor.clients.openai.base.models.OpenAIMessage
import ai.koog.prompt.executor.clients.openai.base.models.OpenAITool
import ai.koog.prompt.executor.clients.openai.base.models.OpenAIToolChoice
import ai.koog.prompt.executor.model.LLMChoice
import ai.koog.prompt.llm.LLModel
import ai.koog.prompt.params.LLMParams
import ai.koog.prompt.streaming.StreamFrameFlowBuilder
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import kotlinx.datetime.Clock

/**
 * Configuration settings for connecting to the xAI Grok API.
 *
 * @property baseUrl The base URL of the Grok API. Default is "https://api.x.ai".
 * @property chatCompletionsPath The chat completions endpoint path. Default is "v1/chat/completions".
 * @property timeoutConfig Configuration for connection timeouts including request, connection, and socket timeouts.
 */
public class GrokClientSettings(
    baseUrl: String = "https://api.x.ai",
    chatCompletionsPath: String = "v1/chat/completions",
    timeoutConfig: ConnectionTimeoutConfig = ConnectionTimeoutConfig()
) : OpenAIBasedSettings(baseUrl, chatCompletionsPath, timeoutConfig)

/**
 * Implementation of [LLMClient] for xAI Grok API.
 *
 * @param apiKey The API key for the Grok API
 * @param settings The base URL, chat completion path, and timeouts for the Grok API
 * @param clock Clock instance used for tracking response metadata timestamps.
 */
public class GrokLLMClient(
    apiKey: String,
    private val settings: GrokClientSettings = GrokClientSettings(),
    baseClient: HttpClient = HttpClient(),
    clock: Clock = Clock.System
) : AbstractOpenAILLMClient<GrokChatCompletionResponse, GrokChatCompletionStreamResponse>(
    apiKey,
    settings,
    baseClient,
    clock,
    staticLogger
) {

    private companion object {
        private val staticLogger = KotlinLogging.logger { }
    }

    override fun serializeProviderChatRequest(
        messages: List<OpenAIMessage>,
        model: LLModel,
        tools: List<OpenAITool>?,
        toolChoice: OpenAIToolChoice?,
        params: LLMParams,
        stream: Boolean
    ): String {
        val grokParams = params.toGrokParams()
        val responseFormat = createResponseFormat(params.schema, model)

        val request = GrokChatCompletionRequest(
            messages = messages,
            model = model.id,
            frequencyPenalty = grokParams.frequencyPenalty,
            logprobs = grokParams.logprobs,
            maxTokens = grokParams.maxTokens,
            presencePenalty = grokParams.presencePenalty,
            responseFormat = responseFormat,
            stop = grokParams.stop,
            stream = stream,
            temperature = grokParams.temperature,
            toolChoice = grokParams.toolChoice?.toOpenAIToolChoice(),
            tools = tools,
            topLogprobs = grokParams.topLogprobs,
            topP = grokParams.topP,
        )

        return json.encodeToString(request)
    }

    override fun processProviderChatResponse(response: GrokChatCompletionResponse): List<LLMChoice> {
        require(response.choices.isNotEmpty()) { "Empty choices in response" }
        return response.choices.map { it.toMessageResponses(createMetaInfo(response.usage)) }
    }

    override fun decodeStreamingResponse(data: String): GrokChatCompletionStreamResponse =
        json.decodeFromString(data)

    override fun decodeResponse(data: String): GrokChatCompletionResponse =
        json.decodeFromString(data)

    override suspend fun StreamFrameFlowBuilder.processStreamingChunk(chunk: GrokChatCompletionStreamResponse) {
        chunk.choices.firstOrNull()?.let { choice ->
            choice.delta.content?.let { emitAppend(it) }
            choice.delta.toolCalls?.forEach { toolCall ->
                val index = toolCall.index
                val id = toolCall.id
                val name = toolCall.function?.name
                val arguments = toolCall.function?.arguments
                upsertToolCall(index, id, name, arguments)
            }
            choice.finishReason?.let { emitEnd(it, createMetaInfo(chunk.usage)) }
        }
    }

    public override suspend fun moderate(prompt: Prompt, model: LLModel): ModerationResult {
        logger.warn { "Moderation is not supported by Grok API" }
        throw UnsupportedOperationException("Moderation is not supported by Grok API.")
    }
}