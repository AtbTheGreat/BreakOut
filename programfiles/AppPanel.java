import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.ArrayList;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Color;
import java.lang.Runnable; 
import javax.swing.Timer;
import java.awt.event.*;
public class AppPanel extends JPanel implements KeyListener,Runnable
{
  private static final int refreshRate=100;
  private long lastRefresh;
  private boolean gameSwitch;
  private Thread gameLoop;
  private GameField gameField;
  private Rectangle gameFieldBounds;
  private Paddle paddle;
  private Rectangle paddleBounds;
  private Rectangle ballBounds;
  private ArrayList<Ball> balls=new ArrayList<Ball>();
  private Ball ball;
  private int lives;
  private ArrayList<Brick> bricks=new ArrayList<Brick>();
  private ArrayList<PowerUp> powerUps=new ArrayList<PowerUp>();
  private PowerUp powerUp;
  private Rectangle powerUpBounds;
  private final double speed;
  private String[] powerUpNames=new String[5];
  public AppPanel()
  {
    setBackground(new Color(255, 255, 255));
    gameField=new GameField(0, 0, 400, 220);
    gameFieldBounds=gameField.getBounds();
    int paddleHeight=20;
    int paddleWidth=4*paddleHeight;
    int paddleX=gameFieldBounds.x+(gameFieldBounds.width-paddleWidth)/2;
    int paddleY=gameFieldBounds.y+gameFieldBounds.height-paddleHeight;
    paddle=new Paddle(paddleX, paddleY, paddleWidth, paddleHeight);
    paddleBounds=paddle.getBounds();
    createBall();
    speed=100;
    createBricks();
    lives=3;
    powerUpNames[0]="drinkMe.png";
    powerUpNames[1]="eatMe.png";
    powerUpNames[2]="steroids.png";
    powerUpNames[3]="tortoise.png";
    powerUpNames[4]="zoya.png";
    addKeyListener(this);
    setFocusable(true);
    gameLoop=new Thread(this);
    gameSwitch=true;
    gameLoop.start();
  }
  public void run()
  {
    while(gameSwitch==true)
    {
      long currentTime=System.currentTimeMillis();
      paddle.animate(currentTime);
      checkPaddleWallCollision();
      if(balls.size()>0)
      {
        ball=balls.get(0);
        ball.animate(currentTime);
        checkBallPaddleCollision();
        checkBallWallCollision();
      }
      if(bricks.size()>0)
      {
        for(int i=0; i<bricks.size(); i++)
        {
          Brick brick=bricks.get(i);
          if(balls.size()>0)
          {
            if(checkBallBrickCollision(brick, i))
            {
              i-=1;
            }
          }
        }
      }
      if(powerUps.size()>0)
      {
        powerUp=powerUps.get(0); 
        powerUp.animate(currentTime);
        if(checkPowerUpWallCollision())
        {
          checkPaddlePowerUpCollision();
        }
      }
      if((currentTime-lastRefresh)>refreshRate)    
      {                                                 
        repaint();                                    
        lastRefresh=currentTime;                    
      }
      if(gameLoop.interrupted())
      {
        gameSwitch=false;
      }
    }
  }
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    draw(g);
  }
  private void draw(Graphics g)
  {
    g.setColor(new Color(0, 255, 0));
    gameField.draw(g);
    g.setColor(new Color(0, 0, 255));
    paddle.draw(g);
    if(bricks.size()>0)
    {
      for(int i=0; i<bricks.size(); i++)
      {
        Brick brick=bricks.get(i);
        g.setColor(brick.getColor());
        brick.draw(g);
      }
    }
    if(balls.size()>0)
    {
      g.setColor(new Color(255, 0, 0));
      ball.draw(g);
    }
    if(powerUps.size()>0)
    {
      powerUp.draw(g);
    }
  }
  private void createBricks()
  {
    for(int row=0; row<5; row++)
    {
      for(int column=0; column<5; column++)
      {
        int redValue=(int)(Math.random()*256);
        int greenValue=(int)(Math.random()*256);
        int blueValue=(int)(Math.random()*256);
        bricks.add(new Brick(gameFieldBounds.x+column*80, gameFieldBounds.y+row*20, 80, 20, new Color(redValue, greenValue, blueValue)));
      }
    }
  }
  private void checkBallWallCollision()
  {
    if(ballBounds.x<gameFieldBounds.x)
    {
      ball.collideX(gameFieldBounds.x);
    }
    else if((ballBounds.x+ballBounds.width)>(gameFieldBounds.x+gameFieldBounds.width))
    {
      ball.collideX(gameFieldBounds.x+gameFieldBounds.width-ballBounds.width);
    }
    else if(ballBounds.y<gameFieldBounds.y)
    {
      ball.collideY(gameFieldBounds.y);
    }
    else if((ballBounds.y+ballBounds.height)>(gameFieldBounds.y+gameFieldBounds.height))
    {
      balls.clear();
      loseLife();
    }
  }
  private void loseLife()
  {
    lives--;
    if(lives==0)
    {
      gameSwitch=false;
    }
    else if(lives>0)
    {
      createBall();
    }
  }
  private void createBall()
  {
    if(balls.size()==0)
    {
      int ballDiameter=20;
      int ballX=paddleBounds.x+(paddleBounds.width-ballDiameter)/2;
      int ballY=paddleBounds.y-ballDiameter;
      balls.add(new Ball(ballX, ballY, ballDiameter));
      ball=balls.get(0);
      ballBounds=ball.getBounds();
    }
  }
  private void checkBallPaddleCollision()
  {
    if(ballBounds.x>=paddleBounds.x && (ballBounds.x+ballBounds.width)<=(paddleBounds.x+paddleBounds.width) && (ballBounds.y+ballBounds.height)>paddleBounds.y && ball.getDirectionX()!=0 && ball.getDirectionY()!=0)
    {
      ball.collideY(paddleBounds.y-ballBounds.height);
    }
  }
  private void createNewPowerUp(Rectangle brickBounds)
  {
    int filePathNumber=(int)(Math.random()*5);
    int powerUpDiameter=20;
    int powerUpX=brickBounds.x+(brickBounds.width-powerUpDiameter)/2;
    int powerUpY=brickBounds.y+brickBounds.height;
    powerUps.add(new PowerUp(powerUpNames[filePathNumber], powerUpX, powerUpY, powerUpDiameter));
    powerUp=powerUps.get(0);
    powerUpBounds=powerUp.getBounds();
  }
  private void checkPaddlePowerUpCollision()
  {
    if(powerUpBounds.x>=paddleBounds.x && (powerUpBounds.x+powerUpBounds.width)<=(paddleBounds.x+paddleBounds.width) && (powerUpBounds.y+powerUpBounds.height)>=paddleBounds.y)
    {
      String filePath=powerUp.getFilePath();
      if(filePath.equals(powerUpNames[0]))
      {
        paddle.contract(gameFieldBounds);
        paddleBounds=paddle.getBounds();
      }
      else if(filePath.equals(powerUpNames[1]))
      {
        paddle.expand(gameFieldBounds);
        paddleBounds=paddle.getBounds();
      }
      else if(filePath.equals(powerUpNames[2]))
      {
        ball.strengthen();
      }
      else if(filePath.equals(powerUpNames[3]))
      {
        ball.decelerate();
      }
      else if(filePath.equals(powerUpNames[4]))
      {
        ball.accelerate();
      }
      powerUps.clear();
    }
  }
  private boolean checkBallBrickCollision(Brick brick, int brickNumber)
  {
    boolean collide=false;
    Rectangle brickBounds=brick.getBounds();
    if((ballBounds.x>brickBounds.x && (ballBounds.x+ballBounds.width)<(brickBounds.x+brickBounds.width) && ballBounds.y<(brickBounds.y+brickBounds.height)))
    {
      if(ball.getStrength()==false)
      {
        ball.collideY(brickBounds.y+brickBounds.height);
      }
      if(bricks.size()>1)
      {
        bricks.remove(brickNumber);
      }
      else if(bricks.size()==1)
      {
        bricks.clear();
        gameSwitch=false;
      }
      collide=true;
    }
    else if((ballBounds.y+ballBounds.height)<=(brickBounds.y+brickBounds.height) && (ballBounds.y+ballBounds.height)>=brickBounds.y)
    {
      if((ballBounds.x+ballBounds.width)>brickBounds.x && (ballBounds.x+ballBounds.width)<(brickBounds.x+brickBounds.width))
      {
        if(ball.getStrength()==false)
        {
          ball.collideX(brickBounds.x-ballBounds.width);
        }
        if(bricks.size()>1)
        {
          bricks.remove(brickNumber);
        }
        else if(bricks.size()==1)
        {
          bricks.clear();
          gameSwitch=false;
        }
        collide=true;
      }
      else if(ballBounds.x<(brickBounds.x+brickBounds.width) && ballBounds.x>brickBounds.x)
      {
        if(ball.getStrength()==false)
        {
          ball.collideX(brickBounds.x+brickBounds.width);
        }
        if(bricks.size()>1)
        {
          bricks.remove(brickNumber);
        }
        else if(bricks.size()==1)
        {
          bricks.clear();
          gameSwitch=false;
        }
        collide=true;
      }
      if(collide==true)
      {
        int decider=(int)(Math.random()*2);
        if(decider==0)
        {
          createNewPowerUp(brickBounds);
        }
      }
    }
    return collide;
  }
  public void keyReleased(KeyEvent e)
  {
    paddle.halt();
    if(ball.getDirectionY()==0)
    {
      ball.halt();
    }
  }
  public void keyPressed(KeyEvent e)
  {
    if(e.getKeyCode()!=KeyEvent.VK_SPACE)
    {
      paddle.navigate(e.getKeyCode(), gameFieldBounds);
      if(ball.getSticky())
      {
        ball.navigate(paddle);
      }
    }
    else if(e.getKeyCode()==KeyEvent.VK_SPACE && ball.getSticky()==true)
    {
      ball.release(speed);
    }
  }
  public void keyTyped(KeyEvent e)
  {
  }
  public void quitGameLoop()
  {
    gameLoop.interrupt();
  }
  private void checkPaddleWallCollision()
  {
    if(paddleBounds.x<gameFieldBounds.x)
    {
      paddle.collideX(gameFieldBounds.x);
      if(ball.getSticky())
      {
        ball.halt();
      }
    }
    else if((paddleBounds.x+paddleBounds.width)>(gameFieldBounds.x+gameFieldBounds.width))
    {
      paddle.collideX(gameFieldBounds.x+gameFieldBounds.width-paddleBounds.width);
      if(ball.getSticky())
      {
        ball.halt();
      }
    }
  }
  private boolean checkPowerUpWallCollision()
  {
    boolean missed=true;
    if((powerUpBounds.y+powerUpBounds.height)>=(gameFieldBounds.y+gameFieldBounds.height))
    {
      powerUps.clear();
      missed=false;
    }
    return missed;
  }
}