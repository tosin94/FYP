package schedulesim.scheduler;

import java.util.*;
import schedulesim.ConsumingEntity;
import schedulesim.Scheduler;


/**
 *
 * @author paul
 */
public class RoundRobinScheduler extends Scheduler {

  private int index;
    public HashMap<ConsumingEntity, Integer> list =new HashMap<>();


  public RoundRobinScheduler() {
    super();
    index = 0;
  }

  @Override
  public void step() {
    super.step();
    if (super.getChildren().size() > 0) {
      while(super.getWaitingTasks().size() > 0){
        super.getChildren().get(index++ % super.getChildren().size()).submitTask(super.getWaitingTasks().remove(0));
      }
    }
  }
 
}
