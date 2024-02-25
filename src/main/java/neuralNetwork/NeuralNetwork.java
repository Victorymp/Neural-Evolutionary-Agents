package neuralNetwork;

public class NeuralNetwork {
    private int input_nodes;
    private int hidden_nodes;
    private int output_nodes;
    private double learning_rate;
    private double[][] weights_ih;
    private double[][] weights_ho;
    private double[] bias_h;
    private double[] bias_o;

    public NeuralNetwork(int input_nodes, int hidden_nodes, int output_nodes, double learning_rate) {
        this.input_nodes = input_nodes;
        this.hidden_nodes = hidden_nodes;
        this.output_nodes = output_nodes;
        this.learning_rate = learning_rate;

        // weights_ih and weights_ho are the weights matrices between the input and hidden layer and the hidden and output layer respectively
        this.weights_ih = new double[hidden_nodes][input_nodes];
        this.weights_ho = new double[output_nodes][hidden_nodes];
        // bias_h and bias_o are the biases for the hidden and output layers respectively
        this.bias_h = new double[hidden_nodes];
        this.bias_o = new double[output_nodes];
        // Initialize weights and biases
        for (int i = 0; i < hidden_nodes; i++) {
            for (int j = 0; j < input_nodes; j++) {
                // Weights between the input and hidden layer are initialized to a random number between -1 and 1
                this.weights_ih[i][j] = Math.random() * 2 - 1;
            }
        }
        for (int i = 0; i < output_nodes; i++) {
            for (int j = 0; j < hidden_nodes; j++) {
                // Weights between the hidden and output layer are initialized to a random number between -1 and 1
                this.weights_ho[i][j] = Math.random() * 2 - 1;
            }
        }
        for (int i = 0; i < hidden_nodes; i++) {
            this.bias_h[i] = Math.random() * 2 - 1;
        }
        for (int i = 0; i < output_nodes; i++) {
            this.bias_o[i] = Math.random() * 2 - 1;
        }
    }

    public double[] feedForward(double[] input_array,double I) {
        // Calculate the hidden layer values
        double[] hidden = new double[this.hidden_nodes];
        double[] output = new double[this.output_nodes];
        double A = learning_rate;


        // Calculate the hidden layer values
        for (int i = 0; i < this.hidden_nodes; i++) {
            double sum = 0;
            for (int j = 0; j < this.input_nodes; j++) {
                // Sum of the weights times the inputs
                sum += this.weights_ih[i][j] * Math.max(0,input_array[j]) ;
            }
            sum += this.bias_h[i];
            // Activation function at each hidden node
            hidden[i] = tanh(-A+hidden[i]+I+sum);
        }

        for (int i = 0; i < this.output_nodes; i++) {
            double sum = 0;
            for (int j = 0; j < this.hidden_nodes; j++) {
                sum += this.weights_ho[i][j] * hidden[j];
            }
            sum += this.bias_o[i];
            output[i] = tanh((-A+I+sum));
        }
        return output;
    }
    public double sigmoid(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    /**
     * Hyperbolic tangent function
     * @param x
     * @return
     */
    public double tanh(double x) {
        return Math.tanh(x);
    }
    public double[] getWeights_ih() {
        double[] temp = new double[this.hidden_nodes * this.input_nodes];
        int count = 0;
        for (int i = 0; i < this.hidden_nodes; i++) {
            for (int j = 0; j < this.input_nodes; j++) {
                temp[count] = this.weights_ih[i][j];
                count++;
            }
        }
        return temp;
    }

    public NeuralNetwork copy() {
        NeuralNetwork nn = new NeuralNetwork(this.input_nodes, this.hidden_nodes, this.output_nodes, this.learning_rate);
        for (int i = 0; i < this.hidden_nodes; i++) {
            for (int j = 0; j < this.input_nodes; j++) {
                nn.weights_ih[i][j] = this.weights_ih[i][j];
            }
        }
        for (int i = 0; i < this.output_nodes; i++) {
            for (int j = 0; j < this.hidden_nodes; j++) {
                nn.weights_ho[i][j] = this.weights_ho[i][j];
            }
        }
        for (int i = 0; i < this.hidden_nodes; i++) {
            nn.bias_h[i] = this.bias_h[i];
        }
        for (int i = 0; i < this.output_nodes; i++) {
            nn.bias_o[i] = this.bias_o[i];
        }
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
                    child.weights_ih[i][j] = this.weights_ih[i][j];
                } else {
                    child.weights_ih[i][j] = partner.weights_ih[i][j];
                }
            }
        }
        // pick a random midpoint in the parent's output layer
        // first half from this and second half from partner
        for (int i = 0; i < this.output_nodes; i++) {
            for (int j = 0; j < this.hidden_nodes; j++) {
                if (Math.random() < 0.5) {
                    child.weights_ho[i][j] = this.weights_ho[i][j];
                } else {
                    child.weights_ho[i][j] = partner.weights_ho[i][j];
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
    public void mutate() {
        for (int i = 0; i < this.hidden_nodes; i++) {
            for (int j = 0; j < this.input_nodes; j++) {
                if (Math.random() < 0.1) {
                    this.weights_ih[i][j] = Math.random() * 2 - 1;
                }
            }
        }
        for (int i = 0; i < this.output_nodes; i++) {
            for (int j = 0; j < this.hidden_nodes; j++) {
                if (Math.random() < 0.1) {
                    this.weights_ho[i][j] = Math.random() * 2 - 1;
                }
            }
        }
        for (int i = 0; i < this.hidden_nodes; i++) {
            if (Math.random() < 0.1) {
                this.bias_h[i] = Math.random() * 2 - 1;
            }
        }
        for (int i = 0; i < this.output_nodes; i++) {
            if (Math.random() < 0.1) {
                this.bias_o[i] = Math.random() * 2 - 1;
            }
        }
    }
}
