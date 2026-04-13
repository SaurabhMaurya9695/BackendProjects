package com.backend.ai.springai.advisor;

import reactor.core.publisher.Flux;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;

import java.util.logging.Logger;

public class TokenConsumedAdvisor implements CallAdvisor, StreamAdvisor {

    private Logger _logger = Logger.getLogger(TokenConsumedAdvisor.class.getName());

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        _logger.info("TOKEN CONSUMER CALLED");

        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        _logger.info("Response received from token consumer adviser : " + chatClientResponse);
        return chatClientResponse;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest chatClientRequest,
            StreamAdvisorChain streamAdvisorChain) {
        _logger.info("TOKEN CONSUMER CALLED (stream)");
        return streamAdvisorChain.nextStream(chatClientRequest)
                .doOnComplete(() -> _logger.info("Stream completed"));
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
