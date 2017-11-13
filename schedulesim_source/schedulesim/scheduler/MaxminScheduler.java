package schedulesim.scheduler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import schedulesim.ConsumingEntity;
import schedulesim.Task;
import schedulesim.TaskMaxFirstComparator;
import schedulesim.Scheduler;
//line below added by tosin
import java.util.ArrayList;

/**
 *
 * @author paul
 */
public class MaxminScheduler extends Scheduler {
    public HashMap<ConsumingEntity, Integer> list =new HashMap<>();
    
  public MaxminScheduler(){
    super();
  }

  @Override
  public void step() {
    super.step();
    if(super.getChildren().size()>0){

      // This will store the delays on children below.
      HashMap<ConsumingEntity, Double> childDelay = new HashMap<>();

  
      // Find any exsisting delay on child from previous waves
      for (ConsumingEntity child : super.getChildren()) {
        // Create entry for child
        childDelay.put(child, child.getDelay());
      }
      
      // Sort the task max first, biggest tasks first
      Collections.sort(super.getWaitingTasks(), new TaskMaxFirstComparator());

      while(super.getWaitingTasks().size()>0){
        Task task = super.getWaitingTasks().get(0);
        

        // Which ConsumingEntity can finish it first? i.e in the min. time,
        // takes into account the UnitPerStep of the ConsumingEntity and tasks
        // already scheduled to it.
        double minChildDelay = 0.0;
        ConsumingEntity minChild = null;

        // Which child can complete task first
        for(ConsumingEntity child : super.getChildren()){
          double taskMakespanWithChildDelay = ((double)task.getRemaingUnits() / (double)child.getUnitsPerStep()) + childDelay.get(child);
          // Can this child finish the task faster
          if(taskMakespanWithChildDelay < minChildDelay || minChild == null){
            minChild = child;
            minChildDelay = taskMakespanWithChildDelay;
          }
        }

        // Update child delays map
        childDelay.put(minChild, minChildDelay);

        // Submit task to child
        minChild.submitTask(super.getWaitingTasks().remove(0));
      }
    }
  }
}
