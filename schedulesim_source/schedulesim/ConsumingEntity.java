package schedulesim;

import java.util.ArrayList;

/**
 *
 * @author paul
 */
public abstract class ConsumingEntity extends SimEntity {

  private final ArrayList<Task> waiting;

  public ConsumingEntity() {
    super();
    waiting = new ArrayList<>();
  }

  public abstract int getUnitsPerStep();

  public abstract double getDelay();

  public abstract double getUtilisation();

  public void submitTask(Task task) {
    waiting.add(task);
  }

  public ArrayList<Task> getWaitingTasks() {
    return waiting;
  }

  @Override
  public boolean isFinished() {
    return waiting.isEmpty();
  }
}
