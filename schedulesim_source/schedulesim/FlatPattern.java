package schedulesim;

import java.util.ArrayList;

/**
 *
 * @author paul
 */
public class FlatPattern implements MetataskPattern {

  private int taskSize;
  private int taskCount;

  public FlatPattern(int taskCount, int taskSize){
    this.taskCount = taskCount;
    this.taskSize = taskSize;
  }

  @Override
  public ArrayList<Task> generateMetatask() {
    ArrayList<Task> tasks = new ArrayList<>();
    for(int i = 0; i < taskCount; i++){
      tasks.add(new Task(taskSize));
    }
    return tasks;
  }

}
