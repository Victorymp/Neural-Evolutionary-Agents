package animat.behaviours;

import animat.Animat;
import objects.Object;
import objects.Trap;

public class ObstacleAvoidance implements Behaviours{
    private Animat animat;
    /**
     * @param animat
     */
    @Override
    public void activate(Animat animat) {
        this.animat = animat;
    }

    /**
     * Avoids the trap by moving in the opposite direction
     * @param currentObject
     */
    @Override
    public void execute(Object currentObject) {
        if(currentObject instanceof Trap){
            Trap trap = (Trap) currentObject;
            if(animat.getX() == trap.getX() && animat.getY() == trap.getY()){
                // move away from the trap by moving in the opposite direction
                animat.updateMoves((animat.getX() - trap.getX())+1, (animat.getY() - trap.getY())+1);
            }
        }
    }

    @Override
    public Animat deactivate() {
        return animat;
    }

    @Override
    public Boolean fires(Object currentObject) {
        if(currentObject instanceof Trap){
            Trap trap = (Trap) currentObject;
            return animat.getX() == trap.getX() && animat.getY() == trap.getY();
            }
        return false;
    }

    @Override
    public Integer getPriority() {
        return 0;
    }
}
