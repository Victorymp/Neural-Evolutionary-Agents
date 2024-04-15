package animat.behaviours;

import animat.Animat;
import objects.Object;

public class ExploreGoals implements Behaviours {
    private Animat animat;
    /**
     * @param animat
     */
    @Override
    public void activate(Animat animat) {
        this.animat = animat;
    }

    /**
     * @param object
     */
    @Override
    public void execute(Object object) {

    }

    /**
     *
     */
    @Override
    public Animat deactivate() {
        return animat;
    }

    @Override
    public Boolean fires(Object currentObject) {
        return false;
    }

    @Override
    public Integer getPriority() {
        return 2;
    }
}
