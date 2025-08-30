package com.backend.design.pattern.behavioural.template;

import com.backend.design.pattern.behavioural.template.Template.ModelTrainer;

public class NewNetworkTrainer extends ModelTrainer {

    @Override
    protected void load(String path) {
        System.out.println("[NewNetworkTrainer] Loading dataset from " + path);
    }

    @Override
    protected void evaluate() {
        System.out.println("[NewNetworkTrainer] Evaluating model performance...");
    }

    @Override
    protected void process() {
        System.out.println("[NewNetworkTrainer] Processing dataset (normalization, cleaning, etc.)");
    }

    @Override
    protected void train() {
        System.out.println("[NewNetworkTrainer] Training neural network...");
    }
}
