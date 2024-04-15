package animat.behaviours;

import animat.Animat;
import objects.Object;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Subsumption {
    // create a priority queue of behaviours because we want to execute the highest priority behaviour first
    private PriorityQueue<Behaviours> behaviors;
    private int x;
    private int y;
    private Animat animat;

    /**
     * Constructor
     */
    public Subsumption(){
        // initialize the behaviors priority queue based on the priority of the behaviours
        behaviors = new PriorityQueue<>(Comparator.comparing(Behaviours::getPriority).reversed());

        // initialize all behaviours
        behaviors.add(new ObstacleAvoidance());
        behaviors.add(new RandomMovement());
        behaviors.add(new ExploreGoals());
        behaviors.add(new Plan());
    }

    /**
     * Activates all behaviours
     * @param animat
     */
    public void activate(Animat animat){
        for(Behaviours behaviour: behaviors){
            behaviour.activate(animat);
        }
    }
    /**
     * Executes the behaviour that fires
     * @param object
     */
    public void execute(Object object) {
        for (Behaviours behaviour : behaviors) {
            if (behaviour.fires(object)) {
                behaviour.execute(object);
                animat = behaviour.deactivate();
                break;
            }
        }
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public void deactivate(){
        for(Behaviours behaviour: behaviors){
            behaviour.deactivate();
        }
    }
}
