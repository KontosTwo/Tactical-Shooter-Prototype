package com.mygdx.sound;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public enum SoundRepository // can return sound files as either music or noises
{
	GiftOfThistle("sound/Crossing The Yellow River.mp3");
	
	
	
	private final String file;
	
	SoundRepository(String file)
	{
		this.file = file;
	}
	
	public Music getMusic()
	{
		return Gdx.audio.newMusic(Gdx.files.internal(file));
	}
}
