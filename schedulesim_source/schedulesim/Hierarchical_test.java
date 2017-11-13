
package schedulesim;

/**
 *
 * @author tosinomotayo
 */


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import schedulesim.scheduler.MaxminFastTrackScheduler;
import schedulesim.scheduler.MaxminScheduler;
import schedulesim.scheduler.MinminScheduler;
import schedulesim.scheduler.RandomScheduler;
import schedulesim.scheduler.RoundRobinScheduler;
import schedulesim.scheduler.ShoppingScheduler;
import schedulesim.scheduler.Opportunistic;
import schedulesim.Consumer;

public class Hierarchical_test {
    
    public static void runTests() {
        boolean result;
        Log.println("\nRunning Tests:\n");
    
        // Test hierarchical

        /*Log.println("testing EXPR_1");
        result = expr_1();
        Log.println((result ? "SUCCESS\n" : "FAILURE\n"));*/

        /*Log.println("testing EXPR_2");
        result = expr_2();
        Log.println((result ? "SUCCESS\n" : "FAILURE\n"));*/
        
        /*Log.println("testing EXPR_3");
        result = expr_3();
        Log.println((result ? "SUCCESS\n" : "FAILURE\n"));*/

        /*Log.println("Testing Opportunistic...");
        result = testHierarchical3();
        Log.println((result ? "SUCCESS\n" : "FAILURE\n"));*/

        /*Log.println("Testing Hierarchical........Dyanmic");
        result = dynamicTest();
        Log.println((result ? "SUCCESS\n" : "FAILURE\n"));*/

    }
    
    
  private static boolean expr_1() {
    // Create simulation
    // expr 1
    for (int i =1; i <=10; i++){
    ScheduleSim sim = new ScheduleSim();

    // Create Producer, this will dispatch waves of jobs
    Producer producer = new Producer("Opp");

    // Create Job Pattern(s), this will create the wave of jobs. There several
    int taskCount = 20;
    int taskSize = 16;
    FlatPattern flat = new FlatPattern(taskCount, taskSize);
    
    int startSize = 20;
    int endSize = 150;
    double mu = 11;
    double sigma = 4;
    int combinedTargetSize = 5000;
    
    GaussianPattern gaus = new GaussianPattern(startSize,endSize,mu,sigma, combinedTargetSize);
    
    //incrementing pattern
    int start = 20;
    int end = 150;
    IncrementingPattern incrementing = new IncrementingPattern(start,end);
        
    producer.addMetatask(1,gaus);
    producer.addMetatask(10,gaus);
    producer.addMetatask(20, incrementing);
 
    
    // Create Architecture
    Architecture architecture = new Architecture("Min_Min");

    // Create Scheduler(s)
    Opportunistic robinTopScheduler = new Opportunistic();
    MinminScheduler robinSubOneScheduler = new MinminScheduler();
    MinminScheduler robinSubTwoScheduler = new MinminScheduler();

    // Create Consumer(s)
    Consumer[] consumers = new Consumer[10];
    consumers[0] = new Consumer(10); // A consumer with a speed of 10 units per step
    consumers[1] = new Consumer(30);
    consumers[2] = new Consumer(10);
    consumers[3] = new Consumer(10);
    consumers[4] = new Consumer(10);
    consumers[5] = new Consumer(10);
    consumers[6] = new Consumer(10);
    consumers[7] = new Consumer(30);
    consumers[8] = new Consumer(10);
    consumers[9] = new Consumer(10);

    // Build Architecture Tree
    try {
      // Build up tree
      // producer > robinTopScheduler |> robinSubOneScheduler |> consumers[0]
      //                              |                       |> consumers[1]
      //                              |
      //                              |> robinSubTwoScheduler |> consumers[2]
      //                                                      |> consumers[3]
      //                                                      |> consumers[4]
      //                                                      |> consumers[5]
      //                                                      |> consumers[6]
      //                                                      |> consumers[7]
      //                                                      |> consumers[8]
      //                                                      |> consumers[9]
      architecture.addEntity(producer, robinTopScheduler);
      architecture.addEntity(robinTopScheduler, robinSubOneScheduler);
      architecture.addEntity(robinTopScheduler, robinSubTwoScheduler);
      architecture.addEntity(robinSubOneScheduler, consumers[0]);
      architecture.addEntity(robinSubOneScheduler, consumers[1]);
      architecture.addEntity(robinSubTwoScheduler, consumers[2]);
      architecture.addEntity(robinSubTwoScheduler, consumers[3]);
      architecture.addEntity(robinSubTwoScheduler, consumers[4]);
      architecture.addEntity(robinSubTwoScheduler, consumers[5]);
      architecture.addEntity(robinSubTwoScheduler, consumers[6]);
      architecture.addEntity(robinSubTwoScheduler, consumers[7]);
      architecture.addEntity(robinSubTwoScheduler, consumers[8]);
      architecture.addEntity(robinSubTwoScheduler, consumers[9]);
      
    } catch (BadParentException bpe) {
      Log.println(bpe.getMessage());
      Log.println("Parent Id: " + bpe.getParent().getId());
      Log.println("Child Id: " + bpe.getChild().getId());
    }

    sim.addExperiment(architecture);

    // Define what output
    OutputOptions outputOptions = new OutputOptions(); // default is makespan and utilisation
    outputOptions.setCountBins(i); // Display job makespans group on 10 bins of job size
    outputOptions.setRenderSchedule(true); // Render schedule images
    outputOptions.setMakespan(true);
    outputOptions.setUtilisation(true);

    // Run the experiment
    try {
      sim.runExperiments(outputOptions);
    } catch (BadStepsException bse) {
      Log.println(bse.getMessage() + " SimEntity id:" + bse.getSimEntity().getId());
    } catch (BadTaskCompletionException bjce) {
      Log.println(bjce.getMessage() + " Sent:" + bjce.getTasksSent() + " Completed:" + bjce.getTasksCompleted());
    }
    System.out.println("\n");

    }//for
    return true;
  }
  
