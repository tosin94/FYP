package schedulesim;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import schedulesim.scheduler.MaxminFastTrackScheduler;
import schedulesim.scheduler.MaxminScheduler;
import schedulesim.scheduler.MinminScheduler;
import schedulesim.scheduler.RandomScheduler;
import schedulesim.scheduler.RoundRobinScheduler;
import schedulesim.scheduler.ShoppingScheduler;
import schedulesim.scheduler.Opportunistic;
import schedulesim.Consumer;

/**
 *
 * @author paul
 */
public class Tester {

  public static void runTests() {
    boolean result;
    /* Log.println("\nRunning Tests:\n");

    Log.println("Testing ScheduleSim...");
    result = testScheduleSim();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing Task...");
    result = testTask();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing Task Min Sort...");
    result = testTaskMinSort();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing Task Max Sort...");
    result = testTaskMaxSort();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing ConsumingEntity Sort");
    result = testConsumingEntitySort();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing FlatTaskPattern...");
    result = testFlatTaskPattern();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing GaussianTaskPattern...");
    result = testGaussianTaskPattern();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing IncrementingTaskPattern...");
    result = testIncrementingTaskPattern();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing RandomTaskPattern...");
    result = testRandomTaskPattern();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing Producer...");
    result = testProducer();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));


    // Testing each scheduler

    Log.println("Testing Round Robin Scheduler...");
    result = testScheduler(new RoundRobinScheduler(),"RoundRobin");
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing Random Scheduler...");
    result = testScheduler(new RandomScheduler(), "Random");
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing Min-min Scheduler...");
    result = testScheduler(new MinminScheduler(), "Minmin");
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing Max-min Scheduler...");
    result = testScheduler(new MaxminScheduler(), "Maxmin");
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing Max-min Fast Track Scheduler...");
    result = testScheduler(new MaxminFastTrackScheduler(), "MXFT");
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    Log.println("Testing Shopping Scheduler ...");
    result = testScheduler(new ShoppingScheduler(), "Shopping");
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    // Testing multiple metatasks

    Log.println("Testing Multiple Metatasks...");
    result = testRoundRobinMulti();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

    */
    
    // Test hierarchical
    
    /*Log.println("Testing Opportunistic...");
    result = testHierarchical3();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));*/
    
    /*Log.println("Testing Hierarchical........Dyanmic");
    result = dynamicTest();
    Log.println((result ? "SUCCESS\n" : "FAILURE\n"));*/
    
    Log.println("testing EXPR_1");
        result = expr_1();
        Log.println((result ? "SUCCESS\n" : "FAILURE\n"));

  }

  private static boolean testScheduleSim() {
    ScheduleSim sim = new ScheduleSim();
    return ScheduleSim.getSimulationStep() == 0;
  }

  private static boolean testTask() {
    int unitSizeOne = 10;
    int unitSizeTwo = 20;
    Task taskOne = new Task(unitSizeOne);
    Task taskTwo = new Task(unitSizeTwo);
    if (taskOne.getTid() == taskTwo.getTid()
      && taskOne.getStartUnits() != unitSizeOne
      && taskOne.getRemaingUnits() != unitSizeOne
      && taskTwo.getStartUnits() != unitSizeTwo
      && taskTwo.getRemaingUnits() != unitSizeTwo) {
      return false;
    }
    taskOne.decrementUnits(unitSizeOne);
    taskTwo.decrementUnits(unitSizeTwo);
    return taskOne.isFinished() && taskTwo.isFinished()
      && taskOne.getRemaingUnits() == 0
      && taskTwo.getRemaingUnits() == 0
      && taskOne.getStartUnits() == unitSizeOne
      && taskTwo.getStartUnits() == unitSizeTwo;
  }

  private static boolean testTaskMinSort() {
    ArrayList<Task> tasks = new ArrayList<>();

    tasks.add(new Task(12));
    tasks.add(new Task(22));
    tasks.add(new Task(4));
    tasks.add(new Task(11));
    tasks.add(new Task(13));
    tasks.add(new Task(8));
    tasks.add(new Task(1));

    Log.println("Sorting Smallest (Min.) First");
    Collections.sort(tasks, new TaskMinFirstComparator());

    for (Task task : tasks) {
      Log.println(task.getTid() + "," + task.getRemaingUnits() + "u");
    }

    if (tasks.get(6).getRemaingUnits() < tasks.get(0).getRemaingUnits()) {
      return false;
    }

    return true;
  }

