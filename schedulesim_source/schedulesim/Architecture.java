package schedulesim;

import java.util.ArrayList;

/**
 *
 * @author paul
 */
public class Architecture {

  private final String name;
  private Producer producer;
  private final ArrayList<ConsumingEntity> consumingEntities;

  public Architecture(String name){
    this.name = name;
    this.consumingEntities = new ArrayList<>();
  }

  public String getName() {
    return name;
  }

  public void addEntity(SimEntity parent , ConsumingEntity child) throws BadParentException {
    if(parent instanceof Producer){
      // Root of the tree
      producer = (Producer)parent;
    } else {
      // Before adding, confirm parent exists
      if(!consumingEntities.contains((ConsumingEntity)parent)){
        throw new BadParentException("Tried to add child with a parent that had not yet been added! " +
                                      "You have to build the tree starting with the Producer.",parent,child);
      }
    }
    // Add child to archictecture
    consumingEntities.add(child);
    // Add child to parent.
    parent.addChild(child);
  }

  public Producer getProducer(){
    return producer;
  }

  public ArrayList<ConsumingEntity> getConsumingEntities(){
    return consumingEntities;
  }

  public ArrayList<Scheduler> getSchedulers() {
    ArrayList<Scheduler> schedulers = new ArrayList<>();
    for(ConsumingEntity entity : consumingEntities){
      if(entity instanceof Scheduler){
        schedulers.add((Scheduler)entity);
      }
    }
    return schedulers;
  }

  public ArrayList<Consumer> getConsumers() {
    ArrayList<Consumer> consumers = new ArrayList<>();
    for(ConsumingEntity entity : consumingEntities){
      if(entity instanceof Consumer){
        consumers.add((Consumer)entity);
      }
    }
    return consumers;
  }
}
