package com.backend.design.pattern.behavioural.template.Template;

public abstract class ModelTrainer {

    protected abstract void process();

    protected abstract void train();

    protected abstract void load(String path);

    protected abstract void evaluate();

    protected void save() {
        System.out.println("[ModelTrainer] Saving data from ModelTrainer");
    }

    // The Template Method (defines algorithm skeleton)
    public final void templateMethod(String path) {
        load(path);       // Step 1: load data
        process();        // Step 2: process it
        train();          // Step 3: train model
        evaluate();       // Step 4: evaluate
        save();           // Step 5: save model
    }
}
