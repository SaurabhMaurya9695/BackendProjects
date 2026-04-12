package com.backend.ai.springai.strategy;

/**
 * Strategy Pattern — defines a common contract for all AI model implementations.
 * Each model (Anthropic, Ollama, OpenAI, etc.) provides its own implementation.
 */
public interface ChatModelStrategy {

    String chat(String query);

    String getModelName();
}
