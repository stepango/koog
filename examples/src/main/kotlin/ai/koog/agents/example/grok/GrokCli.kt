package ai.koog.agents.example.grok

import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.grok.GrokLLMClient
import ai.koog.prompt.executor.clients.grok.GrokModels
import ai.koog.prompt.message.Message

fun main(args: Array<String>) {
    val apiKey = System.getenv("GROK_API_KEY")
        ?: System.getenv("koog.grok.apikey")
        ?: run {
            System.err.println("Missing GROK_API_KEY. Set GROK_API_KEY in your environment or env.properties (used by examples run tasks).")
            return
        }

    val client = GrokLLMClient(apiKey)
    val model = GrokModels.Grok4Fast // grok-4-fast-non-reasoning

    val question = if (args.isNotEmpty()) args.joinToString(" ") else "Give me a two-sentence summary of the benefits of Grok models."

    val p = prompt("grok-cli") {
        system("You are a concise assistant.")
        user(question)
    }

    runCatching {
        val responses: List<Message.Response> = kotlinx.coroutines.runBlocking {
            client.execute(p, model, emptyList())
        }
        val text = responses.joinToString("\n") { r ->
            when (r) {
                is Message.Assistant -> r.content
                is Message.Tool.Call -> "[tool:${r.tool}] ${r.content}"
                is Message.Tool.Result -> r.content
            }
        }
        println(text)
    }.onFailure { e ->
        e.printStackTrace()
    }
}