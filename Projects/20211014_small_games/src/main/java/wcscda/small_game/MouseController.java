package wcscda.small_game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.ImageObserver;

<<<<<<< HEAD
import javax.swing.SwingUtilities;

public class MouseController extends MouseAdapter {
    
	private final Board board;
=======
public class MouseController extends MouseAdapter {
    private final Board board;
>>>>>>> aenori_main

    public MouseController(Board board) {
        this.board = board;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
<<<<<<< HEAD
    	
=======
>>>>>>> aenori_main
        board.redraw(new Drawable(){
            @Override
            public void draw(Graphics2D g, ImageObserver io) {
                int x = mouseEvent.getX();
                int y = mouseEvent.getY();
<<<<<<< HEAD
                g.setColor(Color.WHITE);
                
          
            	if(x!=0 ) {
            		
            	}
                    g.drawLine(x - 10, y - 10, x + 10, y +10);
                    g.drawLine(x + 10, y - 10, x - 10, y +10);
                   
                    g.drawString(" x= "+mouseEvent.getX()+" y= "+mouseEvent.getY(), x, y);
                   
            	
            		g.drawOval(15, 15, 15, 15);
            		g.drawString("x = "+mouseEvent.getX()+"y= "+mouseEvent.getY(), x, y);
            	
           

//                g.setColor(Color.WHITE);
//                g.drawLine(x - 100, y - 100, x + 110, y +100);
//                g.drawLine(x + 100, y - 100, x - 110, y +100);
                
            }
            
        } );
    }

    @Override
     
    public void mousePressed (MouseEvent e)
   
    {
       
    }
=======

                g.setColor(Color.WHITE);
                g.drawLine(x - 30, y - 30, x + 30, y +30);
                g.drawLine(x + 30, y - 30, x - 30, y +30);
            }
        } );
    }

    /*@Override
    public void mousePressed(MouseEvent mouseEvent) {

    }*/
>>>>>>> aenori_main
}
