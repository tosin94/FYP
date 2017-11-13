package schedulesim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author paul
 */
public class ScheduleSim {

  // Version
  private static String version = "1.3.0"; // Major . Minor . BugFix

  // Current simulator time
  private static int step = 0;

  // Experiments to run
  private ArrayList<Architecture> experiments;

  public static void main(String[] args) {
    Log.println("ScheduleSim " + version);
    Log.println("Running self test...");
    //Tester.runTests();
    Hierarchical_test.runTests();
  }

  public ScheduleSim() {
    this.experiments = new ArrayList<>();
    reset();
  }

  public static int getSimulationStep() {
    return step;
  }

  public void addExperiment(Architecture experiment) {
    experiments.add(experiment);
  }

  public void addExperiments(ArrayList<Architecture> experiments) {
    this.experiments.addAll(experiments);
  }

  public void runExperiments(OutputOptions outputOptions) throws BadStepsException, BadTaskCompletionException {
    // Print results header
    printResultsHeader(outputOptions);

    // Loop over experiments
    for (Architecture architecture : experiments) {
      // reset simulator
      reset();

      // Run experiment
      runExperiment(architecture);

      // Check simulation step integrity
      checkStepIntegrity(architecture);

      // Check task completion integrity
      checkTaskCompletedIntegrity(architecture);

      // Output results
      printResult(outputOptions,
        architecture.getProducer().getName(),
        architecture.getName(),
        architecture.getConsumers());

      // Draw images
      if (outputOptions.isRenderSchedule()) {
        renderScheduleDiagram(architecture.getConsumers(),
          architecture.getProducer().getName(),
          architecture.getName());
      }
    }

  }

  private void reset() {
    step = 0;
  }

  private void runExperiment(Architecture architecture) {
    while (!isExperimentFinsihed(architecture)) {
      stepSimulator(architecture);
    }
  }

  private boolean isExperimentFinsihed(Architecture architecture) {
    // Check whether the consumer and schedulers are finished
    for (ConsumingEntity consumingEntity : architecture.getConsumingEntities()) {
      if (!consumingEntity.isFinished()) {
        return false;
      }
    }
    // Lastly check for tasks that haven't been sent yet
    return architecture.getProducer().isFinished();
  }

  private void stepSimulator(Architecture architecture) {
    step++;
    stepProducer(architecture);
    stepConsumingEntities(architecture);
  }

  private void stepProducer(Architecture architecture) {
    architecture.getProducer().step();
  }

  private void stepConsumingEntities(Architecture architecture) {
    // The schedulers and consumers are stepped in random order. Why is this?
    // Imagine consumer 1 was always stepped before consumer 2. Then
    // from consumer 2's point of view consumer 1 is always be slgihtly ahead
    // in time. This behaviour could skew data, so address this, schedulers and
    // consumers (ConsumingEntities) are stepping in radnom order.

    // ScheduleSim is not a network simulator and network propagation is not
    // simulated. Thus, in ScheduleSim tasks can transverse
    // from the producer to a consumer through any number of schedulers in a
    // single time step. To avoid the randomness preventing consumer transvering
    // fully each tier of network is stepped randomly.
    // In short like Breadth First crossed with Random in term of tree search
    ArrayList<ConsumingEntity> firstLayer = architecture.getProducer().getChildren(); // only one
    stepBreadthFirstRandnom(firstLayer);
  }

  private void stepBreadthFirstRandnom(ArrayList<ConsumingEntity> entities) {
    Collections.shuffle(entities);
    ArrayList<ConsumingEntity> nextLayer = new ArrayList<>();
    for (ConsumingEntity entity : entities) {
      entity.step();
      if (entity instanceof Scheduler) {
        nextLayer.addAll(((Scheduler) entity).getChildren());
      }
    }

    if (!nextLayer.isEmpty()) {
      stepBreadthFirstRandnom(nextLayer);
    }
  }

  private void checkStepIntegrity(Architecture architecture) throws BadStepsException {
    // Check integrity of SimEntities
    // make sure no one has stepped too much
    if (!architecture.getProducer().checkStep()) {
      throw new BadStepsException("Producer took incorrect number of steps.", architecture.getProducer());
    }
    for (ConsumingEntity consumingEntity : architecture.getConsumingEntities()) {
      if (!consumingEntity.checkStep()) {
        throw new BadStepsException("Consuming entity took incorrect number of steps.", consumingEntity);
      }
    }
  }

  private void checkTaskCompletedIntegrity(Architecture architecture) throws BadTaskCompletionException {
    // Does the number of the tasks the producer sent match the
    // the number of tasks ompleted by the consumers
    int consumerCompletedTaskCount = 0;
    for (Consumer consumer : architecture.getConsumers()) {
      consumerCompletedTaskCount += consumer.getCompletedTasks().size();
    }
    if (consumerCompletedTaskCount != architecture.getProducer().getTasksSubmittedCount()) {
      throw new BadTaskCompletionException("Task completed did not match the number of task sent!",
        architecture.getProducer().getTasksSubmittedCount(), consumerCompletedTaskCount);
    }
  }

