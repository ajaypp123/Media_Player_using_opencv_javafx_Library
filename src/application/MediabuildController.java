package application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer.Status;


public class MediabuildController implements Initializable {
	
	private Stage stage;
	
	@FXML private MediaView mv ;
	private MediaPlayer mp ;
	private Media me ;
	@FXML Slider vol = new Slider();
	@FXML Slider time=new Slider();
	@FXML MenuItem op;
	@FXML public MenuBar mb ;
	@FXML Label vl = new Label();
	@FXML Label t2 = new Label();
	@FXML Label t3 = new Label();

	
	String plist = "";
	int numlist = -1;
	String[] listp ;
	
	double r=0,s=0.99;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		web thread = new web();
		thread.setDaemon(true);
	    thread.start();
	
		String path1 = new File("src/media/welcome.mp4").getAbsolutePath();
		 
		me = new Media(new File(path1).toURI().toString());
		mp = new MediaPlayer(me);
	    mv.setMediaPlayer(mp); 
		mp.setAutoPlay(false);
		
		mp.currentTimeProperty().addListener(new InvalidationListener()
		{
			public void invalidated(Observable ov)
			{
				updateValues();
			}					
		});
		 
		op.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent e)
			{
			try{
				mp.pause();
				FileChooser fileChooser = new FileChooser();
				File file = fileChooser.showOpenDialog(stage);
				String path =  file.getAbsolutePath();
				stage.setTitle("Media : " + file.getName() );
				path = path.replace("\\", "/");
				if(file!=null)
				{
					try
					{
						me = new Media(new File(path).toURI().toString());
						mp.stop();
						mp = new MediaPlayer(me);
						mp.setAutoPlay(true);
						mv.setMediaPlayer(mp);
					}
					catch(Exception e1)
					{
						e1.printStackTrace();
					}
				}
				
				mp.currentTimeProperty().addListener(new InvalidationListener()
				{
					public void invalidated(Observable ov)
					{
						updateValues();
					}					
				});
				}catch(Exception ex)
			{}
			}
		});
		
		 mv.setOnDragOver(dragEvent -> {
			 Dragboard db = dragEvent.getDragboard();
			 if (db.hasFiles() || db.hasUrl()) {
			 dragEvent.acceptTransferModes(TransferMode.LINK);
			 } else {
			 dragEvent.consume();
			 }
			 });
		 
		 mv.setOnDragDropped(dragEvent -> {
			 Dragboard db = dragEvent.getDragboard();
			 String l = null;
			 try {
				l = db.getFiles().get(0).toURI().toURL().toString();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			 stage.setTitle("Media : " + db.getFiles() );

			 if(l!=null)
				{
				 try
					{
						me = new Media(l);
						mp.stop();
						mp = new MediaPlayer(me);
						mp.setAutoPlay(true);
						mv.setMediaPlayer(mp);
					}
					catch(Exception e1)
					{
						e1.printStackTrace();
					}
				}
			 
			 mp.currentTimeProperty().addListener(new InvalidationListener()
				{
					public void invalidated(Observable ov)
					{
						
						updateValues();
					}					
				});
			 
		 });
	 
		 DoubleProperty width = mv.fitWidthProperty();
		 DoubleProperty height = mv.fitHeightProperty();
		 
		 width.bind(Bindings.selectDouble(mv.sceneProperty(),"width"));
		 height.bind(Bindings.selectDouble(mv.sceneProperty(),"height"));
		 
		 //------------volumeslider-----------------------
		 
		 vol.valueProperty().addListener(new InvalidationListener() {
			    public void invalidated(Observable ov) {
			       if (vol.isValueChanging()) {
			           mp.setVolume(vol.getValue() / 100.0);
			       }
			       vl.setText("%" + vol.getValue());
			    }
			});
		 
		//------------timeslider-----------------------
		 
		 time.valueProperty().addListener(new InvalidationListener()
			{
				public void invalidated(Observable ov)
				{
					if(time.isPressed())
					{
						mp.seek(mp.getMedia().getDuration().multiply(time.getValue()/100));
					}
				}
			});
		//------------opacity-----------------------
		 
}
	
	public void init(Stage stage) {
		this.stage = stage ;	
	}
	
	public void close(ActionEvent event){
		
			Platform.exit();
			System.exit(0);
	}
	
	public void play(ActionEvent event){
		
		Status status = mp.getStatus();
		
		if(status==Status.PLAYING)
		{	
			mp.pause();
		}
		
		if(status==Status.PAUSED)
		{
			mp.play();
		}

	}
	
	public void fast(ActionEvent event){
		r = r + 1.5;
		mp.setRate(r);
	}
	
	public void slow(ActionEvent event){
		s=s*0.8;
		mp.setRate(s);
	}
	
	public void reload(ActionEvent event){
		mp.seek(mp.getStartTime());
		mp.play();
	}	
	
	public void normal(ActionEvent event){
		mp.setRate(1);
	}
	
	public void backward(ActionEvent event){
		mp.seek(mp.getCurrentTime().divide(2.5));
	}

	public void forward(ActionEvent event){
		mp.seek(mp.getCurrentTime().multiply(2.5));
	}

	public void help(ActionEvent event){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Help");
		alert.setHeaderText("Welcome to Media Player Help");
		alert.setContentText("Documentation:\n||	: Play/Pause Button\nN		: Normal Speed\n<<<	: Previous media\n<<	: Slow Forward\nR		: Reload\n>>	: Fast Forward\n>>>	: Next Media");
		
		alert.showAndWait();
	}

	public void about(ActionEvent event){

		Alert alert1 = new Alert(AlertType.INFORMATION);
		alert1.setTitle("About");
		alert1.setHeaderText("Ajay Media Player Help");
		alert1.setContentText("Ajay media player is an open source media player in which you can play any type of media files. ");
		
		alert1.showAndWait();
	}

	
	public void stop(ActionEvent event){
		mp.stop();

	}

	public void next(ActionEvent event){
			nextsong();
	}

	public void privios(ActionEvent event){
			previoussong();
	}

	public void playlist(ActionEvent event){
		mp.pause();
		DirectoryChooser directoryChooser = new DirectoryChooser(); 
		File file = directoryChooser.showDialog(null);
		if(file!=null){
			plist = file.getPath();
			plist = plist.replace("\\", "/");
			plist = plist + "/" ;
			listp = file.list();
		}
	}

	protected void updateValues()
	{
		Platform.runLater(new Runnable()
				{
					public void run()
					{
						int emin=(int) (mp.getTotalDuration().toMinutes());
						int ssec=(int) (mp.getTotalDuration().toSeconds());
						int tsec = ((ssec) - (emin*60));
						
						int cmin = (int) (mp.getCurrentTime().toMinutes());					
						int csec = (int) (mp.getCurrentTime().toSeconds());
						int ctime = ((csec) - (cmin*60));
						
						csec = csec - 3 ;
						
						if(ssec == csec)
						{
							nextsong();
							
						}
								
						t2.setText(""+emin+":"+tsec);
						t3.setText(""+cmin+":"+ctime);
						time.setValue(mp.getCurrentTime().toMillis()/mp.getTotalDuration().toMillis()*100);
						cam();
					}
				});
	}
	
	protected void cam()
	{
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
								  
							
						sumred = sumred/(640*480) ;		
						sumblue = sumblue/(640*480) ;	
						sumgreen = sumgreen/(640*480) ;
					
					  if (sumred>180 && sumred>sumgreen && sumred>sumblue)
					  {						  
						  Status status = mp.getStatus();
						 if(status==Status.PLAYING)
							{	
								mp.pause();
								cam2();
							}
					  }
					  
					  if (sumred<20 && sumgreen<20 && sumblue<20)
					  {						  	
							 Platform.exit();
								System.exit(0);
					  }
					  
					  if (sumgreen>180 && sumgreen>sumred && sumgreen>sumblue)
					  {
						  nextsong();
					  }
					  
					  if (sumblue>180 && sumblue>sumgreen && sumblue>sumred)
					  {
						  previoussong();
					  }
		}catch(Exception e)
		{
			//System.out.println(e);
		}
	}
	
	public void cam2()
	{
		int clr, red, green, blue, sumred=0, sumgreen=0, sumblue=0;
		
		System.out.println("cam2");
		
		while(true)
		{
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
				  
					if (sumred<20 && sumgreen<20 && sumblue<20)
					  {						  	
							 Platform.exit();
							System.exit(0);
					  }
					  
					  if (sumgreen>150 && sumgreen>sumred && sumgreen>sumblue)
					  {
						  mp.play();
						  Thread.sleep(1000);
						  break;

					  }
					  
					  //cam2();
			}catch(Exception e)
			{
				//cam2();
			}
		}
	}
	
	
	protected void nextsong()
	{
		numlist ++;
		if(numlist == listp.length)
		{
			numlist = 0 ;
		}
		stage.setTitle("Media : " + listp[numlist] );
		try
		{
			me = new Media(new File(plist + listp[numlist]).toURI().toString());
			mp.stop();
			mp = new MediaPlayer(me);
			mp.setAutoPlay(true);
			mv.setMediaPlayer(mp);
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		
		mp.currentTimeProperty().addListener(new InvalidationListener()
		{
			public void invalidated(Observable ov)
			{
				updateValues();
			}					
		});
	}
	
	protected void previoussong()
	{
		numlist --;
		if(numlist == -1)
		{
			numlist = 0 ;
		}
		stage.setTitle("Media : " + listp[numlist] );
		try
		{
			me = new Media(new File(plist + listp[numlist]).toURI().toString());
			mp.stop();
			mp = new MediaPlayer(me);
			mp.setAutoPlay(true);
			mv.setMediaPlayer(mp);
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
		
		mp.currentTimeProperty().addListener(new InvalidationListener()
		{
			public void invalidated(Observable ov)
			{
				updateValues();
			}					
		});
	}
}
