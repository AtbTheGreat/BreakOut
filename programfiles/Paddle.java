import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D.Double;
import java.awt.event.*;
public class Paddle
{
  private Rectangle m_bounds;
  private double m_speed, m_delay, distanceMovedX, distanceMovedY, dh, vx, vy;
  private long m_lastTime, elapsedTime;
  private Double m_direction, m_velocity;
  private int initialWidth;
  public Paddle(int x, int y, int width, int height)
  {
    m_bounds=new Rectangle(x, y, width, height);
    initialWidth=m_bounds.width;
    m_direction=new Double(0, 0);
    m_delay=100;
    m_speed=150;
    calculateVelocity();
  }
  public void draw(Graphics g)
  {
    g.drawRect(m_bounds.x, m_bounds.y, m_bounds.width, m_bounds.height);
    g.fillRect(m_bounds.x, m_bounds.y, m_bounds.width, m_bounds.height);
  }
  public Rectangle getBounds()
  {
    return m_bounds;
  }
  public void animate(long currentTime)
  {
    if(m_lastTime==0)
    {
      m_lastTime=currentTime;
    }
    elapsedTime=(currentTime-m_lastTime);
    if(elapsedTime>m_delay)
    {
      int distanceMovedX=(int)(m_velocity.x*(double)(elapsedTime)/1000);
      int distanceMovedY=(int)(m_velocity.y*(double)(elapsedTime)/1000);
      m_bounds.y+=distanceMovedY;
      m_bounds.x+=distanceMovedX;
      m_lastTime=currentTime;
    }
  }
  public void setPosition(int x, int y)
  {
    m_bounds.x=x;
    m_bounds.y=y;
  }
  public void calculateVelocity()
  {
    dh=Math.pow((Math.pow(m_direction.x, 2)+Math.pow(m_direction.y, 2)), .5);
    vx=m_speed*(double)m_direction.x/dh;
    vy=m_speed*(double)m_direction.y/dh;
    
    m_velocity = new Double(vx, vy);
  }
  public void setDirection(double x, double y)
  {
    m_direction.x=x;
    m_direction.y=y;
    calculateVelocity();
  }
  /*public boolean bounceCheck(int x, int y, int diameter)
   {
   boolean bounce=false;
   if(x<=m_bounds.x || (y+diameter)>=m_bounds.y || (x+diameter)>(m_bounds.x+m_bounds.width))
   {
   bounce=true;
   }
   return bounce;
   }*/
  public void halt()
  {
    setDirection(0, 0);
  }
  public void navigate(int keyCode, Rectangle gameFieldBounds)
  {
    if(m_bounds.x>=gameFieldBounds.x && (m_bounds.x+m_bounds.width)<=(gameFieldBounds.x+gameFieldBounds.width))
    {
      if(keyCode==KeyEvent.VK_LEFT)
      {
        setDirection(-1, 0);
      }
      else if(keyCode==KeyEvent.VK_RIGHT)
      {
        setDirection(1, 0);
      }
    }
    else if(m_bounds.x<gameFieldBounds.x)
    {
      m_bounds.x=gameFieldBounds.x;
      halt();
    }
    else if((m_bounds.x+m_bounds.width)>(gameFieldBounds.x+gameFieldBounds.width))
    {
      m_bounds.x=gameFieldBounds.x+gameFieldBounds.width-m_bounds.width;
      halt();
    }
  }
  public double getSpeed()
  {
    return m_speed;
  }
  public void expand(Rectangle gameFieldBounds)
  {
    int leftDistance=m_bounds.x-gameFieldBounds.x;
    int rightDistance=gameFieldBounds.x+gameFieldBounds.width-initialWidth;
    if(m_bounds.width+initialWidth<gameFieldBounds.width)
    {
      if(leftDistance<initialWidth && rightDistance>=initialWidth)
      {
        m_bounds.width+=initialWidth;
      }
      else if(leftDistance>=initialWidth && rightDistance>=initialWidth)
      {
        m_bounds.x-=initialWidth/2;
        m_bounds.width+=initialWidth/2;
      }
      else if(leftDistance>=initialWidth && rightDistance<initialWidth)
      {
        m_bounds.x-=initialWidth;
      }
      m_bounds=new Rectangle(m_bounds.x, m_bounds.y, m_bounds.width, m_bounds.height);
    }
  }
  public void contract(Rectangle gameFieldBounds)
  {
    int leftDistance=m_bounds.x-gameFieldBounds.x;
    int rightDistance=gameFieldBounds.x+gameFieldBounds.width-initialWidth;
    if(m_bounds.width>initialWidth)
    {
      if(leftDistance<initialWidth && rightDistance>=initialWidth)
      {
        m_bounds.width-=initialWidth;
      }
      else if(leftDistance>=initialWidth && rightDistance>=initialWidth)
      {
        m_bounds.x+=initialWidth/2;
        m_bounds.width-=initialWidth/2;
      }
      else if(leftDistance>=initialWidth && rightDistance<initialWidth)
      {
        m_bounds.x+=initialWidth;
      }
      m_bounds=new Rectangle(m_bounds.x, m_bounds.y, m_bounds.width, m_bounds.height);
    }
  }
  public double getDirectionX()
  {
    return m_direction.x;
  }
  public double getDirectionY()
  {
    return m_direction.y;
  }
  public void collideX(int x)
  {
    setPosition(x, m_bounds.y);
    halt();
  }
  public void collideY(int y)
  {
    setPosition(m_bounds.x, y);
    halt();
  }
}