  private static boolean expr_2(){
    // Create simulator
    for (int x =1; x <=10; x++){
    ScheduleSim sim = new ScheduleSim();

    // Create Producer
    Producer producer = new Producer("Opp");

    // Create Task Pattern
    int start = 20;
    int stop = 250;
    IncrementingPattern pattern = new IncrementingPattern(start, stop);
    
    int taskCount = 60;
    int taskSize = 250;
    FlatPattern flat = new FlatPattern(taskCount, taskSize);
    
    int startSize = 20;
    int endSize = 250;
    double mu = 11;
    double sigma = 4;
    int combinedTargetSize = 5000;
    
    GaussianPattern gaus = new GaussianPattern(startSize,endSize,mu,sigma, combinedTargetSize);

    producer.addMetatask(1, flat);
    producer.addMetatask(10, gaus);
    producer.addMetatask(20,pattern);
    producer.addMetatask(30, flat);
    producer.addMetatask(40, pattern);
    producer.addMetatask(60, gaus);


    // Create Architecture
    Architecture architecture = new Architecture("Min");

    // Create Scheduler
    Opportunistic broker = new Opportunistic();
    MinminScheduler scheduler1 = new MinminScheduler();
    MinminScheduler scheduler2 = new MinminScheduler();
    
    // Create Consumers
    ArrayList<Consumer> consumers = new ArrayList<>();
    for (int i = 60; i > 10; i--) {//create consumers with minimum value of 10 and max 50
        consumers.add(new Consumer(i));//done this to ensure that every test will have the same values for the consumers
      //consumers.add(new Consumer(rand.nextInt(60-10)+10));
    }

    // Build Architecture Tree
    try {
      architecture.addEntity(producer, broker);
      architecture.addEntity(broker, scheduler1);
      architecture.addEntity(broker, scheduler2);
      int c =0;
        while(c < consumers.size()){//equally distribute consumers
            
            architecture.addEntity(scheduler1, consumers.get(c));
            c++;
            
            architecture.addEntity(scheduler2, consumers.get(c));
            c++;
        }
      
    } catch (BadParentException bpe) {
      System.out.println(bpe.getMessage());
      System.out.println("Parent Id: " + bpe.getParent().getId());
      System.out.println("Child Id: " + bpe.getChild().getId());
      return false;
    }

    // Give experiment to ScheduleSim
    sim.addExperiment(architecture);

    // Chose output options
    OutputOptions outputOptions = new OutputOptions(); // default is makespan and utilisation
    outputOptions.setCountBins(x); // Display job makespans group on 10 bins of job size
    outputOptions.setRenderSchedule(true); // Render schedule images
    outputOptions.setMakespan(true);
    outputOptions.setUtilisation(true);

    // Run the experiment
    try {
      sim.runExperiments(outputOptions);
    } catch (BadStepsException bse) {
      Log.println(bse.getMessage() + " SimEntity id:" + bse.getSimEntity().getId());
    } catch (BadTaskCompletionException bjce) {
      Log.println(bjce.getMessage() + " Sent:" + bjce.getTasksSent() + " Completed:" + bjce.getTasksCompleted());
    }
        System.out.println("\n");
    }
    return true;
      
  }
  
