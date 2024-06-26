package neuralNetwork;

import java.util.ArrayList;
import java.util.Random;

import objects.Object;
/*
* Neural Network class
 *
 */
public class NeuralNetwork {
    private int input_nodes;
    private int hidden_nodes;
    private int output_nodes;
    private double learning_rate;
    private double[][] weightsIh;
    private double[][] weightsHo;
    private double[] bias_h;
    private double[] bias_o;
    private double mutationRate = 0.5;
    /*
    * Constructor for the Neural Network
     */
    public NeuralNetwork(int input_nodes, int hidden_nodes, int output_nodes, double learning_rate) {
        this.input_nodes = input_nodes;
        this.hidden_nodes = hidden_nodes;
        this.output_nodes = output_nodes;
        this.learning_rate = learning_rate;

        // weights_ih and weights_ho are the weights matrices between the input and hidden layer and the hidden and output layer respectively
        this.weightsIh = new double[hidden_nodes][input_nodes];
        this.weightsHo = new double[output_nodes][hidden_nodes];
        // bias_h and bias_o are the biases for the hidden and output layers respectively
        this.bias_h = new double[hidden_nodes];
        this.bias_o = new double[output_nodes];
        // Initialize weights and biases
        for (int i = 0; i < hidden_nodes; i++) {
            for (int j = 0; j < input_nodes; j++) {
                // Weights between the input and hidden layer are initialized to a random number between -1 and 1
                this.weightsIh[i][j] = Math.random() * 2 - 1;
            }
        }
        for (int i = 0; i < output_nodes; i++) {
            for (int j = 0; j < hidden_nodes; j++) {
                // Weights between the hidden and output layer are initialized to a random number between -1 and 1
                this.weightsHo[i][j] = Math.random() * 2 - 1;
            }
        }
        for (int i = 0; i < hidden_nodes; i++) {
            this.bias_h[i] = Math.random() * 2 - 1;
        }
        for (int i = 0; i < output_nodes; i++) {
            this.bias_o[i] = Math.random() * 2 - 1;
        }
    }

    /*
    * Train the neural network with inputs and targets
    *
     */
    public double[] feedForward(double[] input_array) {
        // Calculate the hidden layer values
        double[] hidden = new double[this.hidden_nodes];
        double[] output = new double[this.output_nodes];
        // Calculate the hidden layer values
        for (int i = 0; i < this.hidden_nodes; i++) {
            double sum = 0;
            for (int j = 0; j < this.input_nodes; j++) {
                // Sum of the weights times the inputs and make sure its between -1 and 1
                sum += this.weightsIh[i][j] * input_array[j];
            }
            sum += bias_h[i];
            // Activation function at each hidden node
            hidden[i] = tanh(sum);
        }
        // Calculate the output layer values
        for (int i = 0; i < this.output_nodes; i++) {
            double sum = 0;
            for (int j = 0; j < this.hidden_nodes; j++) {
                sum += weightsHo[i][j] * hidden[j];
            }
            sum += this.bias_o[i];
            output[i] = tanh(sum);
        }
        return output;
    }


    /**
     * Hyperbolic tangent function
     *
     * @param x
     * @return
     */
    public double tanh(double x) {
        return Math.tanh(x);
    }

    public double[] getWeightsIh() {
        double[] temp = new double[this.hidden_nodes * this.input_nodes];
        int count = 0;
        for (int i = 0; i < this.hidden_nodes; i++) {
            for (int j = 0; j < this.input_nodes; j++) {
                temp[count] = this.weightsIh[i][j];
                count++;
            }
        }
        return temp;
    }

    public NeuralNetwork copy() {
        NeuralNetwork nn = new NeuralNetwork(this.input_nodes, this.hidden_nodes, this.output_nodes, this.learning_rate);
        for (int i = 0; i < this.hidden_nodes; i++) {
            if (this.input_nodes >= 0) System.arraycopy(this.weightsIh[i], 0, nn.weightsIh[i], 0, this.input_nodes);
        }
        for (int i = 0; i < this.output_nodes; i++) {
            if (this.hidden_nodes >= 0) System.arraycopy(this.weightsHo[i], 0, nn.weightsHo[i], 0, this.hidden_nodes);
        }
        if (this.hidden_nodes >= 0) System.arraycopy(this.bias_h, 0, nn.bias_h, 0, this.hidden_nodes);
        if (this.output_nodes >= 0) System.arraycopy(this.bias_o, 0, nn.bias_o, 0, this.output_nodes);
        return nn;
    }

