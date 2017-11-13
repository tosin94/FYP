package schedulesim;

import java.util.ArrayList;

/**
 *
 * @author paul
 */
public abstract class SimEntity {

  private static int StaticId = 0;
  private int id;
  private SimEntity parent;
  private ArrayList<ConsumingEntity> children;

  private int entityStep ;

  public SimEntity() {
    this.id = StaticId++;
    this.entityStep = 0;
    this.children = new ArrayList<>();
  }

  public void step(){
    entityStep++;
  }

  public abstract boolean isFinished();

  public int getId() {
    return id;
  }

  public void addChild(ConsumingEntity entity) {
    // Does the entity already have a parent
    if (entity.getParent() == null) {
      // Assign self as parent
      entity.setParent(this);
      children.add(entity);
    } else {
      // Deassign previous parent
      entity.getParent().removeChild(entity);
      // Assign self as parent
      entity.setParent(this);
      children.add(entity);
    }
  }

  public void removeChild(ConsumingEntity entity) {
    children.remove(entity);
  }

  public ArrayList<ConsumingEntity> getChildren() {
    return children;
  }

  public SimEntity getParent() {
    return parent;
  }

  public void setParent(SimEntity parent) {
    this.parent = parent;
  }

  public boolean checkStep(){
    // When the simulation finishes gracefully, or at the end of a step of the
    // simulator, all the entities should be on the same step. This function
    // can be used to verify this entity is on the right step.
    return (entityStep == ScheduleSim.getSimulationStep());
  }
}
