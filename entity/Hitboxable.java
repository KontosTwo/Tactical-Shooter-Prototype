package com.mygdx.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.misc.Box;

public abstract class Hitboxable extends Visible
{
	private Box hitbox;
	private static final float FORCEFIELD = 1f;
	
	public Hitboxable(Vector2 center) 
	{
		super(center);
		hitbox = new Box(100,100,this.center);
	}
	protected void setSizeAll(int x,int y)
	{
		setAnimationSize(x,y);
		hitbox.setSize(x, y);
	}
	public void update(float dt)
	{
		super.update(dt);
	}
	public Vector2 getHitBoxBound()
	{
		return new Vector2(hitbox.getWidth(),hitbox.getHeight());
	}
	protected void setHitBoxSize(float x,float y)
	{
		hitbox.setSize((int)x, (int)y);
	}
	public void setToLeftOf(Hitboxable h)
	{
		center.x = h.hitbox.getLeft() - hitbox.getWidth()/2 - FORCEFIELD;
	}
	public void setToRightOf(Hitboxable h)
	{
		center.x = h.hitbox.getRight() + hitbox.getWidth()/2 + FORCEFIELD;
	}
	public void setToTopOf(Hitboxable h)
	{
		center.y = h.hitbox.getTop() + hitbox.getWidth()/2 + FORCEFIELD;
	}
	public void setToBotOf(Hitboxable h)
	{
		center.y = h.hitbox.getBot() - hitbox.getWidth()/2- FORCEFIELD;
	}
	public void setToLeftOf(float x)
	{
		center.x = x - hitbox.getWidth()/2 - FORCEFIELD;
	}
	public void setToRightOf(float x)
	{
		center.x = x + hitbox.getWidth()/2 + FORCEFIELD;
	}
	public void setToTopOf(float y)
	{
		center.y = y + hitbox.getHeight()/2 + FORCEFIELD;
	}
	public void setToBotOf(float y)
	{
		center.y = y - hitbox.getHeight()/2 - FORCEFIELD;
	}
	
	public boolean willCrossHitBox(float x1,float y1,float x2,float y2)
	{
		return willCross(x1,y1,x2,y2,hitbox);
		//return hitbox.overLaps(x1 - velocity.x, y1 - velocity.y, x2 - velocity.x, y2 - velocity.y);
	}
	public boolean overLapsHitBox(float x1,float y1,float x2,float y2)
	{
		return hitbox.overLaps(x1, y1, x2, y2);
	}
	public boolean crossX(float x1,float x2)
	{
		return hitbox.crossX(x1, x2);
	}
	public boolean crossY(float y1,float y2)
	{
		return hitbox.crossY(y1, y2);
	}

	public void stoppedBy(float x1,float y1,float x2,float y2)
	{
		/*float temp = 0;
		Box oldBox = hitbox.newBox(centerOld);
		if(y1 < y2)
		{
			temp = y1;
			y1 = y2;
			y2 = temp;
		}
		if(x1 > x2)
		{
			temp = x1;
			x1 = x2;
			x2 = temp;
		}*/
		
		
		/*if(oldBox.getRight() > x1 && oldBox.getLeft() < x2)
		{
			if(oldBox.getTop() < y2)
			{
				setToBotOf(y2);
				System.out.println("Bottom");
			}
			else if(oldBox.getBot() > y1)
			{
				setToTopOf(y1);
				System.out.println("Top");
			}
		}
		else
		{
			if(oldBox.getRight() < x1)
			{
				setToLeftOf(x1);
				System.out.println("Left");

			}
			else if(oldBox.getLeft() > x2)
			{
				setToRightOf(x2);
				System.out.println("Right");
			}
		}*/
		/*if(oldBox.getRight() > x1 && oldBox.getLeft() < x2)
		{
			if(oldBox.getTop() < y2)
			{
				setToBotOf(y2);
				//System.out.println("Bottom");
			}
			else if(oldBox.getBot() > y1)
			{
				setToTopOf(y1);
				//System.out.println("Top");
			}
		}
		else
		{
			if(oldBox.getRight() < x1)
			{
				setToLeftOf(x1);
				//System.out.println("Left");

			}
			else if(oldBox.getLeft() > x2)
			{
				setToRightOf(x2);
				//System.out.println("Right");
			}
		}*/
		center.set(centerOld);
	}
}