  private static boolean testTaskMaxSort() {
    ArrayList<Task> tasks = new ArrayList<>();

    tasks.add(new Task(12));
    tasks.add(new Task(22));
    tasks.add(new Task(4));
    tasks.add(new Task(11));
    tasks.add(new Task(13));
    tasks.add(new Task(8));
    tasks.add(new Task(1));

    Log.println("Sorting Biggest (Max.) First");
    Collections.sort(tasks, new TaskMaxFirstComparator());

    for (Task task : tasks) {
      Log.println(task.getTid() + "," + task.getRemaingUnits() + "u");
    }

    if (tasks.get(6).getRemaingUnits() > tasks.get(0).getRemaingUnits()) {
      return false;
    }

    return true;
  }

  private static boolean testConsumingEntitySort() {
    ArrayList<ConsumingEntity> consumingEntities = new ArrayList<>();

    consumingEntities.add(new Consumer(15));
    consumingEntities.add(new Consumer(10));
    consumingEntities.add(new Consumer(15));
    consumingEntities.add(new Consumer(12));
    consumingEntities.add(new Consumer(5));
    consumingEntities.add(new Consumer(1));
    consumingEntities.add(new Consumer(14));

    Log.println("Sorting Biggest UPS First ");
    Collections.sort(consumingEntities, new ConsumingEntityMinFirstComparator());

    for (ConsumingEntity entity : consumingEntities) {
      Log.println(entity.getId() + "," + entity.getUnitsPerStep() + "ups");
    }

    if (consumingEntities.get(6).getUnitsPerStep() > consumingEntities.get(0).getUnitsPerStep()) {
      return false;
    }
    return true;
  }

  private static boolean testFlatTaskPattern() {
    int taskCount = 8;
    int taskSize = 16;
    FlatPattern flat = new FlatPattern(taskCount, taskSize);
    ArrayList<Task> tasks = flat.generateMetatask();
    Log.println("Flat Tasks");
    for (Task task : tasks) {
      Log.println(task.getTid() + "," + task.getStartUnits());
    }
    return tasks.size() == taskCount
      && tasks.get(0).getStartUnits() == taskSize;
  }

  private static boolean testGaussianTaskPattern() {
    int startSize = 2;
    int endSize = 20;
    double mu = 11;
    double sigma = 4;
    int combinedTargetSize = 5000;
    GaussianPattern gaussian = new GaussianPattern(startSize, endSize, mu, sigma, combinedTargetSize);
    ArrayList<Task> tasks = gaussian.generateMetatask();

    // So we can see the gaussian, count the number of type of task
    int[] counts = new int[endSize - startSize];
    for (int i = 0; i < counts.length; i++) {
      counts[i] = 0;
    }
    for (Task task : tasks) {
      counts[(task.getRemaingUnits() - startSize)]++;
    }
    Log.println("Gaussian Tasks as Distro");
    for (int i = 0; i < counts.length; i++) {
      Log.print("Count of tasks with size " + (i + startSize) + " :");
      String bar = "";
      for (int b = 0; b < counts[i]; b++) {
        bar += "#";
      }
      Log.println(bar);
    }

    return !tasks.isEmpty();
  }

  private static boolean testIncrementingTaskPattern() {
    int start = 2;
    int stop = 10;
    IncrementingPattern increment = new IncrementingPattern(start, stop);
    ArrayList<Task> tasks = increment.generateMetatask();
    Log.println("Incrementing Tasks");
    for (Task task : tasks) {
      Log.println(task.getTid() + "," + task.getStartUnits());
    }
    return tasks.size() == (stop - start);
  }

  private static boolean testRandomTaskPattern() {
    int start = 2;
    int end = 50;
    int count = 10;
    RandomPattern random = new RandomPattern(start, end, count);
    ArrayList<Task> tasks = random.generateMetatask();
    Log.println("Random Tasks");
    for (Task task : tasks) {
      Log.println(task.getTid() + "," + task.getStartUnits());
    }
    return tasks.size() == (count);
  }

