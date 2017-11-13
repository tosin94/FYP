package schedulesim;

/**
 * 
 * @author paul
 */
public class BadParentException extends Exception{
  
  private SimEntity parent;
  private ConsumingEntity child;
  
  public BadParentException(String message, SimEntity parent, ConsumingEntity child){
    super(message);
    this.parent = parent;
    this.child = child;
  }

  public SimEntity getParent() {
    return parent;
  }

  public ConsumingEntity getChild() {
    return child;
  }
  
  
}
