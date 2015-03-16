import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
public class Brick
{
  private Rectangle m_bounds;
  private Color color;
  public Brick(int x, int y, int width, int height, Color colour)
  {
    m_bounds=new Rectangle(x, y, width, height);
    color=colour;
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
  public Color getColor()
  {
    return color;
  }
}