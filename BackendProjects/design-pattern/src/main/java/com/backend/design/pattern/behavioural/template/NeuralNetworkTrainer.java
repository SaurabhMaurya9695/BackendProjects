package com.backend.design.pattern.behavioural.template;

import com.backend.design.pattern.behavioural.template.Template.ModelTrainer;

public class NeuralNetworkTrainer extends ModelTrainer {

    @Override
    protected void process() {
        System.out.println("[NeuralNetworkTrainer] Processing dataset (normalization, cleaning, etc.)");
    }

    @Override
    protected void train() {
        System.out.println("[NeuralNetworkTrainer] Training neural network...");
    }

    @Override
    protected void load(String path) {
        System.out.println("[NeuralNetworkTrainer] Loading dataset from " + path);
    }

    @Override
    protected void evaluate() {
        System.out.println("[NeuralNetworkTrainer] Evaluating model performance...");
    }
}