  /**
   *  Tests the Producer. This test doesn't use a scheduler.
   */
  private static boolean testProducer() {
    // Create simulator
    ScheduleSim sim = new ScheduleSim();

    // Create Producer
    Producer producer = new Producer("ProducerTest");

    // Create Task Pattern
    int taskCount = 1;
    int taskSize = 1;
    FlatPattern flat = new FlatPattern(taskCount, taskSize);
    producer.addMetatask(1, flat); // Consumer in the first then will have have idle
    producer.addMetatask(4, flat);

    // Create architecture
    Architecture architecture = new Architecture("SingleConsumer");

    // Create Scheduler
    // Not going to bother, going to straight from producer to consumer

    // Create Consumer
    Consumer consumer = new Consumer(1);

    // Build architecture Tree
    try {
      architecture.addEntity(producer, consumer);
    } catch (BadParentException bpe) {
      System.out.println(bpe.getMessage());
      System.out.println("Parent Id: " + bpe.getParent().getId());
      System.out.println("Child Id: " + bpe.getChild().getId());
      return false;
    }

    // Give experiment to ScheduleSim
    sim.addExperiment(architecture);

    // Chose output options
    OutputOptions outputOptions = new OutputOptions();

    // Run the experiment
    try {
      sim.runExperiments(outputOptions);
    } catch (BadStepsException bse) {
      Log.println(bse.getMessage() + " SimEntity id:" + bse.getSimEntity().getId());
    } catch (BadTaskCompletionException bjce) {
      Log.println(bjce.getMessage() + " Sent:" + bjce.getTasksSent() + " Completed:" + bjce.getTasksCompleted());
    }

    // Check the consumer in use half the steps. 0 and 3 but, not 1 and 2
    if (consumer.getTotalUtilisation() != 0.5) {
      return false;
    }

    // Check the simulator finsihed at the right step
    if (ScheduleSim.getSimulationStep() != 4) {
      return false;
    }

    return true;
  }


