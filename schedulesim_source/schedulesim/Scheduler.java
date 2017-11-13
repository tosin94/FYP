package schedulesim;

/**
 *
 * @author paul
 */
public abstract class Scheduler extends ConsumingEntity {

  public Scheduler(){
    super();
  }



  @Override
  public int getUnitsPerStep() {
    // Find the fastest UPS
    int totalUPS = 0;
    for(ConsumingEntity entity : this.getChildren()){
        totalUPS += entity.getUnitsPerStep();
    }
    return totalUPS;
  }

  @Override
  public double getUtilisation() {
    // Find a consumer with utilisation 0.0 if there is one
    double totalUtil = 0.0;
    for(ConsumingEntity entity : this.getChildren()){
        totalUtil += entity.getUtilisation();
    }
    if(this.getChildren().size() > 0){
      totalUtil /= this.getChildren().size();
    }
    return totalUtil;
  }

  @Override
  public double getDelay() {
    // Find the consumer with the shortest delay
    double shortestDelay = 0;
    for(ConsumingEntity entity : this.getChildren()){
      if(shortestDelay > entity.getDelay()){
        shortestDelay = entity.getDelay();
      }
    }
    return shortestDelay;
  }
}
