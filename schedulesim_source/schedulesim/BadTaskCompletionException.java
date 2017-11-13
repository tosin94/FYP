package schedulesim;

/**
 *
 * @author paul
 */
public class BadTaskCompletionException extends Exception {
  private int tasksSent;
  private int tasksCompleted;
  public BadTaskCompletionException(String message, int tasksSent, int tasksCompleted){
    super(message);
    this.tasksSent = tasksSent;
    this.tasksCompleted = tasksCompleted;
  }

  public int getTasksSent() {
    return tasksSent;
  }

  public int getTasksCompleted() {
    return tasksCompleted;
  }
}
