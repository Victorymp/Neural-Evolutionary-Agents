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
    private double value;
    private double bias;
    private double weights;
    private double delta;
    private List<Neuron> receptiveField;
    private Object object;
    private double iota;

    Neuron(double value, double bias, double weights, int X, int Y) {
        this.value = value;
        this.bias = -0.1;
        this.weights = weights;
        this.X = X;
        this.Y = Y;
        this.receptiveField = new ArrayList<>();
    }

    public double getValue() {
        return value;
    }

    void setValue(double value) {
        this.value = value;
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
        double sum = 0;
        for (Neuron neuron : receptiveField) {
            sum += neuron.getValue() * weights ;
        }
        if (object != null) {
            sum += object.getIota();
        }
        value = sum + bias;
        tanh();
    }

    public void setDelta(double delta) {
        this.delta = delta;
    }

    public void setObject(Object object) {
        this.object = object;
    }

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

    public void clearReceptiveField() {
        receptiveField.clear();
    }

    public double getIota() {
        return iota;
    }

    void setIota(double iota) {
        this.iota = iota;
    }

    private void tanh() {
        value = Math.tanh(value);
    }
}
