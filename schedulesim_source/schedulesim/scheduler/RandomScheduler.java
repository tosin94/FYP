package schedulesim.scheduler;

import java.util.*;
import java.util.Random;
import schedulesim.ConsumingEntity;
import schedulesim.Scheduler;

/**
 *
 * @author paul
 */
public class RandomScheduler extends Scheduler {

  private Random random;
    public HashMap<ConsumingEntity, Integer> list =new HashMap<>();


  public RandomScheduler(){
    super();
    random = new Random();
  }

  @Override
  public void step() {
    super.step();
    if (super.getChildren().size() > 0) {
      while(super.getWaitingTasks().size() > 0){
        super.getChildren().get(random.nextInt(super.getChildren().size())).submitTask(super.getWaitingTasks().remove(0));
      }
    }
  }
  
  

}
