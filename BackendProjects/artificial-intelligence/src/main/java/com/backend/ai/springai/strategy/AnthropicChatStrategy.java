package com.backend.ai.springai.strategy;

import com.backend.ai.springai.advisor.TokenConsumedAdvisor;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Strategy implementation for Anthropic Claude.
 * Handles all chat interactions routed to the Claude model.
 */
@Component
public class AnthropicChatStrategy implements ChatModelStrategy {

    private final ChatClient chatClient;

    public AnthropicChatStrategy(AnthropicChatModel anthropicChatModel) {
        this.chatClient = ChatClient.builder(anthropicChatModel).build();
    }

    @Override
    public String chat(String query) {
        /*
         * TO GET THE META DETA, USE THIS
         * var resp =  chatClient.prompt(query).call().chatResponse().getMetadata();
         * System.out.println(resp);
         * return "OKAY";
         * */

        /*
         * There is one method by which you can get your response in Entity way
         * */

        /* WITHOUT QUERY PARAM
        Prompt prompt = new Prompt(query);
        return chatClient
                .prompt(prompt)
                .call()
                .content();
         */

        /*
         * WITH QUERY PARAM
         *
        String queryStr =
                "AS an expert in coding and programming. Always write a program in java. now reply for this question "
                        + ": {query}";
        return chatClient
                .prompt()
                .user(u -> u.text(queryStr).param("query",query))
                .call()
                .content();
         */

        // USING Prompt Template
        PromptTemplate promptTemplate = PromptTemplate.builder().template(
                "What is {techName} ? tell me with an {exampleName}").build();
        String renderMsg = promptTemplate.render(Map.of("techName", "SPRING", "exampleName", "SPRING BOOT"));
        Prompt prompt = new Prompt(renderMsg);
        return this.chatClient
                .prompt(prompt)
                .advisors(
                        new TokenConsumedAdvisor(), // custom advisor
                        new SimpleLoggerAdvisor(), // It will prints you req and repose
                        new SafeGuardAdvisor(new ArrayList<>(Collections.singleton("JAVA")))) // whatever the word inside this list will not be allowed by user
                .call()
                .content();
    }

    @Override
    public String getModelName() {
        return "claude";
    }
}