  private static boolean expr_3(){
    // Create simulator
    for (int x =1; x <=10; x++){
    ScheduleSim sim = new ScheduleSim();

    // Create Producer
    Producer producer = new Producer("Opp");

    int start = 20;
    int stop = 250;
    IncrementingPattern pattern = new IncrementingPattern(start, stop);
    
    int taskCount = 60;
    int taskSize = 250;
    FlatPattern flat = new FlatPattern(taskCount, taskSize);
    
    int startSize = 20;
    int endSize = 250;
    double mu = 11;
    double sigma = 4;
    int combinedTargetSize = 5000;
    
    GaussianPattern gaus = new GaussianPattern(startSize,endSize,mu,sigma, combinedTargetSize);

    producer.addMetatask(1, flat);
    producer.addMetatask(10, gaus);
    producer.addMetatask(20,pattern);
    producer.addMetatask(30, flat);
    producer.addMetatask(40, pattern);
    producer.addMetatask(60, gaus);


    // Create Architecture
    Architecture architecture = new Architecture("Min_Min");

    // Create Scheduler
    Opportunistic broker = new Opportunistic();
    MinminScheduler scheduler1 = new MinminScheduler();
    MinminScheduler scheduler2 = new MinminScheduler();
    //MaxminFastTrackScheduler scheduler3 = new MaxminFastTrackScheduler();
    
    Random rand = new Random();
    // Create Consumers
    ArrayList<Consumer> consumers = new ArrayList<>();
    for (int i = 60; i >= 10; i-=2) {//create consumers with minimum value of 10 and max 60
        consumers.add(new Consumer(i));//done this to ensure that every test will have the same values for the consumers
    }
    //System.out.println("size is " + consumers.size());
    
    int [] one = {0,1,3,5,6,7,8,9,10,12,14,16,17,21,23,24}; // indexes for picking consumer out of array list
    int [] two = {2,4,11,13,15,18,19,20,22,25};
      
    // Build Architecture Tree
    try {
      architecture.addEntity(producer, broker);
      architecture.addEntity(broker, scheduler1);
      architecture.addEntity(broker, scheduler2);
      //architecture.addEntity(broker, scheduler3);
      for (int i = 0; i <one.length; i++){
          architecture.addEntity(scheduler1,consumers.get(one[i]));
      }
      for (int i =0; i< two.length; i++){
          architecture.addEntity(scheduler2,consumers.get(two[i]));
      }
    
      
    } catch (BadParentException bpe) {
      System.out.println(bpe.getMessage());
      System.out.println("Parent Id: " + bpe.getParent().getId());
      System.out.println("Child Id: " + bpe.getChild().getId());
      return false;
    }

    // Give experiment to ScheduleSim
    sim.addExperiment(architecture);

    // Chose output options
    OutputOptions outputOptions = new OutputOptions(); // default is makespan and utilisation
    outputOptions.setCountBins(x); // Display job makespans group on 10 bins of job size
    outputOptions.setRenderSchedule(true); // Render schedule images
    outputOptions.setMakespan(true);
    outputOptions.setUtilisation(true);

    // Run the experiment
    try {
      sim.runExperiments(outputOptions);
    } catch (BadStepsException bse) {
      Log.println(bse.getMessage() + " SimEntity id:" + bse.getSimEntity().getId());
    } catch (BadTaskCompletionException bjce) {
      Log.println(bjce.getMessage() + " Sent:" + bjce.getTasksSent() + " Completed:" + bjce.getTasksCompleted());
    }
        System.out.println("\n");
    }
    
    return true;
  }
  
