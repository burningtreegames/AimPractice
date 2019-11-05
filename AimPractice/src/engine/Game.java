package engine;

import java.util.ArrayList;
import java.util.Random;
import java.math.*;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class Game implements ApplicationListener
{	

	private ShapeRenderer pixel;
	private OrthographicCamera gameCamera;
	private OrthographicCamera fontCamera;
	
	private boolean fullscreen;
	
	private BitmapFont font;
	private SpriteBatch sb;
	private Random rand;
	
	private int targetSize = 16;
	
	public float scale;
	
	//private float colorshift = 0.1f;
	//private float colormodifier = -0.1f;
	
	private int bullseyes = 0;
	private int hits = 0;
	private int misses = 0;
	
	private ArrayList<Target> targets = new ArrayList<Target>();  
	
	@Override
	public void create()
	{
		font = new BitmapFont();
		sb = new SpriteBatch();
		gameCamera = new OrthographicCamera();
		gameCamera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		fontCamera = new OrthographicCamera();
		fontCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		scale = Gdx.graphics.getHeight() / 720;
		
		fullscreen = false;
		
		pixel = new ShapeRenderer();
		
		rand = new Random();
		
		targets.add(new Target(rand.nextInt(Gdx.graphics.getWidth() - 100) + 100, rand.nextInt(Gdx.graphics.getHeight() - 100) + 100, (int)(targetSize * scale), (int)(targetSize * scale), (float)Math.random() - 0.5f, (float)Math.random() - 0.5f, 3, 5));
	}
	

	@Override
	public void dispose()
	{
		
	}

	@Override
	public void pause()
	{

	}

	@Override
	public void render()
	{
		gameCamera.update();
		fontCamera.update();
		
		
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.
		
		pixel.begin(ShapeType.Filled);
		
		pixel.setProjectionMatrix(gameCamera.combined);
		sb.setProjectionMatrix(fontCamera.combined);
		update();
		
		
		//Gdx.graphics.setTitle("BTG Shooter Practice = " + Gdx.graphics.getFramesPerSecond() + " Hits = " + hits + " Bullseyes = " + bullseyes + " Misses = " + misses);
		
		pixel.end();
		
		sb.begin();
		font.setColor(Color.GREEN);
		font.draw(sb,"FPS = " + Gdx.graphics.getFramesPerSecond() + " Hits = " + hits + " Bullseyes = " + bullseyes + " Misses = " + misses, 10, 20);
		sb.end();
	}
	
	public void update()
	{
		boolean miss = false;
		
		if(targets.size() > 0)
			for(int i = 0; i < targets.size(); i++)
			{
				if(targets.get(i).hit)
					hits++;
					
				if(targets.get(i).bullseye)
					bullseyes++;
				if(targets.get(i).miss)
					miss = true;
			
				targets.get(i).update();
				targets.get(i).render(pixel);
				
				Gdx.graphics.setTitle( "BTG Shooter Practice" + "; FPS = " + Gdx.graphics.getFramesPerSecond() + "; Speed = " + targets.get(i).xSpeed + "; Hits = " + hits + "; Bullseyes = " + bullseyes + "; Misses = " + misses + "; x = " + targets.get(i).x + "; y = " + targets.get(i).y);
				
				if(targets.get(i).x > Gdx.graphics.getWidth() || targets.get(i).y > Gdx.graphics.getHeight())
				{
					targets.remove(i);
					i--;
				}
				else if(targets.get(i).hp <= 0)
				{	
					if(targets.get(i).lives > 0)
					{
						targets.add(new Target(targets.get(i).x, targets.get(i).y, (int)(targetSize * scale), (int)(targetSize * scale), (float)Math.random() - 0.5f, (float)Math.random() - 0.5f, targets.get(i).lives - 1, targets.get(i).maxSpeed));
						targets.add(new Target(targets.get(i).x, targets.get(i).y, (int)(targetSize * scale), (int)(targetSize * scale), (float)Math.random() - 0.5f, (float)Math.random() - 0.5f, targets.get(i).lives - 1, targets.get(i).maxSpeed));		
						targets.add(new Target(targets.get(i).x, targets.get(i).y, (int)(targetSize * scale), (int)(targetSize * scale), (float)Math.random() - 0.5f, (float)Math.random() - 0.5f, targets.get(i).lives - 1, targets.get(i).maxSpeed));		
					}
					
					targets.remove(i);		
				}	
				
				
			}
		
		
		if(targets.size() == 0)
			targets.add(new Target(rand.nextInt(Gdx.graphics.getWidth() - 100) + 100, rand.nextInt(Gdx.graphics.getHeight() - 100) + 100, (int)(targetSize * scale), (int)(targetSize * scale), (float)Math.random() - 0.5f, (float)Math.random() - 0.5f, 3, 5));
		
		if(Gdx.input.isKeyJustPressed(Keys.F11) && !fullscreen)
		{
			Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
			gameCamera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			fontCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			scale = (float)(Gdx.graphics.getHeight() / 720f);
			System.out.println((float)(Gdx.graphics.getHeight() / 720f));
			
			if(targets.size() > 0)
				for(int i = 0; i < targets.size(); i++)
				{
					targets.get(i).width = (16 * scale);
					targets.get(i).height = (16 * scale);
				}
			
			fullscreen = true;
		}
		else if(Gdx.input.isKeyJustPressed(Keys.F11) && fullscreen)
		{
			Gdx.graphics.setWindowedMode(1280, 720);
			gameCamera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			fontCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			scale = (float)(Gdx.graphics.getHeight() / 720f);
			System.out.println((float)(Gdx.graphics.getHeight() / 720f));
			
			if(targets.size() > 0)
				for(int i = 0; i < targets.size(); i++)
				{
					targets.get(i).width = (16 * scale);
					targets.get(i).height = (16 * scale);
					targets.get(i).bullseyeSize = targets.get(i).width / 4;
				}
			
			fullscreen = false;
		}
		else if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))
		{
			System.exit(0);
		}
			
	}
	

	@Override
	public void resize(int width, int height)
	{
		gameCamera.setToOrtho(true, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		fontCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		scale = (float)(Gdx.graphics.getHeight() / 720f);
		
		if(targets.size() > 0)
			for(int i = 0; i < targets.size(); i++)
			{
				targets.get(i).width = (16 * scale);
				targets.get(i).height = (16 * scale);
				targets.get(i).bullseyeSize = targets.get(i).width / 4;
			}
	}

	@Override
	public void resume()
	{

	}
	
	public int getBullseyes() {return bullseyes;}
	
	public void setBullseyes(int bullseyes) {this.bullseyes = bullseyes;}


	public int getHits() {return hits;}


	public void setHits(int hits) {this.hits = hits;}


	public int getMisses() {return misses;}


	public void setMisses(int misses) {this.misses = misses;}

}
		
/*
		 pixel.setColor(colorshift, 0, 0, 1);
		 
		
		for(int y = 0; y < camera.viewportHeight; y++)
			for(int x = 0; x < camera.viewportWidth; x++)
				pixel.rect(x, y, 1f, 1f);
		
		if (colorshift > 1f)
			colormodifier = -1f;
		else if (colorshift < 0f)
			colormodifier = 1f;
			
		colorshift = (colorshift + (0.1f * colormodifier));
		
		pixel.setColor(1, 1, 1, 1);
		pixel.rect(10, 10, 100f, 100f);*/