  private static void printResultsHeader(OutputOptions outputOptions) {
    Log.println("ScheduleSim Experiment Results");
    String header = "Architecture, Producer,";
    if (outputOptions.isMakespan()) {
      header += "Makespan(step),";
    }
    if (outputOptions.isUtilisation()) {
      header += "Utilisation(%),";
    }
    if (outputOptions.getCountBins() > 0) {
      for (int i = 0; i < outputOptions.getCountBins(); i++) {
        header += "Task Size Group " + i + " Avg. Makespan,";
      }
    }
  }

  private static void printResult(OutputOptions outputOptions, String producerName, String architectureName, ArrayList<Consumer> consumers) {
    // Gather completed tasks
    ArrayList<Task> completedTasks = new ArrayList<>();
    for (Consumer consumer : consumers) {
      completedTasks.addAll(consumer.getCompletedTasks());
    }

    String resultStr = architectureName + "," + producerName + ",";
    if (outputOptions.isMakespan()) {
      resultStr += getMakespan(completedTasks) + " <-make, ";
    }
    if (outputOptions.isUtilisation()) {
      resultStr += getUtilisation(consumers) + " <-,util ";
    }
    if (outputOptions.getCountBins() > 0) {
      double[] binsResult = gettaskSizeBinsAverageMakespans(completedTasks, outputOptions.getCountBins());
      for (int i = 0; i < outputOptions.getCountBins(); i++) {
        resultStr += binsResult[i] + ",";
      }
    }
    Log.println(resultStr);
  }

  public static int getMakespan(ArrayList<Task> tasks) {
    // Get first task submission time, this will count as our start time
    int firstSubmissionStep = -1;
    for (Task task : tasks) {
      if (task.getStartUnits() < firstSubmissionStep || (firstSubmissionStep == -1)) {
        firstSubmissionStep = task.getStepSubmitted();
      }
    }

    // Get time of last task to finish
    int lastFinishStep = -1;
    for (Task task : tasks) {
      if ((task.getStepFinished() > lastFinishStep)
        || (lastFinishStep == -1)) {
        lastFinishStep = task.getStepFinished();
      }
    }

    // This shouldn't happen
    if (lastFinishStep < firstSubmissionStep) {
      return 0;
    }

    return lastFinishStep - firstSubmissionStep;
  }

  public static double getUtilisation(ArrayList<Consumer> consumers) {
    // How much did these tasks utilise these consumers?

    // Having sense of UPS is important so that it is represented that it is worse
    // having a big resource ilde than a small one.
    // Get total UPS
    int totalUPS = 0;
    for (Consumer consumer : consumers) {
      totalUPS += consumer.getUnitsPerStep();
    }

    // Get utilisation of each consumer
    double consumerUtilisation = 0.0;
    for (Consumer consumer : consumers) {
      consumerUtilisation += (consumer.getTotalUtilisation() * consumer.getUnitsPerStep()) / totalUPS;
    }

    return consumerUtilisation;
  }

  public static double[] gettaskSizeBinsAverageMakespans(ArrayList<Task> tasks, int binCount) {

    double[] avgTaskMakespanForBin = new double[binCount];
    int[] taskCountInBins = new int[binCount];
    int[] totalTaskMakespan = new int[binCount];

    // Find biggest and smallest task
    int smallestTaskUnits = -1;
    int largestTaskUnits = -1;
    for (Task task : tasks) {
      if (task.getStartUnits() < smallestTaskUnits || smallestTaskUnits == -1) {
        smallestTaskUnits = task.getStartUnits();
      }
      if (task.getStartUnits() > largestTaskUnits) {
        largestTaskUnits = task.getStartUnits();
      }
    }

    // Create group (bins) based on task size (equi-width)
    double binWidth = ((double)(largestTaskUnits - smallestTaskUnits) / (double)binCount);

    // Place each task in the group based on it's length
    for (Task task : tasks) {
      int binIndex = 0;
      while ((((binIndex + 1) * binWidth) + smallestTaskUnits) < task.getStartUnits()) {
        binIndex++;
      }
      totalTaskMakespan[binIndex] += task.getStepFinished() - task.getStepSubmitted();
      taskCountInBins[binIndex]++;
    }

    // Calculate average task group finish time
    for (int binIndex = 0; binIndex < binCount; binIndex++) {
      avgTaskMakespanForBin[binIndex] = ((double) totalTaskMakespan[binIndex] / (double) taskCountInBins[binIndex]);
    }

    return avgTaskMakespanForBin;
  }

  private static void renderScheduleDiagram(ArrayList<Consumer> consumers, String producerName, String architectureName) {
    try {
      RenderSchedule render = new RenderSchedule(consumers);
      render.renderImageToFile(producerName + "_" + architectureName);
    } catch (IOException ioe) {
      Log.println("Failed to write image: " + ioe.getMessage());
    } catch (BadRenderException bre) {
      Log.println("Failed to  render image: " + bre.getMessage());
    }

  }
}
