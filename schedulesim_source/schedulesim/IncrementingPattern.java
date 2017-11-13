/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedulesim;

import java.util.ArrayList;

/**
 *
 * @author paul
 */
public class IncrementingPattern implements MetataskPattern{

  private int start;
  private int stop;

  public IncrementingPattern(int start, int stop){
    this.start = start;
    this.stop = stop;
  }

  @Override
  public ArrayList<Task> generateMetatask() {
    ArrayList<Task> tasks = new ArrayList<>();
    for(int i = start; i < stop; i++){//changed this line of code to i+=5, remember to change to i++
      tasks.add(new Task(i));
    }
    return tasks;
  }

}
