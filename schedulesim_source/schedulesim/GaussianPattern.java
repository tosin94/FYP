package schedulesim;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author paul
 */
public class GaussianPattern implements MetataskPattern {

  private final int startSize;
  private final int endSize;
  private final double mu;
  private final double sigma;
  private final int combinedTargetSize;

  public GaussianPattern(int startSize, int endSize, double mu, double sigma, int combinedTargetSize){
    this.startSize = startSize;
    this.endSize = endSize;
    this.mu = mu;
    this.sigma = sigma;
    this.combinedTargetSize = combinedTargetSize;
  }

  @Override
  public ArrayList<Task> generateMetatask() {
    ArrayList<Task> tasks = new ArrayList<>();
    double[] taskSizeDistribution = wieghtedDistribution();
    Random rand = new Random();
    int actualTotalLength = 0;

    while (true) {
      // Pick a task size
      int potentialTaskSize = rand.nextInt(endSize - startSize); // not offset
      // Draw a random number
      double randomChance = rand.nextDouble();
      // For the randomly selected task size, use the random chance against the
      // weighted distro to decide whether to create a task of this size
      if (randomChance < taskSizeDistribution[potentialTaskSize]) { // not offset
        // Stop adding tasks if adding this task will exceed cause target to be exceed
        if ((actualTotalLength + (potentialTaskSize + startSize)) < combinedTargetSize) {
          // Create the task
          tasks.add(new Task(potentialTaskSize + startSize));
          // Add length of the task to the running total
          actualTotalLength += (potentialTaskSize + startSize); // plus offset
        } else {
          // Break to stop adding tasks
          break;
        }
      }
    }
    return tasks;
  }

  private double[] wieghtedDistribution() {
   // Uses a gaussian curve
   // mu is the "position"
   // sigma is the "width"
   double[] toReturn = new double[endSize - startSize];
   double a = 1 / (sigma * (Math.sqrt((2 * Math.PI))));
   a *= 100000000;
   for (int i = 0; i < (endSize - startSize); i++) {
     toReturn[i] = (a * (Math.exp(-0.5 * Math.pow(((i - mu) / sigma), 2))));
   }

   //Normalise between 0-1
   double max = 0;
   for (int i = 0; i < toReturn.length; i++) {
     if (max < toReturn[i]) {
       max = toReturn[i];
     }
   }
   for (int i = 0; i < toReturn.length; i++) {
     toReturn[i] /= max;
   }
   return toReturn;
 }

}
