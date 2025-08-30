package com.backend.design.pattern.behavioural.template;

import com.backend.design.pattern.behavioural.template.Template.ModelTrainer;

/**
 * The Template Method Design Pattern is a behavioral design pattern
 * that defines the skeleton of an algorithm in a base class (abstract class),
 * but lets subclasses override specific steps of the algorithm
 * without changing its overall structure.
 * <p>
 * In this pattern:
 * <ul>
 *   <li>The <b>template method</b> is defined in the base class and is usually marked as {@code final},
 *       so subclasses cannot alter the algorithm’s structure.</li>
 *   <li>Individual <b>steps</b> of the algorithm are declared as abstract or default methods,
 *       and subclasses provide their specific implementations.</li>
 * </ul>
 *
 * <p><b>Example Use Cases:</b></p>
 * <ul>
 *   <li>Data processing pipelines (load → process → analyze → save)</li>
 *   <li>Machine learning workflows (preprocess → train → evaluate → save model)</li>
 *   <li>Document rendering systems (open → parse → format → print)</li>
 * </ul>
 *
 * <p><b>Benefits:</b></p>
 * <ul>
 *   <li>Encourages code reuse by keeping invariant parts of the algorithm in one place.</li>
 *   <li>Promotes consistency by ensuring the overall process flow is fixed.</li>
 *   <li>Supports flexibility by letting subclasses customize only the steps they need.</li>
 * </ul>
 *
 * <p><b>Analogy:</b>
 * Think of a "template" for cooking a dish: you always follow the same flow
 * (prepare → cook → serve), but each recipe (subclass) provides its own details
 * for each step.</p>
 */

public class Client {

    public static void main(String[] args) {
        ModelTrainer modelTrainer = new NeuralNetworkTrainer();
        modelTrainer.templateMethod("dataset.csv");
        System.out.println("------------------------------------");
        ModelTrainer modelTrainer2 = new NewNetworkTrainer();
        modelTrainer2.templateMethod("dataset1.csv");
    }
}
