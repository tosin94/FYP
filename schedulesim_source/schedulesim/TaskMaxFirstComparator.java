package schedulesim;

import java.util.Comparator;

/**
 *
 * @author paul
 */
public class TaskMaxFirstComparator implements Comparator<Task>{
  @Override
  public int compare(Task o1, Task o2) {
    return Integer.compare(o2.getRemaingUnits(), o1.getRemaingUnits());
  }
}