  /**
   *  Tests a scheduler.
   */
  private static boolean testScheduler(Scheduler scheduler, String schedulerName) {
    // Create simulator
    ScheduleSim sim = new ScheduleSim();

    // Create Producer
    Producer producer = new Producer("Incrementing");

    // Create Task Pattern
    int start = 2;
    int stop = 250;
    IncrementingPattern pattern = new IncrementingPattern(start, stop);
    producer.addMetatask(1, pattern); // One wave of tasks

    // Create Architecture
    Architecture architecture = new Architecture(schedulerName);

    // Create Consumers
    ArrayList<Consumer> consumers = new ArrayList<>();
    for (int i = 0; i < 16; i++) {
      consumers.add(new Consumer(5));
    }
    for (int i = 0; i < 16; i++) {
      consumers.add(new Consumer(2));
    }


    // Build Architecture Tree
    try {
      architecture.addEntity(producer, scheduler);
      for (Consumer consumer : consumers) {
        architecture.addEntity(scheduler, consumer);
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
    OutputOptions outputOptions = new OutputOptions();
    outputOptions.setRenderSchedule(true); // Render schedule images

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


  /**
   *  Tests RoundRobin with multiple metatasks.
   */
  private static boolean testRoundRobinMulti(){
    // Create simulator
    ScheduleSim sim = new ScheduleSim();

    // Create Producer
    Producer producer = new Producer("IncrementingMulti");

    // Create Task Pattern
    int start = 2;
    int stop = 250;
    IncrementingPattern pattern = new IncrementingPattern(start, stop);
    producer.addMetatask(1, pattern);
    producer.addMetatask(30, pattern);
    producer.addMetatask(60, pattern);


    // Create Architecture
    Architecture architecture = new Architecture("RoundRobin");

    // Create Scheduler
    RoundRobinScheduler scheduler = new RoundRobinScheduler();

    // Create Consumers
    ArrayList<Consumer> consumers = new ArrayList<>();
    for (int i = 50; i > 10; i--) {
      consumers.add(new Consumer(i));
    }

    // Build Architecture Tree
    try {
      architecture.addEntity(producer, scheduler);
      for (Consumer consumer : consumers) {
        architecture.addEntity(scheduler, consumer);
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
    OutputOptions outputOptions = new OutputOptions();
    outputOptions.setRenderSchedule(true); // Render schedule images

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


  /**
   *  Tests hierarchical example from the readme.
   */
  private static boolean testHierarchical() {
    // Create simulation
    // expr 1
    ScheduleSim sim = new ScheduleSim();

    // Create Producer, this will dispatch waves of jobs
    Producer producer = new Producer("TwoFlatMetatasks");

    // Create Job Pattern(s), this will create the wave of jobs. There several
    int taskCount = 20;
    int taskSize = 16;
    FlatPattern flat = new FlatPattern(taskCount, taskSize);
    
    //incrementing pattern
    /* int start = 40;
    int end = 100;
    IncrementingPattern incrementing = new IncrementingPattern(start,end);
    */
    
    producer.addMetatask(1,flat);
    producer.addMetatask(10,flat);

    
    // Create Architecture
    Architecture architecture = new Architecture("Hierarchical_RoundRobin");

    // Create Scheduler(s)
    RoundRobinScheduler robinTopScheduler = new RoundRobinScheduler();
    RoundRobinScheduler robinSubOneScheduler = new RoundRobinScheduler();
    RoundRobinScheduler robinSubTwoScheduler = new RoundRobinScheduler();

    // Create Consumer(s)
    Consumer[] consumers = new Consumer[10];
    consumers[0] = new Consumer(10); // A consumer with a speed of 30 units per step
    consumers[1] = new Consumer(20);
    consumers[2] = new Consumer(10);
    consumers[3] = new Consumer(10);
    consumers[4] = new Consumer(10);
    consumers[5] = new Consumer(10);
    consumers[6] = new Consumer(10);
    consumers[7] = new Consumer(20);
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
    outputOptions.setCountBins(10); // Display job makespans group on 10 bins of job size
    outputOptions.setRenderSchedule(true); // Render schedule images

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
    int taskSize = 100;
    FlatPattern flat = new FlatPattern(taskCount, taskSize);

    //producer.addMetatask(1, gaus);
    //producer.addMetatask(20, gaus);
    producer.addMetatask(1,gaus);
    producer.addMetatask(2, flat);
    producer.addMetatask(3,p);
    producer.addMetatask(4, gaus);

    // Create Architecture
    Architecture architecture = new Architecture("Hierachical");

    // Create Scheduler
    Opportunistic broker = new Opportunistic();
    MaxminFastTrackScheduler scheduler1 = new MaxminFastTrackScheduler();
    MaxminFastTrackScheduler scheduler2 = new MaxminFastTrackScheduler();
    
    Random rand = new Random();
    // Create Consumers
    int d = 0;
    ArrayList<Consumer> consumers = new ArrayList<>();
    for (int i = 60; i > 10; i--) {//create consumers with minimum value of 10 and max 50
      consumers.add(new Consumer(rand.nextInt(60-10)+10));
    }

    // Build Architecture Tree
    try {
      architecture.addEntity(producer, broker);
      architecture.addEntity(broker, scheduler1);
      architecture.addEntity(broker, scheduler2);
      int index = -1;
      
      for (int c =0; c < consumers.size(); c+=2) {//equally distribute consumers
          index ++;
        architecture.addEntity(scheduler1, consumers.get(index % consumers.size()));
        index++;
        architecture.addEntity(scheduler2, consumers.get(index % consumers.size()));
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
    OutputOptions outputOptions = new OutputOptions();
    outputOptions.setRenderSchedule(true); // Render schedule images

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
  
    private static boolean expr_1() {
    // Create simulation
    // expr 1
    ScheduleSim sim = new ScheduleSim();

    // Create Producer, this will dispatch waves of jobs
    Producer producer = new Producer("TwoGaussian");

    // Create Job Pattern(s), this will create the wave of jobs. There several
    int taskCount = 20;
    int taskSize = 16;
    FlatPattern flat = new FlatPattern(taskCount, taskSize);
    
    int startSize = 2;
    int endSize = 20;
    double mu = 11;
    double sigma = 4;
    int combinedTargetSize = 5000;
    
    GaussianPattern gaus = new GaussianPattern(startSize,endSize,mu,sigma, combinedTargetSize);
    
    //incrementing pattern
    /* int start = 40;
    int end = 100;
    IncrementingPattern incrementing = new IncrementingPattern(start,end);
    */
    
    producer.addMetatask(1,gaus);
    producer.addMetatask(10,gaus);

    
    // Create Architecture
    Architecture architecture = new Architecture("Hierarchical_RoundRobin");

    // Create Scheduler(s)
    RoundRobinScheduler robinTopScheduler = new RoundRobinScheduler();
    RoundRobinScheduler robinSubOneScheduler = new RoundRobinScheduler();
    RoundRobinScheduler robinSubTwoScheduler = new RoundRobinScheduler();

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

}
