package schedulesim;

import java.util.Comparator;

/**
 *
 * @author paul
 */
public class ConsumingEntityMinFirstComparator implements Comparator<ConsumingEntity>{
  @Override
  public int compare(ConsumingEntity o1, ConsumingEntity o2) {
    return Integer.compare(o2.getUnitsPerStep(), o1.getUnitsPerStep());
  }

}
