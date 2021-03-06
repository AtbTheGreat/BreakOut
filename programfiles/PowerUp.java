import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Point2D.Double;
import java.awt.Image;
import javax.swing.ImageIcon;
public class PowerUp
{
  private Rectangle m_bounds;
  private double m_speed, m_delay, distanceMovedX, distanceMovedY, dh, vx, vy;
  private long m_lastTime, elapsedTime;
  private Double m_direction, m_velocity;
  private Image image;
  private String filePath;
  public PowerUp(String fp, int x, int y, int d)
  {
    m_bounds=new Rectangle(x, y, d, d);
    filePath=fp;
    image=new ImageIcon(fp).getImage();
    m_speed=50;
    m_delay=100;
    m_direction=new Double(0, 1);
    calculateVelocity();
  }
  public void draw(Graphics g)
  {
    g.drawImage(image, m_bounds.x, m_bounds.y, m_bounds.width, m_bounds.height, null);
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
    calculateVelocity();
  }
  public Rectangle getBounds()
  {
    return m_bounds;
  }
  public String getFilePath()
  {
    return filePath;
  }
  public void calculateVelocity()
  {
    dh=Math.pow((Math.pow(m_direction.x, 2)+Math.pow(m_direction.y, 2)), .5);
    vx=m_speed*(double)m_direction.x/dh;
    vy=m_speed*(double)m_direction.y/dh;
    
    m_velocity = new Double(vx, vy);
  }
}