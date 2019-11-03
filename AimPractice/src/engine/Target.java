package engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Target 
{
	float x;
	float y;
	int width;
	int height;
	
	int lives;
	
	float xDirection;
	float yDirection;
	
	int maxSpeed;
	float xSpeed;
	float ySpeed;
	
	float acceleration = 1f;
	
	int hp;
	
	int bullseyeSize;
	
	boolean hit;
	boolean bullseye;
	boolean miss;
	
	public Target(float x, float y, int width, int height, float xDirection, float yDirection, int lives, int maxSpeed)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		bullseyeSize = width / 4;
		
		hp = 200;
		hit = false;
		bullseye = false;
		miss = false;
		
		this.maxSpeed = maxSpeed;
		
		this.xDirection = xDirection;
		this.yDirection = yDirection;
		
		this.lives = lives;
	}
	
	public void update()
	{
		hit = false;
		bullseye = false;
		miss = false;
		
		if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
			if(collision(Gdx.input.getX(), Gdx.input.getY()))
				{
					if(hit)
						hp = hp - 25;
					else if(bullseye)
						hp = hp - 75;
				}
		
		move();
		
		if(Gdx.input.isKeyJustPressed(Keys.MINUS) && maxSpeed >= 0)
		{
			maxSpeed--;
		}
		else if(Gdx.input.isKeyJustPressed(Keys.PLUS))
		{
			maxSpeed++;
		}
	}
	
	public void render(ShapeRenderer shape)
	{
		shape.setColor(Color.GREEN);
		shape.rect(x, y, width, height);
		
		shape.setColor(Color.RED);
		shape.rect(x + (bullseyeSize + bullseyeSize / 2), y + (bullseyeSize + bullseyeSize / 2), bullseyeSize, bullseyeSize);
	}
	
	public boolean collision(int x, int y)
	{
		
		if(x > this.x && x < this.x + width && y > this.y && y < this.y + height)
		{
			System.out.println("collision");
			
			hit = true;
			
			if(x > this.x + (bullseyeSize + bullseyeSize / 2) && x < this.x + (width - (bullseyeSize + bullseyeSize / 2)) && y > this.y + (bullseyeSize + bullseyeSize / 2) && y < this.y + (height - (bullseyeSize + bullseyeSize / 2)))
			{
				bullseye = true;
				hit = false;
			}
				
			
			return true;
		}
		
		miss = true;
		return false;
	}
	
	public void move()
	{
		if(x >= 0 && x + width <= Gdx.graphics.getWidth() && y >= 0 && y + height <= Gdx.graphics.getHeight())
		{
			if(xSpeed < maxSpeed)
				xSpeed = xSpeed + (acceleration * Gdx.graphics.getDeltaTime());
			
			if(ySpeed < maxSpeed)
				ySpeed = ySpeed + (acceleration * Gdx.graphics.getDeltaTime());
			
			x = x + (xSpeed * xDirection);
			y = y + (ySpeed * yDirection);
			
			if(x<= 0)
			{
				xDirection = Math.abs(xDirection);
				x = x + xSpeed;
			}
			
			if(x + width >= Gdx.graphics.getWidth())
			{
				xDirection = xDirection * -1;
				x = x - xSpeed;
			}
			
			if(y<= 0)
			{
				yDirection = Math.abs(yDirection);
				y = y + ySpeed;
			}
			
			if(y + height >= Gdx.graphics.getHeight())
			{
				yDirection = yDirection * -1;
				y = y - ySpeed;
			}
			
		}
	
	}
	
}
