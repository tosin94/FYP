# README

ScheduleSim is an open source batch mode vector bin packing simulator implemented in Java.

ScheduleSim's design is focused towards generically simulating batch mode vector bin packing. The simulator uses generic terminology (Producer, Tasks, Units, Steps, Schedulers and Consumers) this prevents association of ScheduleSim to a specific context.

The simulator operates using discrete time steps opposed to a event driven design. This allows fine gain control and simplifies design.

Outside of scope is modelling Scheduler processing time. Schedulers are allowed to do as much processing as they wish without time advancing. Furthermore, network latency is is not modelled. This means a Task can fully transverse the network from Producer, through any number of Schedulers to a Consumer in a single Step.

ScheduleSim simulates a single Producer makes simulation inherently centralised. ScheduleSim is however capable of modelling hierarchical tree structures with the limitation that Schedulers and Consumers can only have one parent.

Scheduler and Consumer have buffers in which they store tasks waiting to be processed. Consumers can only activity process one Task at a time.

Task are measured in Units, the higher Units the larger the Task. Consumer are rated with a Units Per Step, this is how many units they can decrement per step from the Task they are activity processing.

Producer can submit a wave (meta-task) of tasks at any time step. The Tasks in wave of Task can be defined using a MetataskPattern. These specify number and sizes on Task in the wave.

Through keeping the features low and the implementation simple and verbose, ScheduleSim aims to be easy modify and adapt and even easy to use. To create a ScheduleSim simulation simply build the jar and reference the ``.jar" in your experiment.

## To build:

Make sure you have ant and the java jdk 7 upwards. Clone this repository and in the project root directory run:

        ant

The jar file should appear in the dist directory.

## To Run

The jar is intended to be used as a library. Running it as seen below runs a self test.

        java -jar ScheduleSim.jar

## To Use as Library

Reference the jar file.

In NetBeans for example, Create a new project, right click project, click properties, click libraries, click add jar and browser to SheduleSim jar.

## Writing an Experiment for ScheduleSim

Below is an example of a ScheduleExperiment. Which should work when referencing the ScheduleSim jar as mentioned above.

        package exampleexperiment;
        
        import schedulesim.Archictecture;
        import schedulesim.BadTaskCompletionException;
        import schedulesim.BadParentException;
        import schedulesim.BadStepsException;
        import schedulesim.Consumer;
        import schedulesim.IncrementingPattern;
        import schedulesim.Log;
        import schedulesim.OutputOptions;
        import schedulesim.Producer;
        import schedulesim.ScheduleSim;
        import schedulesim.scheduler.RoundRobinScheduler;
        
        public class ExampleExperiment {
          public static void main(String[] args) {
            // Create simulation environment
            ScheduleSim simulator = new ScheduleSim();
        
            // Create Producer, this will dispatch waves of tasks
            Producer producer = new Producer("TwoIncrementingMetatasks");
        
            // Create Task Pattern(s), this will create the wave of tasks. There several
            int start = 40;
            int end = 240;
            IncrementingPattern incrementing = new IncrementingPattern(start,end);
            producer.addMetatask(1, incrementing);
            producer.addMetatask(120, incrementing);
        
            // Create Architecture
            Architecture architecture = new Architecture("MyThreeRoundRobinScheduling");
        
            // Create Scheduler(s)
            RoundRobinScheduler robinTopScheduler = new RoundRobinScheduler();
            RoundRobinScheduler robinSubOneScheduler = new RoundRobinScheduler();
            RoundRobinScheduler robinSubTwoScheduler = new RoundRobinScheduler();
            
            // Create Consumer(s)
            Consumer[] consumers = new Consumer[10];
            consumers[0] = new Consumer(30); // A consumer with a speed of 30 units per step
            consumers[1] = new Consumer(30);
            consumers[2] = new Consumer(20);
            consumers[3] = new Consumer(20);
            consumers[4] = new Consumer(16);
            consumers[5] = new Consumer(16);
            consumers[6] = new Consumer(10);
            consumers[7] = new Consumer(10);
            consumers[8] = new Consumer(8);
            consumers[9] = new Consumer(8);
            
        
            // Build Archictecture Tree
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
              archictecture.addEntity(producer, robinTopScheduler);
              archictecture.addEntity(robinTopScheduler, robinSubOneScheduler);
              archictecture.addEntity(robinTopScheduler, robinSubTwoScheduler);
              archictecture.addEntity(robinSubOneScheduler, consumers[0]);
              archictecture.addEntity(robinSubOneScheduler, consumers[1]);
              archictecture.addEntity(robinSubTwoScheduler, consumers[2]);
              archictecture.addEntity(robinSubTwoScheduler, consumers[3]);
              archictecture.addEntity(robinSubTwoScheduler, consumers[4]);
              archictecture.addEntity(robinSubTwoScheduler, consumers[5]);
              archictecture.addEntity(robinSubTwoScheduler, consumers[6]);
              archictecture.addEntity(robinSubTwoScheduler, consumers[7]);
              archictecture.addEntity(robinSubTwoScheduler, consumers[8]);
              archictecture.addEntity(robinSubTwoScheduler, consumers[9]);
            } catch (BadParentException bpe) {
              Log.println(bpe.getMessage());
              Log.println("Parent Id: " + bpe.getParent().getId());
              Log.println("Child Id: " + bpe.getChild().getId());
            }
            
            simulator.addExperiment(architecture);
            
            // Define what output
            OutputOptions outputOptions = new OutputOptions(); // default is makespan and utilisation
            outputOptions.setCountBins(10); // Display task makespans group on 10 bins of task size
            outputOptions.setRenderSchedule(true); // Render schedule images
            
            // Run the experiment
            try {
              simulator.runExperiments(outputOptions);
            } catch (BadStepsException bse) {
              Log.println(bse.getMessage() + " SimEntity id:" + bse.getSimEntity().getId());
            } catch (BadTaskCompletionException bjce) {
              Log.println(bjce.getMessage() + " Sent:" + bjce.getTasksSent() + " Completed:" + bjce.getTasksCompleted());
            }
          }  
        }


If you have any problems, questions, comments or advice to improve this application please email paulmogs398@gmail.com  many thanks. Paul