  private static boolean dynamicTest(){
    // Create simulator
    ScheduleSim sim = new ScheduleSim();

    // Create Producer
    Producer producer = new Producer("Dynamic Producer");

    // Create Task Pattern
    int start = 2;
    int stop = 250;
    IncrementingPattern pattern = new IncrementingPattern(start, stop);
    //producer.addMetatask(1, pattern);
    //producer.addMetatask(30, pattern);
    //producer.addMetatask(60, pattern);
    
    /*int taskCount = 40;
    int taskSize = 100;
    FlatPattern flat = new FlatPattern(taskCount, taskSize);
    producer.addMetatask(1, flat);
    producer.addMetatask(2, flat);*/
    
    int startSize = 2;
    int endSize = 20;
    double mu = 11;
    double sigma = 4;
    int combinedTargetSize = 5000;
    
    GaussianPattern gaus = new GaussianPattern(startSize,endSize,mu,sigma, combinedTargetSize);
    RandomPattern p = new RandomPattern(2, 50, 10);
    
    int taskCount = 40;
    int taskSize = 255;
    FlatPattern flat = new FlatPattern(taskCount, taskSize);

    //producer.addMetatask(1, gaus);
    //producer.addMetatask(20, gaus);
    producer.addMetatask(1,gaus);
    producer.addMetatask(5, flat);
    producer.addMetatask(10,p);
    producer.addMetatask(15, gaus);
    producer.addMetatask(20, gaus);
    producer.addMetatask(25, p);


    // Create Architecture
    Architecture architecture = new Architecture("Hierachical");

    // Create Scheduler
    Opportunistic broker = new Opportunistic();
    MaxminFastTrackScheduler scheduler1 = new MaxminFastTrackScheduler();
    MaxminFastTrackScheduler scheduler2 = new MaxminFastTrackScheduler();
    MaxminFastTrackScheduler scheduler3 = new MaxminFastTrackScheduler();
    
    Random rand = new Random();
    // Create Consumers
    int d = 0;
    ArrayList<Consumer> consumers = new ArrayList<>();
    for (int i = 60; i > 10; i--) {//create consumers with minimum value of 10 and max 50
        consumers.add(new Consumer(i));//done this to ensure that every test will have the same values for the consumers
      //consumers.add(new Consumer(rand.nextInt(60-10)+10));
    }
    System.out.println(consumers.size());

    // Build Architecture Tree
    try {
      architecture.addEntity(producer, broker);
      architecture.addEntity(broker, scheduler1);
      architecture.addEntity(broker, scheduler2);
      //architecture.addEntity(broker, scheduler3);
      int c =0;
        /*while(c < consumers.size()){//equally distribute consumers
            
            architecture.addEntity(scheduler1, consumers.get(c));
            //consumers.remove(0);
            c++;
            //System.out.println(c);
            if (c >= consumers.size()){break;}
            architecture.addEntity(scheduler2, consumers.get(c));
            //consumers.remove(0);
            c++;
            //System.out.println(c);
            if (c>=consumers.size()){break;}
            architecture.addEntity(scheduler3, consumers.get(c));
            //consumers.remove(0);
            c++;
            //System.out.println(c);
        }*/
      
        //with two schedulers
        while(c < consumers.size()){//equally distribute consumers
            
            architecture.addEntity(scheduler1, consumers.get(c));
            c++;
            
            architecture.addEntity(scheduler2, consumers.get(c));
            c++;
        }
      
    } catch (BadParentException bpe) {
      System.out.println(bpe.getMessage());
      System.out.println("Parent Id: " + bpe.getParent().getId());
      System.out.println("Child Id: " + bpe.getChild().getId());
      return false;
    }

    // Give experiment to ScheduleSim
    sim.addExperiment(architecture);

    // Chose output options
    OutputOptions outputOptions = new OutputOptions(); // default is makespan and utilisation
    outputOptions.setCountBins(10); // Display job makespans group on 10 bins of job size
    outputOptions.setRenderSchedule(true); // Render schedule images
    outputOptions.setMakespan(true);
    outputOptions.setUtilisation(true);

    // Run the experiment
    try {
      sim.runExperiments(outputOptions);
    } catch (BadStepsException bse) {
      Log.println(bse.getMessage() + " SimEntity id:" + bse.getSimEntity().getId());
    } catch (BadTaskCompletionException bjce) {
      Log.println(bjce.getMessage() + " Sent:" + bjce.getTasksSent() + " Completed:" + bjce.getTasksCompleted());
    }

    return true;
  }
  
