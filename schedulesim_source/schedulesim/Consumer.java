package schedulesim;

import java.util.ArrayList;

/**
 *
 * @author paul
 */
public class Consumer extends ConsumingEntity {

  private int unitPerStep;
  private int unitsUsed;

  private ArrayList<Task> completedTasks;

  public Consumer(int unitPerStep) throws IllegalArgumentException{
    super();
    // Conusmer must atleast have a processing speed of atleast 1.
    if(unitPerStep < 1){
      throw new IllegalArgumentException("Tried to create consumer with speed < 1");
    }
    this.unitPerStep = unitPerStep;
    this.unitsUsed = 0;
    this.completedTasks = new ArrayList<>();
  }

  @Override
  public int getUnitsPerStep() {
    return unitPerStep;
  }

  @Override
  public double getUtilisation() {
    if(!super.getWaitingTasks().isEmpty()){
      return 1.0;
    }else{
      return 0.0;
    }
  }

  @Override
  public double getDelay() {
    double delay = 0;
    for(Task task : super.getWaitingTasks()){
      // Work out the delay for each task
      delay += (task.getRemaingUnits() / unitPerStep);
    }
    return delay;
  }

  public ArrayList<Task> getCompletedTasks(){
    return completedTasks;
  }

  public double getTotalUtilisation(){
    return ((double)unitsUsed) / ((double)(ScheduleSim.getSimulationStep() * unitPerStep));
  }

  @Override
  public void submitTask(Task task){
    super.submitTask(task);
  }

  @Override
  public void step() {
    super.step();

    // Apply the unitPerStep of this consumer to the tasks in the waiting list
    int remainingUnitsForThisStep = unitPerStep;
    while(remainingUnitsForThisStep > 0){

      // Is work to do
      if(super.getWaitingTasks().isEmpty()){
        // There is no more work to do, stop.
        break;
      }

      // Decrement task size by the unitPerStep of this consumer. Note that in the
      // event the work the task has to do is smaller than the unitPerStep then
      // the remaining unitPerStep is returned so there can be applied to the
      // next task.
      remainingUnitsForThisStep = super.getWaitingTasks().get(0).decrementUnits(remainingUnitsForThisStep);

      // Is the task finished
      if(super.getWaitingTasks().get(0).isFinished()){

        // add to completed tasks list
        completedTasks.add(super.getWaitingTasks().get(0));

        // Remove from waiting list
        super.getWaitingTasks().remove(0);
      }
    }

    // Update running utilisation figure
    unitsUsed += (unitPerStep - remainingUnitsForThisStep);
  }
}
