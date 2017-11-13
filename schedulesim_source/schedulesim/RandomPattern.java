package schedulesim;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author paul
 */
public class RandomPattern implements MetataskPattern {

  private int start;
  private int end;
  private int count;

  public RandomPattern(int start, int end, int count){
    this.start = start;
    this.end = end;
    this.count = count;
  }

  @Override
  public ArrayList<Task> generateMetatask() {
    ArrayList<Task> tasks = new ArrayList<>();
    Random random = new Random();
    for(int i = 0; i < count; i++){
      tasks.add(new Task(random.nextInt(end-start)+start));
    }
    return tasks;
  }


}
