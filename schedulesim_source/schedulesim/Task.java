package schedulesim;

import java.util.Comparator;

/**
 *
 * @author paul
 */
public class Task {

  private static int staticTid = 0;

  private final int tid;

  private final int startUnits;
  private int remainingUnits;
  private final int stepSubmitted;
  private int stepProcessingStarted;
  private int stepFinished;

  public Task(int units) {
    this.tid = staticTid++;
    this.startUnits = units;
    this.remainingUnits = units;
    this.stepSubmitted = ScheduleSim.getSimulationStep();
    this.stepProcessingStarted = -1;
  }

  public int getTid() {
    return tid;
  }

  public int decrementUnits(int units) {
    if (stepProcessingStarted == -1) {
      stepProcessingStarted = ScheduleSim.getSimulationStep();
    }

    if ((remainingUnits - units) > 0) {
      // The units from the consumer where completely used up
      this.remainingUnits -= units;
      return 0;
    } else {
      // The units from the consumer were not completed used up, there some process
      // units left over for another task to use.
      int unUsedUnits = (units - remainingUnits);
      // This task is finished
      this.remainingUnits = 0;
      this.stepFinished = ScheduleSim.getSimulationStep();
      // return the un used consumer unit so the next task on the consumer can
      // use them.
      return unUsedUnits;
    }
  }

  public boolean isFinished() {
    return (remainingUnits == 0);
  }

  public int getStartUnits() {
    return startUnits;
  }

  public int getRemaingUnits() {
    return remainingUnits;
  }

  public int getStepSubmitted() {
    return stepSubmitted;
  }

  public int getStepProcessingStarted(){
    return stepProcessingStarted;
  }

  public int getStepFinished() {
    return stepFinished;
  }
}
