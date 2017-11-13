package schedulesim;

import java.util.Comparator;

/**
 *
 * @author paul
 */
public class TaskMinFirstComparator implements Comparator<Task>{

  @Override
  public int compare(Task o1, Task o2) {
    return Integer.compare(o1.getRemaingUnits(), o2.getRemaingUnits());
  }
}
