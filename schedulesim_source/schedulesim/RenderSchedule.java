package schedulesim;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;

/**
 *
 * @author paul
 */
public class RenderSchedule {

  private ArrayList<Consumer> consumers;

  public RenderSchedule(ArrayList<Consumer> consumers) {
    this.consumers = consumers;
  }

  public void renderImageToFile(String fileName) throws IOException, BadRenderException{

    int margin = 4;

    // Work out how the hight of the image
    int height = (consumers.size() * 2) + (margin*2);

    // Work out width
    int biggestConsumerUPS = -1;
    for (Consumer consumer : consumers) {
      if (biggestConsumerUPS < consumer.getUnitsPerStep()) {
        biggestConsumerUPS = consumer.getUnitsPerStep();
      }
    }
    int scaleStartPos = biggestConsumerUPS + margin + 1;
    int width = (ScheduleSim.getSimulationStep()*2) + scaleStartPos + margin;

    // Create image to draw on
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    // Prepare to draw on image
    Graphics2D g = image.createGraphics();

    // White backgorund
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, width, height);

    // Draw scale across top
    g.setColor(Color.BLACK);
    for(int i = 0; i < width; i=i+10){
      g.fillRect(scaleStartPos + (i*2) + 1 , margin, 1, 1);
    }
    scaleStartPos = biggestConsumerUPS + margin + 1;

    // Check task size are between 1-255
    for(Consumer consumer : consumers ){
      for(Task task : consumer.getCompletedTasks()){
        if(task.getStartUnits()>0 && task.getStartUnits()<256){
          continue;
        } else{
          throw new BadRenderException("Task Size are to large for shading. Bigger than 255.");
        }
      }
    }

    // Draw consumers with their tasks
    int consumerPos = margin + 2;

    Collections.sort(consumers, new ConsumingEntityMinFirstComparator());

    for(Consumer consumer : consumers){

      // Draw bar to represent consumer UPS
      g.setColor(Color.BLACK);
      g.drawLine(margin, consumerPos, margin + consumer.getUnitsPerStep(), consumerPos);

      // Draw tasks
      for(Task task : consumer.getCompletedTasks()){
        g.setColor(Color.CYAN);
        g.fillRect(((task.getStepProcessingStarted()*2)-1) + scaleStartPos, consumerPos, 1, 1);
        g.setColor(new Color(255-task.getStartUnits(),255-task.getStartUnits(),255-task.getStartUnits()));
        g.drawLine((task.getStepProcessingStarted() *2) + scaleStartPos, consumerPos, (task.getStepFinished()*2) + scaleStartPos, consumerPos);
      }

      consumerPos += 2;
    }

    //Write image
    File outputfile = new File(fileName + ".png");
    ImageIO.write(image, "png", outputfile);

  }
}