  private static boolean testHierarchical3(){
          // my algorithm 
          // Create simulation
    ScheduleSim sim = new ScheduleSim();

    // Create Producer, this will dispatch waves of jobs
    Producer producer = new Producer("Mixture");

    // Create Job Pattern(s), this will create the wave of jobs. There several
    int startSize = 2;
    int endSize = 20;
    double mu = 11;
    double sigma = 4;
    int combinedTargetSize = 5000;
    
    GaussianPattern gaus = new GaussianPattern(startSize,endSize,mu,sigma, combinedTargetSize);
    
    int taskCount = 40;
    int taskSize = 100;
    FlatPattern flat = new FlatPattern(taskCount, taskSize);
    
    //incrementing pattern
    
    int start = 2;
    int stop = 250;
    IncrementingPattern pattern = new IncrementingPattern(start, stop);
    
    RandomPattern p = new RandomPattern(2, 50, 10);
    //producer.addMetatask(1, pattern);
    //producer.addMetatask(30, pattern);
    //producer.addMetatask(60, pattern);
    producer.addMetatask(1,gaus);
    producer.addMetatask(2, flat);
    producer.addMetatask(3,p);
    producer.addMetatask(4, gaus);
    //producer.addMetatask(1, pattern);
    // Create Architecture
    Architecture architecture = new Architecture("Hierarchical_opportunistic");
    
    Opportunistic capable = new Opportunistic();
    MaxminScheduler robinSubOneScheduler = new MaxminScheduler();
    MaxminScheduler robinSubTwoScheduler = new MaxminScheduler();

    // Create Consumer(s)
    Consumer[] consumers = new Consumer[10];
    consumers[0] = new Consumer(10); // A consumer with a speed of 30 units per step
    consumers[1] = new Consumer(10);
    consumers[2] = new Consumer(10);
    consumers[3] = new Consumer(20);
    consumers[4] = new Consumer(20);
    consumers[5] = new Consumer(5);
    consumers[6] = new Consumer(10);
    consumers[7] = new Consumer(30);
    consumers[8] = new Consumer(10);
    consumers[9] = new Consumer(10);

    // Build Architecture Tree
    try {
      // Build up tree
      // producer > robinTopScheduler |> robinSubOneScheduler |> consumers[0]
      //                              |                       |> consumers[1]
      //                              |
      //                              |> robinSubTwoScheduler |> consumers[2]
      //                                                      |> consumers[3]
      //                                                      |> consumers[4]
      //                                                      |> consumers[5]
      //                                                      |> consumers[6]
      //                                                      |> consumers[7]
      //                                                      |> consumers[8]
      //                                                      |> consumers[9]
      architecture.addEntity(producer, capable);
      architecture.addEntity(capable, robinSubOneScheduler);
      architecture.addEntity(capable, robinSubTwoScheduler);
      architecture.addEntity(robinSubOneScheduler, consumers[0]);
      architecture.addEntity(robinSubOneScheduler, consumers[1]);
      architecture.addEntity(robinSubOneScheduler, consumers[2]);
      architecture.addEntity(robinSubOneScheduler, consumers[3]);
      architecture.addEntity(robinSubOneScheduler, consumers[4]);
      architecture.addEntity(robinSubTwoScheduler, consumers[5]);
      architecture.addEntity(robinSubTwoScheduler, consumers[6]);
      architecture.addEntity(robinSubTwoScheduler, consumers[7]);
      architecture.addEntity(robinSubTwoScheduler, consumers[8]);
      architecture.addEntity(robinSubOneScheduler, consumers[9]);
      
    } catch (BadParentException bpe) {
      Log.println(bpe.getMessage());
      Log.println("Parent Id: " + bpe.getParent().getId());
      Log.println("Child Id: " + bpe.getChild().getId());
    }

    sim.addExperiment(architecture);

    // Define what output
    OutputOptions outputOptions = new OutputOptions(); // default is makespan and utilisation
    outputOptions.setCountBins(10); // Display job makespans group on 10 bins of job size
    outputOptions.setRenderSchedule(true); // Render schedule images
    outputOptions.setMakespan(true);
    outputOptions.setUtilisation(true);
    

    // Run the experiment
    try {
      sim.runExperiments(outputOptions);
    } catch (BadStepsException bse) {
      Log.println(bse.getMessage() + " SimEntity id:" + bse.getSimEntity().getId());
    } catch (BadTaskCompletionException bjce) {
      Log.println(bjce.getMessage() + " Sent:" + bjce.getTasksSent() + " Completed:" + bjce.getTasksCompleted());
    }
    return true;
  }

    //int c =0;
        /*while(c < consumers.size()){//equally distribute consumers
            
            architecture.addEntity(scheduler1, consumers.get(c));
            //consumers.remove(0);
            c++;
            //System.out.println(c);
            if (c >= consumers.size()){break;}
            architecture.addEntity(scheduler2, consumers.get(c));
            //consumers.remove(0);
            c++;
            //System.out.println(c);
            if (c>=consumers.size()){break;}
            architecture.addEntity(scheduler3, consumers.get(c));
            //consumers.remove(0);
            c++;
            //System.out.println(c);
        }*/
      
        //with two schedulers
//        while(c < consumers.size()){//equally distribute consumers
//            
//            architecture.addEntity(scheduler1, consumers.get(c));
//            c++;
//            
//            architecture.addEntity(scheduler2, consumers.get(c));
//            c++;
//        }
  
}
