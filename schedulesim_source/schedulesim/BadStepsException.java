package schedulesim;

/**
 *
 * @author paul
 */
public class BadStepsException extends Exception {
  private SimEntity simEntity;
  public BadStepsException(String message, SimEntity simEntity){
    super(message);
    this.simEntity = simEntity;
  }

  public SimEntity getSimEntity() {
    return simEntity;
  }  
}
