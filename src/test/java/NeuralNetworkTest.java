import neuralNetwork.NeuralNetwork;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NeuralNetworkTest {
    @Test
    void testFeedForward() {
        // Initialize a NeuralNetwork object
        NeuralNetwork nn = new NeuralNetwork(3, 3, 2, 0.1);

        // Define the input array
        double[] inputArray = {0.5, -0.2, 0.1};

        // Call the feedForward method
        double[] output = nn.feedForward(inputArray);

        // Check the output
        // Since the weights and biases are randomly initialized, we can't predict the exact output.
        // But we can check if the output array has the correct length (equal to the number of output nodes)
        assertEquals(2, output.length);
    }


}
