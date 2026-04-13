package com.backend.ai.springai;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * Test to save embeddings into MariaDB vector store.
 * Run this test to populate the DB — embeddings are NOT saved during chat calls.
 */
@SpringBootTest
public class VectorStoreTest {

    @Autowired
    private VectorStore vectorStore;

    @Test
    void saveEmbeddings() {
        List<Document> documents = List.of(
                new Document("Spring AI is a framework for building AI-powered applications using Spring Boot."),
                new Document("Ollama allows running large language models locally on your machine."), new Document(
                        "MariaDB vector store supports storing and querying vector embeddings for semantic search."),
                new Document("RAG (Retrieval Augmented Generation) combines vector search with LLM responses."));

        vectorStore.add(documents);

        System.out.println("Saved " + documents.size() + " embeddings to MariaDB.");
    }
}
