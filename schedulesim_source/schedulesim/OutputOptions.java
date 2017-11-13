package schedulesim;

/**
 *
 * @author paul
 */
public class OutputOptions {
  
  private boolean makespan;
  private boolean utilisation;
  private int countBins;
  
  private boolean renderSchedule;
  
  public OutputOptions(){
    this.makespan= true;
    this.utilisation = true;
    this.countBins = 0;
    this.renderSchedule = false;
  }

  public boolean isMakespan() {
    return makespan;
  }

  public void setMakespan(boolean makespan) {
    this.makespan = makespan;
  }

  public boolean isUtilisation() {
    return utilisation;
  }

  public void setUtilisation(boolean utilisation) {
    this.utilisation = utilisation;
  }

  public int getCountBins() {
    return countBins;
  }

  public void setCountBins(int countBins) {
    this.countBins = countBins;
  }
  
  public boolean isRenderSchedule() {
    return renderSchedule;
  }

  public void setRenderSchedule(boolean renderSchedule) {
    this.renderSchedule = renderSchedule;
  }
}
