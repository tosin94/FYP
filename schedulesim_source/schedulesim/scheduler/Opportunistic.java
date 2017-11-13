package schedulesim.scheduler;
/**
 *
 * @author Samuel Omotayo
 */

import java.util.*;
import schedulesim.Consumer;
import schedulesim.ConsumingEntity;
import schedulesim.SimEntity;
import schedulesim.Task;
import schedulesim.Scheduler;
import schedulesim.scheduler.*;

public class Opportunistic extends Scheduler{
    //HashMap to use to determine which child gets allocated job
    private HashMap<ConsumingEntity, Integer> results = new HashMap<>();
    private HashMap<ConsumingEntity, Double> delay = new HashMap<>();
    private int i =0, min_util, value = 0, count = 0;
    private Map.Entry entry;
    private ConsumingEntity child;
    private Task task;
    private SimEntity scheduler, decision;
    private boolean check = true;
    private double updatedDelay;
    private ConsumingEntity bestScheduler;

    
    public Opportunistic(){
        super();
    }
    
    @Override
    public void step(){
        super.step();
        if (count == 0){//round robin one task to all consumers
            for (ConsumingEntity entity: super.getChildren()){
                for (ConsumingEntity consumer: entity.getChildren()){
                    consumer.submitTask(super.getWaitingTasks().remove(0));
                }
            }
            for (ConsumingEntity entity: super.getChildren()){
                for (ConsumingEntity consumer: entity.getChildren()){
                    delay.put(consumer, consumer.getDelay());
                }
            }
            count++;
        }
        if (super.getChildren().size() > 0){ //as long as there are children run this code block

            for (ConsumingEntity sch: super.getChildren()){
                    for (ConsumingEntity consume: sch.getChildren()){
                        results.put(consume,consume.getUnitsPerStep());// units per step of all consumers will be here.
                    }
                }
            
            //choose scheduler to allocate job
            while (super.getWaitingTasks().size()>0){
                task = super.getWaitingTasks().get(0);
                
                Set set = results.entrySet();
                Iterator iterator = set.iterator();
                                        
                    while (iterator.hasNext()){//iterate amount of units per step per map
                        entry = (Map.Entry)iterator.next();
                        child = (ConsumingEntity)entry.getKey();//consumer is here
                        scheduler = child.getParent(); //parent is here

                        updatedDelay = task.getRemaingUnits() - ((double)child.getUnitsPerStep() + (double)delay.get(child));
                        min_util = (int)updatedDelay;
                        
                        if (min_util < value || check ){
                            value = min_util;
                            check = false;
                            decision = scheduler; //scheduler with least delay is chosen
                        }
                        delay.put(child, (double)min_util);//update delays
                    }
                    
                bestScheduler = (ConsumingEntity)decision;
                bestScheduler.submitTask(super.getWaitingTasks().remove(0));
            }
        }
    }
}
    

