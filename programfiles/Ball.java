import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D.Double;
import java.awt.event.*;
public class Ball
{
  private Rectangle m_bounds;
  private double m_speed, m_delay, distanceMovedX, distanceMovedY, dh, vx, vy;
  private long m_lastTime, elapsedTime;
  private Double m_direction, m_velocity;
  private boolean superStrength, sticky;
  public Ball(int x, int y, int d)
  {
    m_bounds=new Rectangle(x, y, d, d);
    superStrength=false;
    sticky=true;
    m_speed=100;
    m_delay=100;
    m_direction=new Double(0, 0);
    calculateVelocity();
  }
  public void draw(Graphics g)
  {
    g.drawOval(m_bounds.x, m_bounds.y, m_bounds.width, m_bounds.height);
    g.fillOval(m_bounds.x, m_bounds.y, m_bounds.width, m_bounds.height);
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
  public Rectangle getBounds()
  {
    return m_bounds;
  }
  public void setPosition(int x, int y)
  {
    m_bounds.x=x;
    m_bounds.y=y;
    calculateVelocity();
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
  public void collideX(int x)
  {
    setPosition(x, m_bounds.y);
    setDirection(-m_direction.x, m_direction.y);
  }
  public void collideY(int y)
  {
    setPosition(m_bounds.x, y);
    setDirection(m_direction.x, -m_direction.y);
  }
  public void strengthen()
  {
    superStrength=true;
  }
  public boolean getStrength()
  {
    return superStrength;
  }
  public void decelerate()
  {
    if(m_speed>=11)
    {
      m_speed-=10;
    }
    else if(m_speed<11)
    {
      m_speed=1;
    }
    calculateVelocity();
  }
  public void accelerate()
  {
    m_speed+=10;
    calculateVelocity();
  }
  public double getDirectionX()
  {
    return m_direction.x;
  }
  public double getDirectionY()
  {
    return m_direction.y;
  }
  public void navigate(Paddle paddle)
  {
    setDirection(paddle.getDirectionX(), paddle.getDirectionY());
    m_speed=paddle.getSpeed();
    calculateVelocity();
  }
  public void halt()
  {
    setDirection(0, 0);
  }
  public void release(double speed)
  {
    sticky=false;
    setSpeed(speed);
    int positiveNegative=(int)(Math.random()*2);
    double xDirection=Math.pow(1, positiveNegative);
    setDirection(xDirection, -1);
  }
  public boolean getSticky()
  {
    return sticky;
  }
  public void setSpeed(double speed)
  {
    m_speed=speed;
    calculateVelocity();
  }
}