package application;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class web extends Thread 
{
	 public void run() 
	 {
		 try{
				
				
				
				System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
				VideoCapture camera = new VideoCapture(0);
				if(!camera.isOpened())
				{
					System.out.println("Error");
				}
				else
				{
					Mat frame = new Mat();
					while(true)
					{
						camera.read(frame);
							Highgui.imwrite("action.jpg", frame);
							
							int clr, red, green, blue, sumred=0, sumgreen=0, sumblue=0;
							
							try{
								File file= new File("action.jpg");
								BufferedImage image = ImageIO.read(file);
								  
								  int height = image.getHeight();
								  int width = image.getWidth();
								  for(int i=0; i < width ; i++)
									{
										    for(int j=0; j < height ; j++)
										      {
										    	clr=  image.getRGB(i,j); 
												  red   = (clr & 0x00ff0000) >> 16;
												  green = (clr & 0x0000ff00) >> 8;
												  blue  =  clr & 0x000000ff;
												  sumred = red + sumred ;
												  sumgreen = green + sumgreen ;
												  sumblue = blue + sumblue ;
												  
											      }
										}
												  
											
										sumred = sumred/(height * width) ;		
										sumblue = sumblue/(height * width) ;	
										sumgreen = sumgreen/(height * width) ;
										
									  if (sumred<20 && sumblue<20 && sumgreen<20)
									  {						  
										  	break;
									  }
							}catch(Exception e)
							{
								//System.out.println("Exeption");
							}
							///////////////////////////////////////////////////////////////
						
						}
				}
				 	  Thread.sleep(1000);
					File file = new File("action.jpg");
				  file.delete();
				camera.release();
				}catch(Exception e){}
         
     }
}
