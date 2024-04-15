package animat.behaviours;

import animat.Animat;
import objects.Object;

public interface Behaviours {
//    public void obstacleAvoidance();
//    public void randomMovement();
//    public void exploreGoals();
//    public void planRoute();
//    public void die();
//    public void reward();
//    public void punish();
//    public void sense();
    void activate(Animat animat);
    void execute(Object currentObject);
    Animat deactivate();
    Boolean fires(Object currentObject);
    Integer getPriority();
}
