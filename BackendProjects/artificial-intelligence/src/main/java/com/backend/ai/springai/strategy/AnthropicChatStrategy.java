package com.backend.ai.springai.strategy;

import reactor.core.publisher.Flux;
import com.backend.ai.springai.advisor.TokenConsumedAdvisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Strategy implementation for Anthropic Claude.
 * Handles all chat interactions routed to the Claude model.
 */
@Component
public class AnthropicChatStrategy implements ChatModelStrategy {

    private final ChatClient chatClient;
//    private final MessageChatMemoryAdvisor memoryAdvisor;

    private final VectorStore _vectorStore;

    private Logger _logger = LoggerFactory.getLogger(AnthropicChatStrategy.class);

    @Value("classpath:prompt/system-message.st")
    private Resource _system_message;

    @Value("classpath:prompt/user-message.st")
    private Resource _user_message;

//    public AnthropicChatStrategy(AnthropicChatModel anthropicChatModel, JdbcChatMemoryRepository jdbcChatMemoryRepository) {
//        this.chatClient = ChatClient.builder(anthropicChatModel).build();
//        this.memoryAdvisor = MessageChatMemoryAdvisor.builder(
//                MessageWindowChatMemory.builder().chatMemoryRepository(jdbcChatMemoryRepository).build()).build();
//    }

    public AnthropicChatStrategy(AnthropicChatModel anthropicChatModel, VectorStore vectorStore) {
        this.chatClient = ChatClient.builder(anthropicChatModel).build();
        this._vectorStore = vectorStore;
    }

    @Override
    public String chat(String query, String conversationId) {
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

        /*
            // USING Prompt Template
            PromptTemplate promptTemplate = PromptTemplate.builder().template(
                    "What is {techName} ? tell me with an {exampleName}").build();
            String renderMsg = promptTemplate.render(Map.of("techName", "SPRING", "exampleName", "SPRING BOOT"));
            Prompt prompt = new Prompt(renderMsg);
         */

        /* USING MEMORY ADVISOR - FOR MORE COMMENT ABOVE CODE
        return this.chatClient
                .prompt(query)
                .advisors(
                        memoryAdvisor,
                        new TokenConsumedAdvisor(), // custom advisor
                        new SimpleLoggerAdvisor(), // It will prints you req and repose
                        new SafeGuardAdvisor(new ArrayList<>(Collections.singleton("JAVA")))) // whatever the word inside this list will not be allowed by user
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .content();
         */

        /*
        //USING RAG
        // 1: get the smilier data from db
        SearchRequest searchRequest = SearchRequest.builder()
                .topK(1)
                .query(query)
                .similarityThreshold(0.5)
                .build();

        List<Document> documents = this._vectorStore.similaritySearch(searchRequest);

        // retreive String/text from document list
        assert documents != null;
        List<String> documentsList = documents.stream().map(Document::getText).toList();
        String contextData = String.join(",", documentsList);

        _logger.info("prompt : {}", contextData);
        return this.chatClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor())
                .system(
                        promptSystemSpec -> promptSystemSpec.text(this._system_message).param("documents", contextData))
                .user(promptUserSpec -> promptUserSpec.text(this._user_message).param("query",query))
                .call()
                .content();
         */

        // USING RAG ADVISOR
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(
                        VectorStoreDocumentRetriever.builder()
                        .topK(3)
                        .vectorStore(_vectorStore)
                        .similarityThreshold(0.5)
                        .build())
                .build();
        _logger.info("retrievalAugmentationAdvisor : {}", retrievalAugmentationAdvisor);
        return this.chatClient
                .prompt()
                .advisors(retrievalAugmentationAdvisor, new SimpleLoggerAdvisor())
                .user(promptUserSpec -> promptUserSpec.text(this._user_message).param("query",query))
                .call()
                .content();
    }

    @Override
    public String getModelName() {
        return "claude";
    }

    @Override
    public Flux<String> streamChat(String query, String conversationId) {
        return this.chatClient
                .prompt()
                .system(system -> system.text(this._system_message))
                .user(user -> user.text(this._user_message).param("concepts", query))
//                .advisors(memoryAdvisor)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();

    }
}
