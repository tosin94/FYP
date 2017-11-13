package schedulesim.scheduler;

import java.util.Random;
import schedulesim.Scheduler;
import schedulesim.ConsumingEntity;

/**
 *
 * @author paul
 */
public class ShoppingScheduler extends Scheduler {

  private int optionCount;
  private Random random;

  public ShoppingScheduler(){
    super();
    optionCount = 16;
    random = new Random();
  }

  @Override
  public void step() {
    super.step();
    if (super.getChildren().size() > 0) {
      while(super.getWaitingTasks().size() > 0){

        // Make sure there is enough consuming entities
        if(optionCount > super.getChildren().size()){
          optionCount = super.getChildren().size();
        }

        // Randomly choose X consuming entities
        // Pick the fastest one (highest UPS) thus (min. execution time).
        ConsumingEntity minEntity = null;
        for(int i = 0; i < optionCount; i++){
          ConsumingEntity temp = super.getChildren().get(random.nextInt(super.getChildren().size()));
          if( minEntity == null ||
              temp.getUnitsPerStep() > minEntity.getUnitsPerStep()){
            minEntity = temp;
          }
        }

        // Assign task to min of randomly entities
        minEntity.submitTask(super.getWaitingTasks().remove(0));
      }
    }
  }

}