    public NeuralNetwork crossOver(NeuralNetwork partner) {
        // create a new child
        NeuralNetwork child = new NeuralNetwork(this.input_nodes, this.hidden_nodes, this.output_nodes, this.learning_rate);
        // pick a random midpoint in the parent's Hidden layer
        // first half from this and second half from partner
        for (int i = 0; i < this.hidden_nodes; i++) {
            for (int j = 0; j < this.input_nodes; j++) {
                if (Math.random() < 0.5) {
                    child.weightsIh[i][j] = this.weightsIh[i][j];
                } else {
                    child.weightsIh[i][j] = partner.weightsIh[i][j];
                }
            }
        }
        // pick a random midpoint in the parent's output layer
        // first half from this and second half from partner
        for (int i = 0; i < this.output_nodes; i++) {
            for (int j = 0; j < this.hidden_nodes; j++) {
                if (Math.random() < 0.5) {
                    child.weightsHo[i][j] = this.weightsHo[i][j];
                } else {
                    child.weightsHo[i][j] = partner.weightsHo[i][j];
                }
            }
        }
        for (int i = 0; i < this.hidden_nodes; i++) {
            if (Math.random() < 0.5) {
                child.bias_h[i] = this.bias_h[i];
            } else {
                child.bias_h[i] = partner.bias_h[i];
            }
        }
        for (int i = 0; i < this.output_nodes; i++) {
            if (Math.random() < 0.5) {
                child.bias_o[i] = this.bias_o[i];
            } else {
                child.bias_o[i] = partner.bias_o[i];
            }
        }
        return child;
    }

    public void setMutationRate(double rate) {
        this.mutationRate = rate;
    }

    /**
     * Mutate the weights and biases of the Neural Network
     * Ai helped optimize this method
     */
    public void mutate() {
        // Select a random layer to mutate: 0 for weightsIh, 1 for weightsHo, 2 for bias_h, 3 for bias_o
        int layer = new Random().nextInt(4);
        switch (layer) {
            case 0: // Mutate weightsIh
                int i = new Random().nextInt(this.hidden_nodes);
                int j = new Random().nextInt(this.input_nodes);
                if (Math.random() <= mutationRate) {
                    // Mutate the weight by a random value between -1 and 1
                    this.weightsIh[i][j] = Math.random() * 2 - 1;
                }
                break;
            case 1: // Mutate weightsHo
                i = new Random().nextInt(this.output_nodes);
                j = new Random().nextInt(this.hidden_nodes);
                if (Math.random() <= mutationRate) {
                    // Mutate the weight by a random value between -1 and 1
                    this.weightsHo[i][j] = Math.random() * 2 - 1;
                }
                break;
            case 2: // Mutate bias_h
                i = new Random().nextInt(this.hidden_nodes);
                if (Math.random() <= mutationRate) {
                    // Mutate the bias by a random value between -1 and 1
                    this.bias_h[i] = Math.random() * 2 - 1;
                }
                break;
            case 3: // Mutate bias_o
                i = new Random().nextInt(this.output_nodes);
                if (Math.random() <= mutationRate) {
                    // Mutate the bias by a random value between -1 and 1
                    this.bias_o[i] = Math.random() * 2 - 1;
                }
                break;
        }
    }


    private double[][] setWeightsIh(ArrayList<Object> searchField, Object currentPosition) {
        // weights are set to be an Euler distance between the two points starting from the current position
        double[][] weights_ih = new double[hidden_nodes][input_nodes];
        for (int i = 0; i < hidden_nodes; i++) {
            for (int j = 0; j < input_nodes; j++) {
                weights_ih[i][j] = searchField.get(i).distance(currentPosition);
            }
        }
        return weights_ih;
    }

    public void crossOver(NeuralNetwork parentA, NeuralNetwork parentB) {
    // Create a new child NeuralNetwork
    NeuralNetwork child = new NeuralNetwork(this.input_nodes, this.hidden_nodes, this.output_nodes, this.learning_rate);
    double random = Math.random() < 0.5?0:1;
    // Perform crossover on the weights and biases
    for (int i = 0; i < this.hidden_nodes; i++) {
        for (int j = 0; j < this.input_nodes; j++) {
            if (random < 0.5) {
                child.weightsIh[i][j] = parentA.weightsIh[i][j];
            } else {
                child.weightsIh[i][j] = parentB.weightsIh[i][j];
            }
        }
    }
    for (int i = 0; i < this.output_nodes; i++) {
        for (int j = 0; j < this.hidden_nodes; j++) {
            if (random < 0.5) {
                child.weightsHo[i][j] = parentA.weightsHo[i][j];
            } else {
                child.weightsHo[i][j] = parentB.weightsHo[i][j];
            }
        }
    }
    for (int i = 0; i < this.hidden_nodes; i++) {
        if (random < 0.5) {
            child.bias_h[i] = parentA.bias_h[i];
        } else {
            child.bias_h[i] = parentB.bias_h[i];
        }
    }
    for (int i = 0; i < this.output_nodes; i++) {
        if (random < 0.5) {
            child.bias_o[i] = parentA.bias_o[i];
        } else {
            child.bias_o[i] = parentB.bias_o[i];
        }
    }
    this.weightsIh = child.weightsIh;
    this.weightsHo = child.weightsHo;
    this.bias_h = child.bias_h;
    this.bias_o = child.bias_o;
    }
}