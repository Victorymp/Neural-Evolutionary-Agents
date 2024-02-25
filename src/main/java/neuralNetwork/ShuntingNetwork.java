package neuralNetwork;

public class ShuntingNetwork {
    private double[][] neurons; // 2D array representing the environment
    private double A; // passive decay rate
    private double E; // large integer for Iota calculation

    public ShuntingNetwork(int size, double A, double E) {
        this.neurons = new double[size][size];
        this.A = A;
        this.E = E;
    }

    // Update the state of the neuron at position (i, j)
    public void updateNeuron(int i, int j, double I) {
        double sum = 0;
        // Calculate the sum of the states of the neighboring neurons
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0) continue; // Skip the neuron itself
                int ni = i + di;
                int nj = j + dj;
                if (ni >= 0 && ni < neurons.length && nj >= 0 && nj < neurons[0].length) {
                    double wij = Math.sqrt(di * di + dj * dj); // Euclidean distance
                    sum += wij * Math.max(0, neurons[ni][nj]);
                }
            }
        }
        // Update the state of the neuron
        neurons[i][j] = A * neurons[i][j] + I + sum;
    }

    // Calculate the gradient of the activity landscape at position (i, j)
    public double[] calculateGradient(int i, int j) {
        double dx = 0;
        double dy = 0;
        if (i > 0) dx = neurons[i][j] - neurons[i - 1][j];
        if (j > 0) dy = neurons[i][j] - neurons[i][j - 1];
        return new double[] {dx, dy};
    }

    // Update the position of the animat based on the gradient of the activity landscape
    public int[] updatePosition(int i, int j) {
        double[] gradient = calculateGradient(i, j);
        int ni = i;
        int nj = j;
        if (gradient[0] > 0) ni++;
        else if (gradient[0] < 0) ni--;
        if (gradient[1] > 0) nj++;
        else if (gradient[1] < 0) nj--;
        return new int[] {ni, nj};
    }
}