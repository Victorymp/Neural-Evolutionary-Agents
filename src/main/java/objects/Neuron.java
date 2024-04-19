package objects;

import java.util.ArrayList;
import java.util.List;
/**
 * Implements the Neuron object
 * Is no modifier because it is package-private for the animat package
 */
public class Neuron {
    private int X;
    private int Y;
    private double currentValue;
    private double bias;
    private double weights;
    private double delta;
    private List<Neuron> receptiveField;
    private Object object;
    private double iota;

    private double activity;
    private double decay;
    private double input;
    private double connectionWeight;
    private double E;

    Neuron(double currentValue, double bias, double weights, int X, int Y) {
        this.currentValue = currentValue;
        this.bias = -0.1;
        this.weights = weights;
        this.X = X;
        this.Y = Y;
        this.receptiveField = new ArrayList<>();
        this.decay = 0.1;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    double getBias() {
        return bias;
    }

    void setBias(double bias) {
        this.bias = bias;
    }

    double getWeights() {
        return weights;
    }

    void setWeights(double weights) {
        this.weights = weights;
    }

    /**
     * Generates random weights for the neuron
     * @param size
     */
    void generateWeights(int size) {
        for (int i = 0; i < size; i++) {
            // Generate a random weight between -1 and 1
            weights = Math.random() * 2 - 1;
        }
    }

    /**
     * Generates all the neurons accessible 3x3 to the current neuron
     */
    public void generateReceptiveField() {
        // Check if the receptive field is empty
        // Initialize the receptive field
            receptiveField = new ArrayList<>();
            for (int i = X - 1; i <= X + 1; i++) {
                for (int j = Y - 1; j <= Y + 1; j++) {
                    // Check if the neighbor is within the grid
                    if (i >= 0 && i < 20 && j >= 0 && j < 20) {
                        // Add the neuron to the receptive field
                        receptiveField.add(new Neuron(0, 0, euclideanDistance(i,j), i, j));
                    }
                }
            }
    }


    void setReceptiveField(List<Neuron> receptiveField) {
        this.receptiveField = receptiveField;
    }

    public List<Neuron> getReceptiveField() {
        return receptiveField;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public void activate() {
        // Calculate the sum of the weights and the current value
        double sum = 0;
        // If the neighbouring current value is negative, set it to 0
        // Weights are Euclidean distance
        for (Neuron neighbours : receptiveField)
            sum += Math.max(0, neighbours.getCurrentValue()) * euclideanDistance(neighbours);
        // Calculate the activity
        // Decay is 0.1 and represents the degree each neurons activity decays towards idle state
        // Current value is the activity of the neuron
        // Iota is the reward signal
        double activity = (-decay * currentValue) + object.getIota();
        currentValue = activity + sum;
    }


    public void updateActivity(){
        double sum = 0;
        for (Neuron neighbor : receptiveField) {
            sum += neighbor.getCurrentValue() * euclideanDistance(neighbor);
        }
        double activity = -decay * sum + input;
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public void setObject(Object object) { this.object = object;}

    public Object getObject() {
        if (object == null) {
            return null;
        }
        return object;
    }


    public void addReceptiveField(Neuron neuron) {
        receptiveField.add(neuron);
    }

    private double euclideanDistance(int x, int y) {

        return Math.sqrt(Math.pow(X - x, 2) + Math.pow(Y - y, 2));
    }

    private double euclideanDistance(Neuron neuron) {
        return Math.sqrt(Math.pow(X - neuron.getX(), 2) + Math.pow(Y - neuron.getY(), 2));
    }

    public void clearReceptiveField() {
        receptiveField.clear();
    }

    public double getIota() {
        return iota;
    }

    void setIota(double iota) {
        if (object.getClass()== Trap.class) {
            this.iota = -iota;
        } else {
            this.iota = iota;
        }
//        if (object.getClass() == Stone.class) {
//            System.out.println("Stone: " + object.getClass());
//            System.out.println("Iota: " + this.iota);
//        }
    }

    private void tanh() {
        currentValue = Math.tanh(currentValue);
    }
}
