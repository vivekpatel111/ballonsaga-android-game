package com.rocketapps.balloonsaga;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.adt.color.Color;

import android.graphics.Typeface;
import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.rocketapps.balloonsaga.SceneManager.AllScenes;

public class GameManager {

	public enum GameLevels{
		LEVELSELECT1,
		LEVELSELECT2,
		LEVEL0,
		LEVEL1,
		LEVEL2,
		LEVEL3,
		LEVEL4,
		LEVEL5,
		LEVEL6,
		LEVEL7,
		LEVEL8,
		LEVEL9,
		LEVEL10
	};
	
	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
	public SceneManager sceneManager;
	public GameLevels currentLevel;
	public Scene levelSelectOne,levelSelectTwo,levelOne,levelTwo,levelThree,levelFour,levelFive,
			levelSix,levelSeven,levelEight,levelNine,levelTen;
	public Sound balloonPop,starSound,happySound,sadSound;
	public static Music backMusic,levelMusic1,levelMusic2,levelMusic3;
	
	private ITextureRegion oneTR,twoTR,threeTR,fourTR,fiveTR,sixTR,sevenTR,eightTR,nineTR,tenTR;
	private BitmapTextureAtlas oneTA,twoTA,threeTA,fourTA,fiveTA,sixTA,sevenTA,eightTA,nineTA,tenTA,lockTA;
	private ITiledTextureRegion star1TR,star2TR,star3TR;
	private BitmapTextureAtlas  star1TA,star2TA,star3TA;
	private ITextureRegion lockTR,menuTR,menuonTR,navigateTR,gameCompleteTR, playAgainTR, nextTR, ratingStarTR,resultTR,heart1TR,heart2TR,heart3TR;
	private BitmapTextureAtlas menuTA,menuonTA,navigateTA,gameCompleteTA, playAgainTA, nextTA, ratingStarTA,resultTA,heartTA;
	private BitmapTextureAtlas levelBackgroundTA;
	private ITextureRegion balloonTR,levelBackgroundTR;
	private ITiledTextureRegion fanTR;
	
	private ButtonSprite menu,playAgain,next;
	private Sprite backgroundImage,balloon,gameComplete, resultImage, ratingStar1, ratingStar2, ratingStar3, heart1,heart2,heart3;
	private AnimatedSprite fan,star1, star2, star3;
	private FixtureDef BALLOON_FIX;
	
	private Body balloonBody;
	private Rectangle lWall, rWall, dWall, uWall;
	private Body lBody,rBody,uBody,dBody;
	private Rectangle exitGround;
	private Rectangle sharpPointRect1,sharpPointRect2,sharpPointRect3,sharpPointRect4,sharpPointRect5,sharpPointRect6,sharpPointRect7;
	private ITexture fontTR;
	private Font font;
	
	private int starCollected, lifeRemaining;
	private Text scoreText;
	private Rectangle startGround;

    private Rectangle smallWall1,smallWall2,smallWall3,smallWall4,smallWall5,smallWall6;
	private Body startGroundBody;
	private Body smallWallBody1,smallWallBody2,smallWallBody3,smallWallBody4,smallWallBody5,smallWallBody6;
	private LevelHelper dbHandler;
	
	public boolean isLevelLocked = false;
	
	public GameManager(SceneManager sceneManager) {
		this.sceneManager = sceneManager;
		dbHandler = new LevelHelper(sceneManager.activity.getApplicationContext());
		 dbHandler.open();
		
		 dbHandler.addLevel(1);
		 dbHandler.addLevel(2);
		 dbHandler.addLevel(3);
		 dbHandler.addLevel(4);
		 dbHandler.addLevel(5);
		 dbHandler.addLevel(6);
		 dbHandler.addLevel(7);
		 dbHandler.addLevel(8);
		 dbHandler.addLevel(9);
		 dbHandler.addLevel(10);
		
		
		dbHandler.updateLevel(1);
		loadMusicResources();
		
	}
	
	public void loadMusicResources()
	{
		//TODO Load Music Resource
		MusicFactory.setAssetBasePath("sound/");
		SoundFactory.setAssetBasePath("sound/");
		try {
			levelMusic1 = MusicFactory.createMusicFromAsset(this.sceneManager.engine.getMusicManager(),this.sceneManager.activity, "levelMusic1.ogg");
			levelMusic2 = MusicFactory.createMusicFromAsset(this.sceneManager.engine.getMusicManager(),this.sceneManager.activity, "levelMusic2.ogg");
			levelMusic3 = MusicFactory.createMusicFromAsset(this.sceneManager.engine.getMusicManager(),this.sceneManager.activity, "levelMusic3.ogg");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    levelMusic1.setVolume(1f);
		levelMusic1.setLooping(true);
		levelMusic2.setVolume(1f);
		levelMusic2.setLooping(true);
		levelMusic3.setVolume(1f);
		levelMusic3.setLooping(true);
		levelMusic1.play();
		levelMusic2.play();
		levelMusic3.play();
		levelMusic1.pause();
		levelMusic2.pause();
		levelMusic3.pause();
		
		
	}
	
	public void stopAllMusic()
	{   
		if(levelMusic1.isPlaying())
		levelMusic1.pause();
	    
		if(levelMusic2.isPlaying())
		levelMusic2.pause();
	   
		if(levelMusic3.isPlaying())
		levelMusic3.pause();
	}
	public void loadLevelOneSelectionResources(){
		//TODO Load Level Selection 1 Resource
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("background/");
		this.sceneManager.splashTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(),800,480);
		this.sceneManager.splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.sceneManager.splashTA, this.sceneManager.activity, "help.png",0,0);
		this.sceneManager.splashTA.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("button/");
		menuTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 130, 65);
		menuTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, this.sceneManager.activity, "menu.png",0,0);
		menuTA.load();
		
		menuonTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 130, 65);
		menuonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuonTA, this.sceneManager.activity, "menu_pressed.png",0,0);
		menuonTA.load();
		
		navigateTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 130, 65);
		navigateTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(navigateTA, this.sceneManager.activity, "nextsimple.png",0,0);
		navigateTA.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
		oneTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 120, 150);
		oneTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(oneTA, this.sceneManager.activity, "I.png",0,0);
		oneTA.load();
		
		twoTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 120, 150);
		twoTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(twoTA, this.sceneManager.activity, "II.png",0,0);
		twoTA.load();
		
		threeTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 120, 150);
		threeTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(threeTA, this.sceneManager.activity, "III.png",0,0);
		threeTA.load();
		
		fourTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 120, 150);
		fourTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(fourTA, this.sceneManager.activity, "IV.png",0,0);
		fourTA.load();
		
		fiveTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 120, 150);
		fiveTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(fiveTA, this.sceneManager.activity, "V.png",0,0);
		fiveTA.load();
		
		
		lockTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 75, 75);
		lockTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(lockTA, this.sceneManager.activity, "lock.png",0,0);
		lockTA.load();
		
		SoundFactory.setAssetBasePath("sound/");
		try {
			balloonPop = SoundFactory.createSoundFromAsset(this.sceneManager.engine.getSoundManager(), this.sceneManager.activity, "balloonPop.ogg");
			starSound = SoundFactory.createSoundFromAsset(this.sceneManager.engine.getSoundManager(), this.sceneManager.activity, "star.ogg");
			happySound = SoundFactory.createSoundFromAsset(this.sceneManager.engine.getSoundManager(), this.sceneManager.activity, "happySound.ogg");
			sadSound = SoundFactory.createSoundFromAsset(this.sceneManager.engine.getSoundManager(), this.sceneManager.activity, "sadSound.ogg");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		balloonPop.setLoopCount(0);
		balloonPop.setVolume(1f);
		starSound.setLoopCount(0);
		starSound.setVolume(1f);
		happySound.setLoopCount(0);
		happySound.setVolume(1f);
		sadSound.setLoopCount(0);
		sadSound.setVolume(1f);
	}
	
	public Scene createLevelOneSelectionScene(){
		levelSelectOne = new Scene();
		levelSelectOne.setOnSceneTouchListenerBindingOnActionDownEnabled(true);

		this.sceneManager.backGround = new Sprite(400, 240, this.sceneManager.splashTR, this.sceneManager.engine.getVertexBufferObjectManager());
		levelSelectOne.attachChild(this.sceneManager.backGround);
		
		menu = new ButtonSprite(150, 50, menuTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp())
					GameManager.this.sceneManager.loadMenu();
				else{
					menu.detachSelf();
					menu = new ButtonSprite(150, 50, menuonTR, GameManager.this.sceneManager.engine.getVertexBufferObjectManager());
					levelSelectOne.attachChild(menu);
					setCurrentScene(GameLevels.LEVELSELECT1);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		levelSelectOne.registerTouchArea(menu);
		levelSelectOne.attachChild(menu);
		
		ButtonSprite navigate = new ButtonSprite(650, 50, navigateTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp())
					loadNextLevelSelection(true);
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		levelSelectOne.registerTouchArea(navigate);
		levelSelectOne.attachChild(navigate);
	
		ButtonSprite one = new ButtonSprite(100, 340, oneTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					
					if(sceneManager.soundEnabled)
					{	
					stopAllMusic();
						 levelMusic1.resume();
					}
					loadLevelOneResources();
					createLevelOneScene();
					lifeRemaining = 2;
					setCurrentScene(GameLevels.LEVEL1);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		
		levelSelectOne.attachChild(one);
		levelSelectOne.registerTouchArea(one);
		
		ButtonSprite two = new ButtonSprite(250, 240, twoTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					 lifeRemaining = 2;
					if(dbHandler.getLevelStatus(2)==1)
					{
						if(sceneManager.soundEnabled)
						{	
						stopAllMusic();
						levelMusic2.resume();
						}
						
					 loadLevelTwoResources();
					 createLevelTwoScene();
					 setCurrentScene(GameLevels.LEVEL2);
					}
					else
					{
						createLevelLockedScene(2);
					}
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		levelSelectOne.attachChild(two);
		levelSelectOne.registerTouchArea(two);
		
		ButtonSprite three = new ButtonSprite(400, 340, threeTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				  if(pSceneTouchEvent.isActionUp()){
					  if(dbHandler.getLevelStatus(3)==1)
						{  
						  if(sceneManager.soundEnabled)
							{	
							stopAllMusic();
							levelMusic3.resume();
							}
					     loadLevelThreeResources();
					     createLevelThreeScene();
					     lifeRemaining = 2;
					     setCurrentScene(GameLevels.LEVEL3);
				    }
					  else
						{
							createLevelLockedScene(3);
						}
				}
				  
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		levelSelectOne.attachChild(three);
		levelSelectOne.registerTouchArea(three);
		
		ButtonSprite four = new ButtonSprite(550, 240, fourTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(dbHandler.getLevelStatus(4)==1)
				{
					if(sceneManager.soundEnabled)
					{	
					stopAllMusic();
					levelMusic1.resume();
					}
				loadLevelFourResources();
				createLevelFourScene();
				 lifeRemaining = 2;
				 setCurrentScene(GameLevels.LEVEL4);
				}
				else
				{
					createLevelLockedScene(4);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		levelSelectOne.attachChild(four);
		levelSelectOne.registerTouchArea(four);
		
		ButtonSprite five = new ButtonSprite(700, 340, fiveTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(dbHandler.getLevelStatus(5)==1)
				{
					if(sceneManager.soundEnabled)
					{	
					stopAllMusic();
					levelMusic2.resume();
					}
				loadLevelFiveResources();
				createLevelFiveScene();
				 lifeRemaining = 2;
				 setCurrentScene(GameLevels.LEVEL5);
				}
				else
				{
					createLevelLockedScene(5);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		levelSelectOne.attachChild(five);
		levelSelectOne.registerTouchArea(five);
		if(dbHandler.getLevelStatus(2) == 0 )
		{
			Sprite lock2 = new Sprite(215,225,lockTR,this.sceneManager.engine.getVertexBufferObjectManager());
			lock2.setAnchorCenter(0, 0);
			levelSelectOne.attachChild(lock2);
		}
		if(dbHandler.getLevelStatus(3) == 0 )
		{
			Sprite lock3 = new Sprite(365,325,lockTR,this.sceneManager.engine.getVertexBufferObjectManager());
			lock3.setAnchorCenter(0, 0);
			levelSelectOne.attachChild(lock3);
		}
		if(dbHandler.getLevelStatus(4) == 0 )
		{
			Sprite lock4 = new Sprite(515,225,lockTR,this.sceneManager.engine.getVertexBufferObjectManager());
			lock4.setAnchorCenter(0, 0);
			levelSelectOne.attachChild(lock4);
		}
		if(dbHandler.getLevelStatus(5) == 0 )
		{
			Sprite lock5 = new Sprite(665,325,lockTR,this.sceneManager.engine.getVertexBufferObjectManager());
			lock5.setAnchorCenter(0, 0);
			levelSelectOne.attachChild(lock5);
		}
		
		return levelSelectOne;
	}
	
	public void loadLevelTwoSelectionResources(){
		//TODO Load Level Selection 2 Resource
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("background/");
		this.sceneManager.splashTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(),800,480);
		this.sceneManager.splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.sceneManager.splashTA, this.sceneManager.activity, "help.png",0,0);
		this.sceneManager.splashTA.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("button/");
		menuTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 130, 65);
		menuTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, this.sceneManager.activity, "menu.png",0,0);
		menuTA.load();
		
		menuonTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 130, 65);
		menuonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuonTA, this.sceneManager.activity, "menu_pressed.png",0,0);
		menuonTA.load();
		
		navigateTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 130, 65);
		navigateTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(navigateTA, this.sceneManager.activity, "backsimple.png",0,0);
		navigateTA.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
		sixTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 120, 150);
		sixTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sixTA, this.sceneManager.activity, "VI.png",0,0);
		sixTA.load();
		
		sevenTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 120, 150);
		sevenTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sevenTA, this.sceneManager.activity, "VII.png",0,0);
		sevenTA.load();
		
		eightTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 120, 150);
		eightTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(eightTA, this.sceneManager.activity, "VIII.png",0,0);
		eightTA.load();
		
		nineTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 120, 150);
		nineTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(nineTA, this.sceneManager.activity, "IX.png",0,0);
		nineTA.load();
		
		tenTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 120, 150);
		tenTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(tenTA, this.sceneManager.activity, "X.png",0,0);
		tenTA.load();
		
		lockTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 75, 75);
		lockTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(fiveTA, this.sceneManager.activity, "lock.png",0,0);
		lockTA.load();
	}
	
	public Scene createLevelTwoSelectionScene(){
		levelSelectTwo = new Scene();
		levelSelectTwo.setOnSceneTouchListenerBindingOnActionDownEnabled(true);

		this.sceneManager.backGround = new Sprite(400, 240, this.sceneManager.splashTR, this.sceneManager.engine.getVertexBufferObjectManager());
		levelSelectTwo.attachChild(this.sceneManager.backGround);
		
		menu = new ButtonSprite(150, 50, menuTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp())
					GameManager.this.sceneManager.loadMenu();
				else{
					menu.detachSelf();
					menu = new ButtonSprite(150, 50, menuonTR, GameManager.this.sceneManager.engine.getVertexBufferObjectManager());
					levelSelectTwo.attachChild(menu);
					setCurrentScene(GameLevels.LEVELSELECT1);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		levelSelectTwo.registerTouchArea(menu);
		levelSelectTwo.attachChild(menu);
		
		ButtonSprite navigate = new ButtonSprite(650, 50, navigateTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp())
					loadNextLevelSelection(false);
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		levelSelectTwo.registerTouchArea(navigate);
		levelSelectTwo.attachChild(navigate);
		
		ButtonSprite six = new ButtonSprite(100, 240, sixTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(dbHandler.getLevelStatus(6)==1)
				{ 	
					if(sceneManager.soundEnabled)
					{	
					stopAllMusic();
					levelMusic3.resume();
					}
				  loadLevelSixResources();
				    createLevelSixScene();
				    lifeRemaining = 2;
				    setCurrentScene(GameLevels.LEVEL6);
				}
				else
				{
					createLevelLockedScene(6);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		levelSelectTwo.attachChild(six);
		levelSelectTwo.registerTouchArea(six);
		
		ButtonSprite seven = new ButtonSprite(250, 340, sevenTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(dbHandler.getLevelStatus(7)==1)
				{  
					if(sceneManager.soundEnabled)
					{	
					stopAllMusic();
					levelMusic1.resume();
					}
				loadLevelSevenResources();
			       createLevelSevenScene();
			       lifeRemaining = 2;
			       setCurrentScene(GameLevels.LEVEL7);
				}
				else
				{
					createLevelLockedScene(7);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		levelSelectTwo.attachChild(seven);
		levelSelectTwo.registerTouchArea(seven);
		
		ButtonSprite eight = new ButtonSprite(400, 240, eightTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(dbHandler.getLevelStatus(8)==1)
				{ 
					if(sceneManager.soundEnabled)
					{	
					stopAllMusic();
					levelMusic2.resume();
					}
				loadLevelEightResources();
			       createLevelEightScene();
			       lifeRemaining = 2;
			       setCurrentScene(GameLevels.LEVEL8);
				}
				else
				{
					createLevelLockedScene(8);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		levelSelectTwo.attachChild(eight);
		levelSelectTwo.registerTouchArea(eight);
		
		ButtonSprite nine = new ButtonSprite(550, 340, nineTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(dbHandler.getLevelStatus(9)==1)
				{  
					if(sceneManager.soundEnabled)
					{	
					stopAllMusic();
					levelMusic3.resume();
					}
				loadLevelNineResources();
			       createLevelNineScene();
			       lifeRemaining = 2;
			       setCurrentScene(GameLevels.LEVEL9);
				}
				else
				{
					createLevelLockedScene(9);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		levelSelectTwo.attachChild(nine);
		levelSelectTwo.registerTouchArea(nine);
		
		ButtonSprite ten = new ButtonSprite(700, 240, tenTR, this.sceneManager.engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(dbHandler.getLevelStatus(10)==1)
				{
					if(sceneManager.soundEnabled)
					{	
					stopAllMusic();
					levelMusic1.resume();
					}
				loadLevelTenResources();
			       createLevelTenScene();
			       lifeRemaining = 2;
			       setCurrentScene(GameLevels.LEVEL10);
				}
				else
				{
					createLevelLockedScene(10);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		levelSelectTwo.attachChild(ten);
		levelSelectTwo.registerTouchArea(ten);
		
		if(dbHandler.getLevelStatus(6) == 0 )
		{
			Sprite lock6 = new Sprite(65,225,lockTR,this.sceneManager.engine.getVertexBufferObjectManager());
			lock6.setAnchorCenter(0, 0);
			levelSelectTwo.attachChild(lock6);
		}
		if(dbHandler.getLevelStatus(7) == 0 )
		{
			Sprite lock7 = new Sprite(215,325,lockTR,this.sceneManager.engine.getVertexBufferObjectManager());
			lock7.setAnchorCenter(0, 0);
			levelSelectTwo.attachChild(lock7);
		}
		if(dbHandler.getLevelStatus(8) == 0 )
		{
			Sprite lock8 = new Sprite(365,225,lockTR,this.sceneManager.engine.getVertexBufferObjectManager());
			lock8.setAnchorCenter(0, 0);
			levelSelectTwo.attachChild(lock8);
		}
		if(dbHandler.getLevelStatus(9) == 0 )
		{
			Sprite lock9 = new Sprite(515,325,lockTR,this.sceneManager.engine.getVertexBufferObjectManager());
			lock9.setAnchorCenter(0, 0);
			levelSelectTwo.attachChild(lock9);
		}
		if(dbHandler.getLevelStatus(10) == 0 )
		{
			Sprite lock10 = new Sprite(665,225,lockTR,this.sceneManager.engine.getVertexBufferObjectManager());
			lock10.setAnchorCenter(0, 0);
			levelSelectTwo.attachChild(lock10);
		}
				return levelSelectTwo;
	}
	
	protected void loadNextLevelSelection(boolean next) {
		if(next){
			loadLevelTwoSelectionResources();
			createLevelTwoSelectionScene();
			setCurrentScene(GameLevels.LEVELSELECT2);
		}
		else{
			loadLevelOneSelectionResources();
			createLevelOneSelectionScene();
			setCurrentScene(GameLevels.LEVELSELECT1);
		}
	}

	public void loadLevelOneResources(){
		// TODO Load Level 1 Resource 
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
		levelBackgroundTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),1024,1024);
		balloonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA, this.sceneManager.activity,"balloon.png",5,5);
		fanTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(levelBackgroundTA,this.sceneManager.activity,"fanSprite.png",125,5, 6, 1);
		levelBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA,this.sceneManager.activity,"one.png",5,125); 
		levelBackgroundTA.load();
		
		star1TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star1TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star1TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star1TA.load();
		
		star2TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star2TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star2TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star2TA.load();
		
		star3TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star3TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star3TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star3TA.load();
		
		heartTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),30, 30);
		heart1TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart2TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart3TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heartTA.load();
		
		fontTR =  new BitmapTextureAtlas(sceneManager.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(sceneManager.engine.getFontManager(),fontTR, Typeface.SANS_SERIF,24,true, Color.WHITE);
	    font.load();    
		
		sceneManager.mPhysicsWorld = new PhysicsWorld(new Vector2(0,-SensorManager.GRAVITY_EARTH + 6.5f), false);
		sceneManager.WALL_FIX = PhysicsFactory.createFixtureDef(1,0,1);
		BALLOON_FIX=PhysicsFactory.createFixtureDef(1,0.3f,1);
	}
	
	public Scene createLevelOneScene(){
		levelOne = new Scene();
		
		starCollected = 0;
		backgroundImage = new Sprite(0,0,levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager());
	    backgroundImage.setAnchorCenter(0,0);
	    levelOne.attachChild(backgroundImage);
	    
	    heart1 = new Sprite(690,455,heart1TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart1.setVisible(true);
	    levelOne.attachChild(heart1);
	    
	    heart2 = new Sprite(730,455,heart2TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart2.setVisible(true);
	    levelOne.attachChild(heart2);
	    
	    heart3 = new Sprite(770,455,heart3TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart3.setVisible(true);
	    levelOne.attachChild(heart3);
	    
	    fan = new AnimatedSprite(50,400,fanTR,this.sceneManager.engine.getVertexBufferObjectManager());
	    long[] frameDuration = {50,50,50,50,50,50};
	    fan.animate(frameDuration);
	    levelOne.attachChild(fan);
	    fan.setVisible(false);
	    
	    star1 = new AnimatedSprite(100,450,star1TR,this.sceneManager.engine.getVertexBufferObjectManager())
		{
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				 if(this.collidesWith(balloon)){
					 if(sceneManager.soundEnabled)
						 starSound.play();
					 this.detachSelf();
					 star1TR = null;
					 starCollected++;
					 scoreText.setText("Stars : "+ starCollected+" ");
					 levelOne.unregisterUpdateHandler(this);
			      }
			    
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		star2 = new AnimatedSprite(300, 75, star2TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(balloon)) {
					if(sceneManager.soundEnabled)
						 starSound.play();
					this.detachSelf();
					star2TR = null;
					starCollected ++;
					scoreText.setText("Stars :" + starCollected + " ");
					levelOne.unregisterUpdateHandler(this);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		star3 = new AnimatedSprite(500, 125, star3TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(balloon)) {
					if(sceneManager.soundEnabled)
						 starSound.play();
					this.detachSelf();
	
					star3TR = null;
					starCollected ++;
					scoreText.setText("Stars :" + starCollected + " ");
					levelOne.unregisterUpdateHandler(this);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		long[] starFrameDuration = { 300, 150 };
		star1.animate(starFrameDuration);
		star2.animate(starFrameDuration);
		star3.animate(starFrameDuration);

		levelOne.registerUpdateHandler(star1);
		levelOne.registerUpdateHandler(star2);
		levelOne.registerUpdateHandler(star3);

		scoreText = new Text(60, 460, font, "Stars : XX",sceneManager.engine.getVertexBufferObjectManager());
		scoreText.setText("Stars : 0 ");
		levelOne.attachChild(scoreText);
		levelOne.attachChild(star1);
		levelOne.attachChild(star2);
		levelOne.attachChild(star3);
	
		Sprite lOneImage = new Sprite(CAMERA_WIDTH / 2 - 20, CAMERA_HEIGHT / 2,	levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					
					fan.setVisible(true);
					fan.setPosition(pTouchAreaLocalX, pTouchAreaLocalY + 30);
					//balloonBody.setType(BodyType.DynamicBody);
					float vX = balloon.getX() - pTouchAreaLocalX + 30;
					float vY = balloon.getY() - pTouchAreaLocalY + 30;
					if (Math.abs(vX) <= 70 && Math.abs(vY) <= 70)
						balloonBody.setLinearVelocity(vX / 17, vY / 17);
				}
				if (pSceneTouchEvent.isActionUp()) {
					fan.setVisible(false);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}
		};
	    levelOne.registerTouchArea(lOneImage);
		levelOne.setAnchorCenter(0,0);
		this.sceneManager.camera.setCenter(CAMERA_WIDTH/2, CAMERA_HEIGHT/2);
		
		lWall  = new Rectangle(0,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
		rWall  = new Rectangle(795,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
		dWall  = new Rectangle(0,0,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
		uWall  = new Rectangle(0,475,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall1 = new Rectangle(425, 95,140, 10,this.sceneManager.engine.getVertexBufferObjectManager());
		sharpPointRect1 = new Rectangle(0,10,800,10,this.sceneManager.engine.getVertexBufferObjectManager());
		
		smallWall1.setColor(Color.TRANSPARENT);
		sharpPointRect1.setColor(Color.TRANSPARENT);
		
		lWall.setColor(Color.WHITE);
		rWall.setColor(Color.WHITE);
		dWall.setColor(Color.WHITE);
		uWall.setColor(Color.WHITE);
		
		smallWall1.setAnchorCenter(0, 0);
		sharpPointRect1.setAnchorCenter(0, 0);
		
		lWall.setAnchorCenter(0,0);
		rWall.setAnchorCenter(0,0);
		dWall.setAnchorCenter(0,0);
		uWall.setAnchorCenter(0,0);
			
		
		lBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, lWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		rBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, rWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		uBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, uWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		dBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, dWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody1 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall1,BodyType.StaticBody,this.sceneManager.WALL_FIX);
		
		levelOne.attachChild(smallWall1);
		levelOne.attachChild(sharpPointRect1);
		
		levelOne.attachChild(lWall);
		levelOne.attachChild(rWall);
		levelOne.attachChild(uWall);
		levelOne.attachChild(dWall);
		
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall1,smallWallBody1 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lWall,lBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rWall,rBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(uWall,uBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dWall,dBody ));
		
		startGround = new Rectangle(30,95,140,10,this.sceneManager.engine.getVertexBufferObjectManager());
		startGround.setAnchorCenter(0,0);
		startGround.setColor(Color.TRANSPARENT);
		
		//middleGround = new Rectangle(425, 95,140, 10,this.sceneManager.engine.getVertexBufferObjectManager());
		//middleGround.setAnchorCenter(0,0);
		//middleGround.setColor(Color.TRANSPARENT);
		
		exitGround = new Rectangle(655, 95,145, 10,this.sceneManager.engine.getVertexBufferObjectManager());
		exitGround.setAnchorCenter(0,0);
		exitGround.setColor(Color.TRANSPARENT);
		
		startGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,startGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		//exitGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,exitGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		//middleGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,middleGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(startGround,startGroundBody ));
		//this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(middleGround,middleGroundBody ));
		//this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(exitGround,exitGroundBody ));
		levelOne.attachChild(exitGround);
		//levelOne.attachChild(middleGround);
		levelOne.attachChild(startGround);
		
		
		balloon = new Sprite(70, 150, balloonTR,this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(exitGround)) {
					 unregisterUpdateHandler(balloon);
						balloon.detachSelf();
						GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
						
					if (starCollected >= 2)
					{
						 unregisterUpdateHandler(balloon);
						 if(sceneManager.soundEnabled)
							 happySound.play();
						createGameOverScene(starCollected, 1);
					}
					else
					{
						 unregisterUpdateHandler(balloon);
						 if(sceneManager.soundEnabled)
							 sadSound.play();
						createGameOverScene(0, 1);
					}
				}
				if(this.collidesWith(sharpPointRect1)) {
                    unregisterUpdateHandler(balloon);
					balloon.detachSelf();
					GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
					if(sceneManager.soundEnabled)
						balloonPop.play();
					checkForGameOver(GameLevels.LEVEL1,1);
					
				}
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		balloon.setAnchorCenter(0,0);
		
		balloonBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, balloon, BodyType.DynamicBody,BALLOON_FIX);
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon,balloonBody, true,false ));
		levelOne.attachChild(balloon);
		createGameOverResources();
		levelOne.registerUpdateHandler(this.sceneManager.mPhysicsWorld);
			
		return levelOne;

			}

	public void loadLevelTwoResources(){
		// TODO Load Level 2 Resource 
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
		levelBackgroundTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),1024,1024);
		balloonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA, this.sceneManager.activity,"balloon.png",5,5);
		fanTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(levelBackgroundTA,this.sceneManager.activity,"fanSprite.png",125,5, 6, 1);
		levelBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA,this.sceneManager.activity,"two.png",5,125); 
		levelBackgroundTA.load();
		
		star1TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star1TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star1TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star1TA.load();
		
		star2TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star2TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star2TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star2TA.load();
		
		star3TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star3TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star3TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star3TA.load();
		
		heartTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),30, 30);
		heart1TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart2TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart3TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heartTA.load();   
		
		fontTR =  new BitmapTextureAtlas(sceneManager.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(sceneManager.engine.getFontManager(),fontTR, Typeface.SANS_SERIF,24,true, Color.WHITE);
	    font.load();    
		
		sceneManager.mPhysicsWorld = new PhysicsWorld(new Vector2(0,-SensorManager.GRAVITY_EARTH + 6.5f), false);
		sceneManager.WALL_FIX = PhysicsFactory.createFixtureDef(1,0,1);
		BALLOON_FIX=PhysicsFactory.createFixtureDef(1,0.3f,1);
	}

	public Scene createLevelTwoScene(){
	 
		levelTwo = new Scene();
		starCollected = 0;
		backgroundImage = new Sprite(0,0,levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager());
	    backgroundImage.setAnchorCenter(0,0);
	    levelTwo.attachChild(backgroundImage);
	    
	    heart1 = new Sprite(690,455,heart1TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart1.setVisible(true);
	    levelTwo.attachChild(heart1);
	    
	    heart2 = new Sprite(730,455,heart2TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart2.setVisible(true);
	    levelTwo.attachChild(heart2);
	    
	    heart3 = new Sprite(770,455,heart3TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart3.setVisible(true);
	    levelTwo.attachChild(heart3);
	    
	    fan = new AnimatedSprite(50,400,fanTR,this.sceneManager.engine.getVertexBufferObjectManager());
	    long[] frameDuration = {50,50,50,50,50,50};
	    fan.animate(frameDuration);
	    levelTwo.attachChild(fan);
	    fan.setVisible(false);
	    
	    star1 = new AnimatedSprite(210,425,star1TR,this.sceneManager.engine.getVertexBufferObjectManager())
		{
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				 if(this.collidesWith(balloon)){   
					 if(sceneManager.soundEnabled)
						 starSound.play();
					 this.detachSelf();
					 star1TR = null;
					 starCollected++;
					 scoreText.setText("Stars : "+ starCollected+" ");
					 levelTwo.unregisterUpdateHandler(this);
			      }
			    
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		star2 = new AnimatedSprite(300, 100, star2TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(balloon)) {
					if(sceneManager.soundEnabled)
						 starSound.play();
					this.detachSelf();
					star2TR = null;
					starCollected ++;
					scoreText.setText("Stars :" + starCollected + " ");
					levelTwo.unregisterUpdateHandler(this);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		star3 = new AnimatedSprite(750, 200, star3TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(balloon)) {
					if(sceneManager.soundEnabled)
						 starSound.play();
					this.detachSelf();
	
					star3TR = null;
					starCollected ++;
					scoreText.setText("Stars :" + starCollected + " ");
					levelTwo.unregisterUpdateHandler(this);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		long[] starFrameDuration = { 300, 150 };
		star1.animate(starFrameDuration);
		star2.animate(starFrameDuration);
		star3.animate(starFrameDuration);

		levelTwo.registerUpdateHandler(star1);
		levelTwo.registerUpdateHandler(star2);
		levelTwo.registerUpdateHandler(star3);

		scoreText = new Text(60, 460, font, "Stars : XX",sceneManager.engine.getVertexBufferObjectManager());
		scoreText.setText("Stars : 0 ");
		levelTwo.attachChild(scoreText);
		levelTwo.attachChild(star1);
		levelTwo.attachChild(star2);
		levelTwo.attachChild(star3);
	
		Sprite lOneImage = new Sprite(CAMERA_WIDTH / 2 - 20, CAMERA_HEIGHT / 2,	levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					
					fan.setVisible(true);
					fan.setPosition(pTouchAreaLocalX, pTouchAreaLocalY + 30);
					//balloonBody.setType(BodyType.DynamicBody);
					float vX = balloon.getX() - pTouchAreaLocalX + 30;
					float vY = balloon.getY() - pTouchAreaLocalY + 30;
					if (Math.abs(vX) <= 70 && Math.abs(vY) <= 70)
						balloonBody.setLinearVelocity(vX / 17, vY / 17);
				}
				if (pSceneTouchEvent.isActionUp()) {
					fan.setVisible(false);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}
		};
	    levelTwo.registerTouchArea(lOneImage);
		levelTwo.setAnchorCenter(0,0);
		this.sceneManager.camera.setCenter(CAMERA_WIDTH/2, CAMERA_HEIGHT/2);
		
		lWall  = new Rectangle(0,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
		rWall  = new Rectangle(795,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
		dWall  = new Rectangle(0,0,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
		uWall  = new Rectangle(0,475,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
		
		sharpPointRect1 = new Rectangle(0,210,620,60,this.sceneManager.engine.getVertexBufferObjectManager());
		
		sharpPointRect1.setColor(Color.TRANSPARENT);
		
		lWall.setColor(Color.WHITE);
		rWall.setColor(Color.WHITE);
		dWall.setColor(Color.WHITE);
		uWall.setColor(Color.WHITE);
		
		lWall.setAnchorCenter(0,0);
		rWall.setAnchorCenter(0,0);
		dWall.setAnchorCenter(0,0);
		uWall.setAnchorCenter(0,0);
		sharpPointRect1.setAnchorCenter(0, 0);
		
		
		
		lBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, lWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		rBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, rWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		uBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, uWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		dBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, dWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		
		levelTwo.attachChild(lWall);
		levelTwo.attachChild(rWall);
		levelTwo.attachChild(uWall);
		levelTwo.attachChild(dWall);
		levelTwo.attachChild(sharpPointRect1);
		
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lWall,lBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rWall,rBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(uWall,uBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dWall,dBody ));
		
		startGround = new Rectangle(0,265,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
		startGround.setAnchorCenter(0,0);
		startGround.setColor(Color.TRANSPARENT);
	   	
		//middleGround = new Rectangle(240,45,165,10,this.sceneManager.engine.getVertexBufferObjectManager());
		//middleGround.setAnchorCenter(0,0);
		//middleGround.setColor(Color.TRANSPARENT);
		
		exitGround = new Rectangle(0,0,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
		exitGround.setAnchorCenter(0,0);
		exitGround.setColor(Color.TRANSPARENT);
		
		startGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,startGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		//exitGroundGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,middleGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(startGround,startGroundBody ));
		//this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(middleGround,middleGroundBody ));
	
		levelTwo.attachChild(exitGround);
		//levelTwo.attachChild(middleGround);
		levelTwo.attachChild(startGround);
		  createGameOverResources();
	       
	
        balloon = new Sprite(30, 300, balloonTR,this.sceneManager.engine.getVertexBufferObjectManager()) {
			
		          	@Override
		          	protected void onManagedUpdate(float pSecondsElapsed) {
		          	
	                    if(this.collidesWith(sharpPointRect1))
	                    {
	                    	unregisterUpdateHandler(balloon);
	    					balloon.detachSelf();
	    					GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    					if(sceneManager.soundEnabled)
	    						balloonPop.play();
	    					checkForGameOver(GameLevels.LEVEL2,2);
	    					
	                    }
	                    
	                    if(this.collidesWith(exitGround))
	                    {
	                    	unregisterUpdateHandler(balloon);
							balloon.detachSelf();
							GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
							if (starCollected >= 2)
							{  
								if(sceneManager.soundEnabled)
									 happySound.play();
								createGameOverScene(starCollected, 2);
							}
							else
							{
								if(sceneManager.soundEnabled)
									 sadSound.play();
								createGameOverScene(0, 2);
							}
							
	                    }
   		          		super.onManagedUpdate(pSecondsElapsed);
		          	}
         };
         balloon.setAnchorCenter(0,0);
		
         balloonBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, balloon, BodyType.DynamicBody,BALLOON_FIX);
         this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon,balloonBody, true,false ));
         levelTwo.attachChild(balloon);
         levelTwo.registerUpdateHandler(this.sceneManager.mPhysicsWorld);
			
	
		return levelTwo;
	
		
	}
		
	public void loadLevelThreeResources(){
		// TODO Load Level 3 Resource 
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
		levelBackgroundTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),1024,1024);
		balloonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA, this.sceneManager.activity,"balloon.png",5,5);
		fanTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(levelBackgroundTA,this.sceneManager.activity,"fanSprite.png",125,5, 6, 1);
		levelBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA,this.sceneManager.activity,"three.png",5,125); 
		levelBackgroundTA.load();
		
		star1TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star1TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star1TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star1TA.load();
		
		star2TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star2TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star2TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star2TA.load();
		
		star3TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star3TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star3TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star3TA.load();
		
		heartTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),30, 30);
		heart1TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart2TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart3TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heartTA.load();   
		
		fontTR =  new BitmapTextureAtlas(sceneManager.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(sceneManager.engine.getFontManager(),fontTR, Typeface.SANS_SERIF,24,true, Color.WHITE);
	    font.load();    
		
		sceneManager.mPhysicsWorld = new PhysicsWorld(new Vector2(0,-SensorManager.GRAVITY_EARTH + 6.5f), false);
		sceneManager.WALL_FIX = PhysicsFactory.createFixtureDef(1,0,1);
		BALLOON_FIX=PhysicsFactory.createFixtureDef(1,0.3f,1);
	
	}
	
	public Scene createLevelThreeScene(){
		
		levelThree = new Scene();
		starCollected = 0;
		backgroundImage = new Sprite(0,0,levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager());
	    backgroundImage.setAnchorCenter(0,0);
	    levelThree.attachChild(backgroundImage);
	    
	    heart1 = new Sprite(690,455,heart1TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart1.setVisible(true);
	    levelThree.attachChild(heart1);
	    
	    heart2 = new Sprite(730,455,heart2TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart2.setVisible(true);
	    levelThree.attachChild(heart2);
	    
	    heart3 = new Sprite(770,455,heart3TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart3.setVisible(true);
	    levelThree.attachChild(heart3);
	    
	    fan = new AnimatedSprite(50,400,fanTR,this.sceneManager.engine.getVertexBufferObjectManager());
	    long[] frameDuration = {50,50,50,50,50,50};
	    fan.animate(frameDuration);
	    levelThree.attachChild(fan);
	    fan.setVisible(false);
	    
	    star1 = new AnimatedSprite(210,425,star1TR,this.sceneManager.engine.getVertexBufferObjectManager())
		{
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				 if(this.collidesWith(balloon)){   
					 if(sceneManager.soundEnabled)
						 starSound.play();
					 this.detachSelf();
					 star1TR = null;
					 starCollected++;
					 scoreText.setText("Stars : "+ starCollected+" ");
					 levelThree.unregisterUpdateHandler(this);
			      }
			    
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		star2 = new AnimatedSprite(300, 100, star2TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(balloon)) {
					if(sceneManager.soundEnabled)
						 starSound.play();
					this.detachSelf();
					star2TR = null;
					starCollected ++;
					scoreText.setText("Stars :" + starCollected + " ");
					levelThree.unregisterUpdateHandler(this);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		star3 = new AnimatedSprite(750, 200, star3TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(balloon)) {
					if(sceneManager.soundEnabled)
						 starSound.play();
					this.detachSelf();
	
					star3TR = null;
					starCollected ++;
					scoreText.setText("Stars :" + starCollected + " ");
					levelThree.unregisterUpdateHandler(this);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		long[] starFrameDuration = { 300, 150 };
		star1.animate(starFrameDuration);
		star2.animate(starFrameDuration);
		star3.animate(starFrameDuration);

		levelThree.registerUpdateHandler(star1);
		levelThree.registerUpdateHandler(star2);
		levelThree.registerUpdateHandler(star3);

		scoreText = new Text(60, 460, font, "Stars : XX",sceneManager.engine.getVertexBufferObjectManager());
		scoreText.setText("Stars : 0 ");
		levelThree.attachChild(scoreText);
		levelThree.attachChild(star1);
		levelThree.attachChild(star2);
		levelThree.attachChild(star3);
	
		Sprite lOneImage = new Sprite(CAMERA_WIDTH / 2 - 20, CAMERA_HEIGHT / 2,	levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					
					fan.setVisible(true);
					fan.setPosition(pTouchAreaLocalX, pTouchAreaLocalY + 30);
					//balloonBody.setType(BodyType.DynamicBody);
					float vX = balloon.getX() - pTouchAreaLocalX + 30;
					float vY = balloon.getY() - pTouchAreaLocalY + 30;
					if (Math.abs(vX) <= 70 && Math.abs(vY) <= 70)
						balloonBody.setLinearVelocity(vX / 17, vY / 17);
				}
				if (pSceneTouchEvent.isActionUp()) {
					fan.setVisible(false);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}
		};
	    levelThree.registerTouchArea(lOneImage);
		levelThree.setAnchorCenter(0,0);
		this.sceneManager.camera.setCenter(CAMERA_WIDTH/2, CAMERA_HEIGHT/2);
		
		lWall  = new Rectangle(0,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
		rWall  = new Rectangle(795,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
		dWall  = new Rectangle(0,0,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
		uWall  = new Rectangle(0,475,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
		
		smallWall1 = new Rectangle(210,0,10,305,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall2 = new Rectangle(500,175,10,305,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall3 = new Rectangle(0,0,215,25,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall4 = new Rectangle(615,240,190,30,this.sceneManager.engine.getVertexBufferObjectManager());
		sharpPointRect1 = new Rectangle(240,50,160,10,this.sceneManager.engine.getVertexBufferObjectManager());
		
		smallWall1.setColor(Color.TRANSPARENT);
		smallWall2.setColor(Color.TRANSPARENT);
		smallWall3.setColor(Color.TRANSPARENT);
		smallWall4.setColor(Color.TRANSPARENT);
		sharpPointRect1.setColor(Color.TRANSPARENT);
		
		lWall.setColor(Color.WHITE);
		rWall.setColor(Color.WHITE);
		dWall.setColor(Color.WHITE);
		uWall.setColor(Color.WHITE);
		
		lWall.setAnchorCenter(0,0);
		rWall.setAnchorCenter(0,0);
		dWall.setAnchorCenter(0,0);
		uWall.setAnchorCenter(0,0);
		smallWall1.setAnchorCenter(0, 0);
		smallWall2.setAnchorCenter(0,0);
		smallWall3.setAnchorCenter(0, 0);
		smallWall4.setAnchorCenter(0,0);
		sharpPointRect1.setAnchorCenter(0, 0);
		
		
		
		lBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, lWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		rBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, rWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		uBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, uWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		dBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, dWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody1 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall1,BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody2 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall2,BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody3 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall3,BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody4 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall4,BodyType.StaticBody,this.sceneManager.WALL_FIX);
		
		levelThree.attachChild(lWall);
		levelThree.attachChild(rWall);
		levelThree.attachChild(uWall);
		levelThree.attachChild(dWall);
		levelThree.attachChild(smallWall1);
		levelThree.attachChild(smallWall2);
		levelThree.attachChild(smallWall3);
		levelThree.attachChild(smallWall4);
		levelThree.attachChild(sharpPointRect1);
		
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lWall,lBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rWall,rBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(uWall,uBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dWall,dBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall1,smallWallBody1 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall2,smallWallBody2 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall3,smallWallBody3 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall4,smallWallBody4 ));
		
		startGround = new Rectangle(0,20,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
		startGround.setAnchorCenter(0,0);
		startGround.setColor(Color.TRANSPARENT);
	   	
		//middleGround = new Rectangle(240,45,165,10,this.sceneManager.engine.getVertexBufferObjectManager());
		//middleGround.setAnchorCenter(0,0);
		//middleGround.setColor(Color.TRANSPARENT);
		
		exitGround = new Rectangle(685,265,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
		exitGround.setAnchorCenter(0,0);
		exitGround.setColor(Color.TRANSPARENT);
		
		startGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,startGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		//exitGroundGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,middleGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(startGround,startGroundBody ));
		//this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(middleGround,middleGroundBody ));
	
		levelThree.attachChild(exitGround);
		//levelThree.attachChild(middleGround);
		levelThree.attachChild(startGround);
		  createGameOverResources();
	       
	
        balloon = new Sprite(30, 100, balloonTR,this.sceneManager.engine.getVertexBufferObjectManager()) {
			
		          	@Override
		          	protected void onManagedUpdate(float pSecondsElapsed) {
		          	
	                    if(this.collidesWith(sharpPointRect1))
	                    {
	                    	unregisterUpdateHandler(balloon);
	    					balloon.detachSelf();
	    					GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    					if(sceneManager.soundEnabled)
	    						balloonPop.play();
	    					checkForGameOver(GameLevels.LEVEL3,3);
	    					
	                    }
	                    
	                    if(this.collidesWith(exitGround))
	                    {
	                    	unregisterUpdateHandler(balloon);
							balloon.detachSelf();
							GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
							if (starCollected >= 2)
							{
								if(sceneManager.soundEnabled)
									 happySound.play();
								createGameOverScene(starCollected, 3);
							}
							else
							{
								if(sceneManager.soundEnabled)
									 sadSound.play();
								createGameOverScene(0, 3);
							}
							
	                    }
   		          		super.onManagedUpdate(pSecondsElapsed);
		          	}
         };
         balloon.setAnchorCenter(0,0);
		
         balloonBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, balloon, BodyType.DynamicBody,BALLOON_FIX);
         this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon,balloonBody, true,false ));
         levelThree.attachChild(balloon);
         levelThree.registerUpdateHandler(this.sceneManager.mPhysicsWorld);
			
	
		return levelThree;
	}
	
	public void loadLevelFourResources(){
		// TODO Load Level 4 Resource 
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
		levelBackgroundTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),1024,1024);
		balloonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA, this.sceneManager.activity,"balloon.png",5,5);
		fanTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(levelBackgroundTA,this.sceneManager.activity,"fanSprite.png",125,5, 6, 1);
		levelBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA,this.sceneManager.activity,"four.png",5,125); 
		levelBackgroundTA.load();
		
		star1TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star1TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star1TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star1TA.load();
		
		star2TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star2TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star2TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star2TA.load();
		
		star3TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star3TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star3TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star3TA.load();
		
		heartTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),30, 30);
		heart1TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart2TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart3TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heartTA.load();   
		
		fontTR =  new BitmapTextureAtlas(sceneManager.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(sceneManager.engine.getFontManager(),fontTR, Typeface.SANS_SERIF,24,true, Color.WHITE);
	    font.load();    
		
		sceneManager.mPhysicsWorld = new PhysicsWorld(new Vector2(0,-SensorManager.GRAVITY_EARTH + 6.5f), false);
		sceneManager.WALL_FIX = PhysicsFactory.createFixtureDef(1,0,1);
		BALLOON_FIX=PhysicsFactory.createFixtureDef(1,0.3f,1);
		
	}
	
	public Scene createLevelFourScene(){
        levelFour = new Scene();
        
        starCollected = 0;
		backgroundImage = new Sprite(0,0,levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager());
	    backgroundImage.setAnchorCenter(0,0);
	    levelFour.attachChild(backgroundImage);
	    
	    heart1 = new Sprite(690,455,heart1TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart1.setVisible(true);
	    levelFour.attachChild(heart1);
	    
	    heart2 = new Sprite(730,455,heart2TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart2.setVisible(true);
	    levelFour.attachChild(heart2);
	    
	    heart3 = new Sprite(770,455,heart3TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart3.setVisible(true);
	    levelFour.attachChild(heart3);
	    
	    fan = new AnimatedSprite(50,400,fanTR,this.sceneManager.engine.getVertexBufferObjectManager());
	    long[] frameDuration = {50,50,50,50,50,50};
	    fan.animate(frameDuration);
	    levelFour.attachChild(fan);
	    fan.setVisible(false);
	    
	    star1 = new AnimatedSprite(200,450,star1TR,this.sceneManager.engine.getVertexBufferObjectManager())
		{
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				 if(this.collidesWith(balloon)){   
					 if(sceneManager.soundEnabled)
						 starSound.play();
					 this.detachSelf();
					 star1TR = null;
					 starCollected++;
					 scoreText.setText("Stars : "+ starCollected+" ");
					 levelFour.unregisterUpdateHandler(this);
			      }
			    
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		star2 = new AnimatedSprite(400, 50, star2TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(balloon)) {
					if(sceneManager.soundEnabled)
						 starSound.play();
					this.detachSelf();
					star2TR = null;
					starCollected ++;
					scoreText.setText("Stars :" + starCollected + " ");
					levelFour.unregisterUpdateHandler(this);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		star3 = new AnimatedSprite(525, 400, star3TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(balloon)) {
					if(sceneManager.soundEnabled)
						 starSound.play();
					this.detachSelf();
	
					star3TR = null;
					starCollected ++;
					scoreText.setText("Stars :" + starCollected + " ");
					levelFour.unregisterUpdateHandler(this);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		long[] starFrameDuration = { 300, 150 };
		star1.animate(starFrameDuration);
		star2.animate(starFrameDuration);
		star3.animate(starFrameDuration);

		levelFour.registerUpdateHandler(star1);
		levelFour.registerUpdateHandler(star2);
		levelFour.registerUpdateHandler(star3);

		scoreText = new Text(60, 460, font, "Stars : XX",sceneManager.engine.getVertexBufferObjectManager());
		scoreText.setText("Stars : 0 ");
		levelFour.attachChild(scoreText);
		levelFour.attachChild(star1);
		levelFour.attachChild(star2);
		levelFour.attachChild(star3);
	
		Sprite lOneImage = new Sprite(CAMERA_WIDTH / 2 - 20, CAMERA_HEIGHT / 2,	levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					
					fan.setVisible(true);
					fan.setPosition(pTouchAreaLocalX, pTouchAreaLocalY + 30);
					//balloonBody.setType(BodyType.DynamicBody);
					float vX = balloon.getX() - pTouchAreaLocalX + 30;
					float vY = balloon.getY() - pTouchAreaLocalY + 30;
					if (Math.abs(vX) <= 70 && Math.abs(vY) <= 70)
						balloonBody.setLinearVelocity(vX / 17, vY / 17);
				}
				if (pSceneTouchEvent.isActionUp()) {
					fan.setVisible(false);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}
		};
	    levelFour.registerTouchArea(lOneImage);
		levelFour.setAnchorCenter(0,0);
		this.sceneManager.camera.setCenter(CAMERA_WIDTH/2, CAMERA_HEIGHT/2);
		
		lWall  = new Rectangle(0,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
		rWall  = new Rectangle(795,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
		dWall  = new Rectangle(0,0,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
		uWall  = new Rectangle(0,475,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
		
		smallWall1 = new Rectangle(150,0,20,150,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall2 = new Rectangle(150,320,20,160,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall3 = new Rectangle(620,0,20,150,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall4 = new Rectangle(620,250,20,230,this.sceneManager.engine.getVertexBufferObjectManager());
		
		sharpPointRect1 = new Rectangle(380,110,60,370,this.sceneManager.engine.getVertexBufferObjectManager());
		sharpPointRect2 = new Rectangle(600,250,10,110,this.sceneManager.engine.getVertexBufferObjectManager());
		
		smallWall1.setColor(Color.TRANSPARENT);
		smallWall2.setColor(Color.TRANSPARENT);
		smallWall3.setColor(Color.TRANSPARENT);
		smallWall4.setColor(Color.TRANSPARENT);
		
		sharpPointRect1.setColor(Color.TRANSPARENT);
		sharpPointRect2.setColor(Color.TRANSPARENT);
		
		lWall.setColor(Color.WHITE);
		rWall.setColor(Color.WHITE);
		dWall.setColor(Color.WHITE);
		uWall.setColor(Color.WHITE);
		
		lWall.setAnchorCenter(0,0);
		rWall.setAnchorCenter(0,0);
		dWall.setAnchorCenter(0,0);
		uWall.setAnchorCenter(0,0);
		
		smallWall1.setAnchorCenter(0, 0);
		smallWall2.setAnchorCenter(0,0);
		smallWall3.setAnchorCenter(0, 0);
		smallWall4.setAnchorCenter(0,0);
		sharpPointRect1.setAnchorCenter(0, 0);
		sharpPointRect2.setAnchorCenter(0,0);
		
		
		lBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, lWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		rBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, rWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		uBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, uWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		dBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, dWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody1 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall1,BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody2 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall2,BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody3 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall3,BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody4 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall4,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	
		levelFour.attachChild(lWall);
		levelFour.attachChild(rWall);
		levelFour.attachChild(uWall);
		levelFour.attachChild(dWall);
		levelFour.attachChild(smallWall1);
		levelFour.attachChild(smallWall2);
		levelFour.attachChild(smallWall3);
		levelFour.attachChild(smallWall4);
		levelFour.attachChild(sharpPointRect1);
		levelFour.attachChild(sharpPointRect2);
		
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lWall,lBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rWall,rBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(uWall,uBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dWall,dBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall1,smallWallBody1 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall2,smallWallBody2 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall3,smallWallBody3 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall4,smallWallBody4 ));
		
		startGround = new Rectangle(0,0,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
		startGround.setAnchorCenter(0,0);
		startGround.setColor(Color.TRANSPARENT);
	   	
		
		exitGround = new Rectangle(685,5,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
		exitGround.setAnchorCenter(0,0);
		exitGround.setColor(Color.TRANSPARENT);
		
		startGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,startGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		//middleGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,middleGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(startGround,startGroundBody ));
		//this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(middleGround,middleGroundBody ));
	
		levelFour.attachChild(exitGround);
		levelFour.attachChild(startGround);
	      createGameOverResources();
	      	
	
        balloon = new Sprite(30, 100, balloonTR,this.sceneManager.engine.getVertexBufferObjectManager()) {
			
		          	@Override
		          	protected void onManagedUpdate(float pSecondsElapsed) {
		          	
	                    if(this.collidesWith(sharpPointRect1))
	                    {
	                    	unregisterUpdateHandler(balloon);
	    					balloon.detachSelf();
	    					GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    					if(sceneManager.soundEnabled)
	    						balloonPop.play();
	    					checkForGameOver(GameLevels.LEVEL4,4);
	    					
	                    }
	                    if(this.collidesWith(sharpPointRect2))
	                    {
	                    	unregisterUpdateHandler(balloon);
	    					balloon.detachSelf();
	    					GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    					if(sceneManager.soundEnabled)
	    						balloonPop.play();
	    					checkForGameOver(GameLevels.LEVEL4,4);
	    					
	                    	
	                    }
	                    if(this.collidesWith(exitGround))
	                    {
	                    	unregisterUpdateHandler(balloon);
							balloon.detachSelf();
							GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
							if (starCollected >= 2)
							{
								if(sceneManager.soundEnabled)
									 happySound.play();
								createGameOverScene(starCollected, 4);
							}
							else
							{
								if(sceneManager.soundEnabled)
									 sadSound.play();
								createGameOverScene(0, 4);
							}
							
	                    }
   		          		super.onManagedUpdate(pSecondsElapsed);
		          	}
         };
         balloon.setAnchorCenter(0,0);
		
         balloonBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, balloon, BodyType.DynamicBody,BALLOON_FIX);
         this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon,balloonBody, true,false ));
         levelFour.attachChild(balloon);
         levelFour.registerUpdateHandler(this.sceneManager.mPhysicsWorld);
		
		return levelFour;
	}
	
	public void loadLevelFiveResources(){
		// TODO Load Level 5 Resource 
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
		levelBackgroundTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),1024,1024);
		balloonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA, this.sceneManager.activity,"balloon.png",5,5);
		fanTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(levelBackgroundTA,this.sceneManager.activity,"fanSprite.png",125,5, 6, 1);
		levelBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA,this.sceneManager.activity,"five.png",5,125); 
		levelBackgroundTA.load();
		
		star1TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star1TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star1TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star1TA.load();
		
		star2TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star2TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star2TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star2TA.load();
		
		star3TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star3TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star3TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star3TA.load();
		
		heartTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),30, 30);
		heart1TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart2TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart3TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heartTA.load();   
		
		fontTR =  new BitmapTextureAtlas(sceneManager.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(sceneManager.engine.getFontManager(),fontTR, Typeface.SANS_SERIF,24,true, Color.WHITE);
	    font.load();    
		
		sceneManager.mPhysicsWorld = new PhysicsWorld(new Vector2(0,-SensorManager.GRAVITY_EARTH + 6.5f), false);
		sceneManager.WALL_FIX = PhysicsFactory.createFixtureDef(1,0,1);
		BALLOON_FIX=PhysicsFactory.createFixtureDef(1,0.3f,1);

	}
	
	
public Scene createLevelFiveScene(){
		
		levelFive = new Scene();
		starCollected = 0;
		backgroundImage = new Sprite(0,0,levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager());
	    backgroundImage.setAnchorCenter(0,0);
	    levelFive.attachChild(backgroundImage);
	    
	    heart1 = new Sprite(690,455,heart1TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart1.setVisible(true);
	    levelFive.attachChild(heart1);
	    
	    heart2 = new Sprite(730,455,heart2TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart2.setVisible(true);
	    levelFive.attachChild(heart2);
	    
	    heart3 = new Sprite(770,455,heart3TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart3.setVisible(true);
	    levelFive.attachChild(heart3);
	    
	    fan = new AnimatedSprite(50,400,fanTR,this.sceneManager.engine.getVertexBufferObjectManager());
	    long[] frameDuration = {50,50,50,50,50,50};
	    fan.animate(frameDuration);
	    levelFive.attachChild(fan);
	    fan.setVisible(false);
	    
	    star1 = new AnimatedSprite(100,450,star1TR,this.sceneManager.engine.getVertexBufferObjectManager())
		{
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				 if(this.collidesWith(balloon)){  
					 if(sceneManager.soundEnabled)
						 starSound.play();
					 this.detachSelf();
					 star1TR = null;
					 starCollected++;
					 scoreText.setText("Stars : "+ starCollected+" ");
					 levelFive.unregisterUpdateHandler(this);
			      }
			    
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		star2 = new AnimatedSprite(400, 350, star2TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(balloon)) {
					if(sceneManager.soundEnabled)
						 starSound.play();
					this.detachSelf();
					star2TR = null;
					starCollected ++;
					scoreText.setText("Stars :" + starCollected + " ");
					levelFive.unregisterUpdateHandler(this);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		star3 = new AnimatedSprite(500, 75, star3TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(balloon)) {
					if(sceneManager.soundEnabled)
						 starSound.play();
					this.detachSelf();
	
					star3TR = null;
					starCollected ++;
					scoreText.setText("Stars :" + starCollected + " ");
					levelFive.unregisterUpdateHandler(this);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		long[] starFrameDuration = { 300, 150 };
		star1.animate(starFrameDuration);
		star2.animate(starFrameDuration);
		star3.animate(starFrameDuration);

		levelFive.registerUpdateHandler(star1);
		levelFive.registerUpdateHandler(star2);
		levelFive.registerUpdateHandler(star3);

		scoreText = new Text(60, 460, font, "Stars : XX",sceneManager.engine.getVertexBufferObjectManager());
		scoreText.setText("Stars : 0 ");
		levelFive.attachChild(scoreText);
		levelFive.attachChild(star1);
		levelFive.attachChild(star2);
		levelFive.attachChild(star3);
	
		Sprite lOneImage = new Sprite(CAMERA_WIDTH / 2 - 20, CAMERA_HEIGHT / 2,	levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					
					fan.setVisible(true);
					fan.setPosition(pTouchAreaLocalX, pTouchAreaLocalY + 30);
					//balloonBody.setType(BodyType.DynamicBody);
					float vX = balloon.getX() - pTouchAreaLocalX + 30;
					float vY = balloon.getY() - pTouchAreaLocalY + 30;
					if (Math.abs(vX) <= 70 && Math.abs(vY) <= 70)
						balloonBody.setLinearVelocity(vX / 17, vY / 17);
				}
				if (pSceneTouchEvent.isActionUp()) {
					fan.setVisible(false);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}
		};
	    levelFive.registerTouchArea(lOneImage);
		levelFive.setAnchorCenter(0,0);
		this.sceneManager.camera.setCenter(CAMERA_WIDTH/2, CAMERA_HEIGHT/2);
		
		lWall  = new Rectangle(0,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
		rWall  = new Rectangle(795,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
		dWall  = new Rectangle(0,0,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
		uWall  = new Rectangle(0,475,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
		
		smallWall1 = new Rectangle(170,0,25,175,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall2 = new Rectangle(390,0,25,260,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall3 = new Rectangle(600,80,200,25,this.sceneManager.engine.getVertexBufferObjectManager());
		sharpPointRect1 = new Rectangle(210, 25,390,10,this.sceneManager.engine.getVertexBufferObjectManager());
		sharpPointRect2 = new Rectangle(162, 328,35,40,this.sceneManager.engine.getVertexBufferObjectManager());
		sharpPointRect3 = new Rectangle(585,280,45,45,this.sceneManager.engine.getVertexBufferObjectManager());
		sharpPointRect4 = new Rectangle(400,300,10,10,this.sceneManager.engine.getVertexBufferObjectManager());
		
		smallWall1.setColor(Color.TRANSPARENT);
		smallWall2.setColor(Color.TRANSPARENT);
		smallWall3.setColor(Color.TRANSPARENT);
		sharpPointRect1.setColor(Color.TRANSPARENT);
		sharpPointRect2.setColor(Color.TRANSPARENT);
		sharpPointRect3.setColor(Color.TRANSPARENT);
		sharpPointRect4.setColor(Color.TRANSPARENT);
		
		lWall.setColor(Color.WHITE);
		rWall.setColor(Color.WHITE);
		dWall.setColor(Color.WHITE);
		uWall.setColor(Color.WHITE);
		
		lWall.setAnchorCenter(0,0);
		rWall.setAnchorCenter(0,0);
		dWall.setAnchorCenter(0,0);
		uWall.setAnchorCenter(0,0);
		smallWall1.setAnchorCenter(0, 0);
		smallWall2.setAnchorCenter(0,0);
		smallWall3.setAnchorCenter(0,0);
		sharpPointRect1.setAnchorCenter(0, 0);
		sharpPointRect2.setAnchorCenter(0, 0);
		sharpPointRect3.setAnchorCenter(0, 0);
		sharpPointRect4.setAnchorCenter(0, 0);
		
		lBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, lWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		rBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, rWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		uBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, uWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		dBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, dWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody1 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall1,BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody2 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall2,BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody3 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall3,BodyType.StaticBody,this.sceneManager.WALL_FIX);
		
		levelFive.attachChild(lWall);
		levelFive.attachChild(rWall);
		levelFive.attachChild(uWall);
		levelFive.attachChild(dWall);
		levelFive.attachChild(smallWall1);
		levelFive.attachChild(smallWall2);
		levelFive.attachChild(smallWall3);
		levelFive.attachChild(sharpPointRect1);
		levelFive.attachChild(sharpPointRect2);
		levelFive.attachChild(sharpPointRect3);
		levelFive.attachChild(sharpPointRect4);
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lWall,lBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rWall,rBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(uWall,uBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dWall,dBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall1,smallWallBody1 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall2,smallWallBody2 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall3,smallWallBody3 ));
		
		startGround = new Rectangle(0,0,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
		startGround.setAnchorCenter(0,0);
		startGround.setColor(Color.TRANSPARENT);
	   	
		
		exitGround = new Rectangle(685,100,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
		exitGround.setAnchorCenter(0,0);
		exitGround.setColor(Color.TRANSPARENT);
		
		startGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,startGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		//middleGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,middleGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(startGround,startGroundBody ));
		//this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(middleGround,middleGroundBody ));
	
		levelFive.attachChild(exitGround);
		levelFive.attachChild(startGround);
		
	
        balloon = new Sprite(30, 100, balloonTR,this.sceneManager.engine.getVertexBufferObjectManager()) {
			
		          	@Override
		          	protected void onManagedUpdate(float pSecondsElapsed) {
		          	
	                    
	                    if(this.collidesWith(exitGround))
	                    {
	                    	unregisterUpdateHandler(balloon);
							balloon.detachSelf();
							GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
							if (starCollected >= 2)
							{
								if(sceneManager.soundEnabled)
									 happySound.play();
								createGameOverScene(starCollected, 5);
							}
							else
							{
								if(sceneManager.soundEnabled)
									 sadSound.play();
								createGameOverScene(0, 5);
							}
						
	                    }
	                    if(this.collidesWith(sharpPointRect1))
	                    {
	                    	unregisterUpdateHandler(balloon);
		    				balloon.detachSelf();
		    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
		    				if(sceneManager.soundEnabled)
		    					balloonPop.play();
		    				checkForGameOver(GameLevels.LEVEL5,5);
	    				
	                    }
	                    if(this.collidesWith(sharpPointRect2))
	                    {
	                    	unregisterUpdateHandler(balloon);
		    				balloon.detachSelf();
		    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
		    				if(sceneManager.soundEnabled)
		    					balloonPop.play();
		    				checkForGameOver(GameLevels.LEVEL5,5);
	    				
	                    }
	                    if(this.collidesWith(sharpPointRect3))
	                    {
	                    	unregisterUpdateHandler(balloon);
		    				balloon.detachSelf();
		    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
		    				if(sceneManager.soundEnabled)
		    					balloonPop.play();
		    				checkForGameOver(GameLevels.LEVEL5,5);
	    					
	                    }
	                    if(this.collidesWith(sharpPointRect4))
	                    {
	                    	unregisterUpdateHandler(balloon);
		    				balloon.detachSelf();
		    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
		    				if(sceneManager.soundEnabled)
		    					balloonPop.play();
		    				checkForGameOver(GameLevels.LEVEL5,5);
	    					
	                    }
	                    
   		          		super.onManagedUpdate(pSecondsElapsed);
		          	}
         };
         balloon.setAnchorCenter(0,0);
		
         balloonBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, balloon, BodyType.DynamicBody,BALLOON_FIX);
         this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon,balloonBody, true,false ));
         levelFive.attachChild(balloon);
         createGameOverResources();
         levelFive.registerUpdateHandler(this.sceneManager.mPhysicsWorld);
			
	
		return levelFive;
	}
	
	
public void loadLevelSixResources(){
	// TODO Load Level 6 Resource 
	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
	levelBackgroundTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),1024,1024);
	balloonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA, this.sceneManager.activity,"balloon.png",5,5);
	fanTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(levelBackgroundTA,this.sceneManager.activity,"fanSprite.png",125,5, 6, 1);
	levelBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA,this.sceneManager.activity,"six.png",5,125); 
	levelBackgroundTA.load();
	
	star1TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
	star1TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star1TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
	star1TA.load();
	
	star2TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
	star2TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star2TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
	star2TA.load();
	
	star3TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
	star3TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star3TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
	star3TA.load();
	
	heartTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),30, 30);
	heart1TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
	heart2TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
	heart3TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
	heartTA.load();   
	
	fontTR =  new BitmapTextureAtlas(sceneManager.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
	font = new Font(sceneManager.engine.getFontManager(),fontTR, Typeface.SANS_SERIF,24,true, Color.WHITE);
    font.load();    
	
	sceneManager.mPhysicsWorld = new PhysicsWorld(new Vector2(0,-SensorManager.GRAVITY_EARTH + 6.5f), false);
	sceneManager.WALL_FIX = PhysicsFactory.createFixtureDef(1,0,1);
	BALLOON_FIX=PhysicsFactory.createFixtureDef(1,0.3f,1);
	
	}
	public Scene createLevelSixScene(){
	
		levelSix = new Scene();
		starCollected = 0;
		backgroundImage = new Sprite(0,0,levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager());
	    backgroundImage.setAnchorCenter(0,0);
	    levelSix.attachChild(backgroundImage);
	    
	    heart1 = new Sprite(690,455,heart1TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart1.setVisible(true);
	    levelSix.attachChild(heart1);
	    
	    heart2 = new Sprite(730,455,heart2TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart2.setVisible(true);
	    levelSix.attachChild(heart2);
	    
	    heart3 = new Sprite(770,455,heart3TR,this.sceneManager.engine.getVertexBufferObjectManager());
	    heart3.setVisible(true);
	    levelSix.attachChild(heart3);
	    
	    fan = new AnimatedSprite(50,400,fanTR,this.sceneManager.engine.getVertexBufferObjectManager());
	    long[] frameDuration = {50,50,50,50,50,50};
	    fan.animate(frameDuration);
	    levelSix.attachChild(fan);
	    fan.setVisible(false);
	    
	    star1 = new AnimatedSprite(260,200,star1TR,this.sceneManager.engine.getVertexBufferObjectManager())
		{
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				 if(this.collidesWith(balloon)){   
					 if(sceneManager.soundEnabled)
						 starSound.play();
					 this.detachSelf();
					 star1TR = null;
					 starCollected++;
					 scoreText.setText("Stars : "+ starCollected+" ");
					 levelSix.unregisterUpdateHandler(this);
			      }
			    
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		star2 = new AnimatedSprite(100, 350, star2TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(balloon)) {
					if(sceneManager.soundEnabled)
						 starSound.play();
					this.detachSelf();
					star2TR = null;
					starCollected ++;
					scoreText.setText("Stars :" + starCollected + " ");
					levelSix.unregisterUpdateHandler(this);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		star3 = new AnimatedSprite(550, 150, star3TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if (this.collidesWith(balloon)) {
					if(sceneManager.soundEnabled)
						 starSound.play();
					this.detachSelf();
	
					star3TR = null;
					starCollected ++;
					scoreText.setText("Stars :" + starCollected + " ");
					levelSix.unregisterUpdateHandler(this);
				}

				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		
		long[] starFrameDuration = { 300, 150 };
		star1.animate(starFrameDuration);
		star2.animate(starFrameDuration);
		star3.animate(starFrameDuration);

		levelSix.registerUpdateHandler(star1);
		levelSix.registerUpdateHandler(star2);
		levelSix.registerUpdateHandler(star3);

		scoreText = new Text(60, 460, font, "Stars : XX",sceneManager.engine.getVertexBufferObjectManager());
		scoreText.setText("Stars : 0 ");
		levelSix.attachChild(scoreText);
		levelSix.attachChild(star1);
		levelSix.attachChild(star2);
		levelSix.attachChild(star3);
	
		
		Sprite lOneImage = new Sprite(CAMERA_WIDTH / 2 - 20, CAMERA_HEIGHT / 2,	levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager()) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					
					fan.setVisible(true);
					fan.setPosition(pTouchAreaLocalX, pTouchAreaLocalY + 30);
					//balloonBody.setType(BodyType.DynamicBody);
					float vX = balloon.getX() - pTouchAreaLocalX + 30;
					float vY = balloon.getY() - pTouchAreaLocalY + 30;
					if (Math.abs(vX) <= 70 && Math.abs(vY) <= 70)
						balloonBody.setLinearVelocity(vX / 17, vY / 17);
				}
				if (pSceneTouchEvent.isActionUp()) {
					fan.setVisible(false);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}
		};
	    levelSix.registerTouchArea(lOneImage);
		levelSix.setAnchorCenter(0,0);
		this.sceneManager.camera.setCenter(CAMERA_WIDTH/2, CAMERA_HEIGHT/2);
		
		lWall  = new Rectangle(0,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
		rWall  = new Rectangle(795,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
		dWall  = new Rectangle(0,0,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
		uWall  = new Rectangle(0,475,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
		
		smallWall1 = new Rectangle(390,0,25,305,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall2 = new Rectangle(0,235,150,25,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall3 = new Rectangle(165,395,190,25,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall4 = new Rectangle(350,230,155,30,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall5 = new Rectangle(700,230,100,25,this.sceneManager.engine.getVertexBufferObjectManager());
		smallWall6 = new Rectangle(400,45,195,25,this.sceneManager.engine.getVertexBufferObjectManager());

		sharpPointRect1 = new Rectangle(190, 240, 10, 10,this.sceneManager.engine.getVertexBufferObjectManager());
		sharpPointRect2 = new Rectangle(255, 355, 10, 10,this.sceneManager.engine.getVertexBufferObjectManager());
		sharpPointRect3 = new Rectangle(300, 240, 10, 10,this.sceneManager.engine.getVertexBufferObjectManager());
		sharpPointRect4 = new Rectangle(515,90, 75, 10,this.sceneManager.engine.getVertexBufferObjectManager());
		sharpPointRect5 = new Rectangle(650, 240, 10, 10,this.sceneManager.engine.getVertexBufferObjectManager());		
		
		smallWall1.setColor(Color.TRANSPARENT);
		smallWall2.setColor(Color.TRANSPARENT);
		smallWall3.setColor(Color.TRANSPARENT);
		smallWall4.setColor(Color.TRANSPARENT);
		smallWall5.setColor(Color.TRANSPARENT);
		smallWall6.setColor(Color.TRANSPARENT);
		sharpPointRect1.setColor(Color.TRANSPARENT);
		sharpPointRect2.setColor(Color.TRANSPARENT);
		sharpPointRect3.setColor(Color.TRANSPARENT);
		sharpPointRect4.setColor(Color.TRANSPARENT);
		sharpPointRect5.setColor(Color.TRANSPARENT);
		
		lWall.setColor(Color.WHITE);
		rWall.setColor(Color.WHITE);
		dWall.setColor(Color.WHITE);
		uWall.setColor(Color.WHITE);
		lWall.setAnchorCenter(0,0);
		rWall.setAnchorCenter(0,0);
		dWall.setAnchorCenter(0,0);
		uWall.setAnchorCenter(0,0);
		smallWall1.setAnchorCenter(0, 0);
		smallWall2.setAnchorCenter(0, 0);
		smallWall3.setAnchorCenter(0, 0);
		smallWall4.setAnchorCenter(0, 0);
		smallWall5.setAnchorCenter(0, 0);
		smallWall6.setAnchorCenter(0, 0);
		sharpPointRect1.setAnchorCenter(0,0);
		sharpPointRect2.setAnchorCenter(0,0);
		sharpPointRect3.setAnchorCenter(0,0);
		sharpPointRect4.setAnchorCenter(0,0);
		sharpPointRect5.setAnchorCenter(0,0);
		
		
		lBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, lWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		rBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, rWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		uBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, uWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		dBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, dWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody1 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall1,BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody2 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall2, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody3 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall3, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody4 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall4, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody5 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall5, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		smallWallBody6 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall6, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		
		levelSix.attachChild(lWall);
		levelSix.attachChild(rWall);
		levelSix.attachChild(uWall);
		levelSix.attachChild(dWall);
		levelSix.attachChild(smallWall1);
		levelSix.attachChild(smallWall2);
		levelSix.attachChild(smallWall3);
		levelSix.attachChild(smallWall4);
		levelSix.attachChild(smallWall5);
		levelSix.attachChild(smallWall6);
		levelSix.attachChild(sharpPointRect1);
		levelSix.attachChild(sharpPointRect2);
		levelSix.attachChild(sharpPointRect3);
		levelSix.attachChild(sharpPointRect4);
		levelSix.attachChild(sharpPointRect5);
		
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lWall,lBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rWall,rBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(uWall,uBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dWall,dBody ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall1,smallWallBody1 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall2,smallWallBody2 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall3,smallWallBody3 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall4,smallWallBody4 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall5,smallWallBody5 ));
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall6,smallWallBody6 ));
		
		startGround = new Rectangle(0,0,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
		startGround.setAnchorCenter(0,0);
		startGround.setColor(Color.TRANSPARENT);
	   	
		
		exitGround = new Rectangle(685,0,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
		exitGround.setAnchorCenter(0,0);
		exitGround.setColor(Color.TRANSPARENT);
		
		startGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,startGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		//middleGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,middleGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
		
		this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(startGround,startGroundBody ));
		//this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(middleGround,middleGroundBody ));
	
		levelSix.attachChild(exitGround);
		levelSix.attachChild(startGround);
		
	    createGameOverResources();
	       
        balloon = new Sprite(30, 100, balloonTR,this.sceneManager.engine.getVertexBufferObjectManager()) {
			
		          	@Override
		          	protected void onManagedUpdate(float pSecondsElapsed) {
		          	
	                    if(this.collidesWith(sharpPointRect1))
	                    {   
	                    	unregisterUpdateHandler(balloon);
		    				balloon.detachSelf();
		    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
		    				if(sceneManager.soundEnabled)
		    					balloonPop.play();
		    				checkForGameOver(GameLevels.LEVEL6,6);
	    					
	    				}
	                   if(this.collidesWith(sharpPointRect2))
	                    {
	                    	this.detachSelf();
	    					unregisterUpdateHandler(balloon);
	    					GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    					balloonBody = null;
	    					if(sceneManager.soundEnabled)
	    						balloonPop.play();
	    					checkForGameOver(GameLevels.LEVEL6,6);
	                    }
	                    if(this.collidesWith(sharpPointRect3))
	                    {
	                    	this.detachSelf();
	                    	unregisterUpdateHandler(balloon);
	                    	GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	                    	balloonBody = null;
	                    	if(sceneManager.soundEnabled)
	                    		balloonPop.play();
	        				checkForGameOver(GameLevels.LEVEL6,6);
	                    }
	                    if(this.collidesWith(sharpPointRect4))
	                    {
	                    	this.detachSelf();
	                    	unregisterUpdateHandler(balloon);
	                    	GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	                    	balloonBody = null;
	                    	if(sceneManager.soundEnabled)
	                    		balloonPop.play();
	        				checkForGameOver(GameLevels.LEVEL6,6);
	                    }
	                    if(this.collidesWith(sharpPointRect5))
	                    {
	                    
	                        this.detachSelf();
	                        unregisterUpdateHandler(balloon);
	                    	GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	                    	balloonBody = null;
	                    	if(sceneManager.soundEnabled)
	                    		balloonPop.play();
	        				checkForGameOver(GameLevels.LEVEL6,6);
	                    }
	                     
	                    if(this.collidesWith(exitGround))
	                    {
	                    	unregisterUpdateHandler(balloon);
	                    	balloon.detachSelf();
							GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
							balloonBody = null;
							if (starCollected >= 2)
							{
								if(sceneManager.soundEnabled)
									 happySound.play();
								createGameOverScene(starCollected, 6);
							}
							else
							{
								if(sceneManager.soundEnabled)
									 sadSound.play();
								createGameOverScene(0, 6);
							}
							
	                    }
   		          		super.onManagedUpdate(pSecondsElapsed);
		          	}
         };
         balloon.setAnchorCenter(0,0);
		
         balloonBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, balloon, BodyType.DynamicBody,BALLOON_FIX);
         
         this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon,balloonBody, true,false ));
         levelSix.attachChild(balloon);
         levelSix.registerUpdateHandler(this.sceneManager.mPhysicsWorld);
		
		return levelSix;
	}
    
	public void loadLevelSevenResources(){
		// TODO Load Level 7 Resource 
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
		levelBackgroundTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),1024,1024);
		balloonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA, this.sceneManager.activity,"balloon.png",5,5);
		fanTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(levelBackgroundTA,this.sceneManager.activity,"fanSprite.png",125,5, 6, 1);
		levelBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA,this.sceneManager.activity,"seven.png",5,125); 
		levelBackgroundTA.load();
		
		star1TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star1TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star1TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star1TA.load();
		
		star2TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star2TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star2TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star2TA.load();
		
		star3TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star3TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star3TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star3TA.load();
		
		heartTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),30, 30);
		heart1TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart2TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart3TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heartTA.load();   
		
		fontTR =  new BitmapTextureAtlas(sceneManager.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(sceneManager.engine.getFontManager(),fontTR, Typeface.SANS_SERIF,24,true, Color.WHITE);
	    font.load();    
		
		sceneManager.mPhysicsWorld = new PhysicsWorld(new Vector2(0,-SensorManager.GRAVITY_EARTH + 6.5f), false);
		sceneManager.WALL_FIX = PhysicsFactory.createFixtureDef(1,0,1);
		BALLOON_FIX=PhysicsFactory.createFixtureDef(1,0.3f,1);
	
		
	}
	public Scene createLevelSevenScene(){
		
    levelSeven = new Scene();
 	starCollected = 0;
	backgroundImage = new Sprite(0,0,levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager());
    backgroundImage.setAnchorCenter(0,0);
    levelSeven.attachChild(backgroundImage);
    
    heart1 = new Sprite(690,455,heart1TR,this.sceneManager.engine.getVertexBufferObjectManager());
    heart1.setVisible(true);
    levelSeven.attachChild(heart1);
    
    heart2 = new Sprite(730,455,heart2TR,this.sceneManager.engine.getVertexBufferObjectManager());
    heart2.setVisible(true);
    levelSeven.attachChild(heart2);
    
    heart3 = new Sprite(770,455,heart3TR,this.sceneManager.engine.getVertexBufferObjectManager());
    heart3.setVisible(true);
    levelSeven.attachChild(heart3);
    
    fan = new AnimatedSprite(50,400,fanTR,this.sceneManager.engine.getVertexBufferObjectManager());
    long[] frameDuration = {50,50,50,50,50,50};
    fan.animate(frameDuration);
    levelSeven.attachChild(fan);
    fan.setVisible(false);
    
    star1 = new AnimatedSprite(250,450,star1TR,this.sceneManager.engine.getVertexBufferObjectManager())
	{
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			 if(this.collidesWith(balloon)){ 
				 if(sceneManager.soundEnabled)
					 starSound.play();
				 this.detachSelf();
				 star1TR = null;
				 starCollected++;
				 scoreText.setText("Stars : "+ starCollected+" ");
				 levelSeven.unregisterUpdateHandler(this);
		      }
		    
			super.onManagedUpdate(pSecondsElapsed);
		}
	};
	
	star2 = new AnimatedSprite(340, 100, star2TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
		
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			if (this.collidesWith(balloon)) {
				if(sceneManager.soundEnabled)
					 starSound.play();
				this.detachSelf();
				star2TR = null;
				starCollected ++;
				scoreText.setText("Stars :" + starCollected + " ");
				levelSeven.unregisterUpdateHandler(this);
			}

			super.onManagedUpdate(pSecondsElapsed);
		}
	};
	
	star3 = new AnimatedSprite(725, 250, star3TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
		
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			if (this.collidesWith(balloon)) {
				if(sceneManager.soundEnabled)
					 starSound.play();
				this.detachSelf();

				star3TR = null;
				starCollected ++;
				scoreText.setText("Stars :" + starCollected + " ");
				levelSeven.unregisterUpdateHandler(this);
			}

			super.onManagedUpdate(pSecondsElapsed);
		}
	};
	
	long[] starFrameDuration = { 300, 150 };
	star1.animate(starFrameDuration);
	star2.animate(starFrameDuration);
	star3.animate(starFrameDuration);

	levelSeven.registerUpdateHandler(star1);
	levelSeven.registerUpdateHandler(star2);
	levelSeven.registerUpdateHandler(star3);

	scoreText = new Text(60, 460, font, "Stars : XX",sceneManager.engine.getVertexBufferObjectManager());
	scoreText.setText("Stars : 0 ");
	levelSeven.attachChild(scoreText);
	levelSeven.attachChild(star1);
	levelSeven.attachChild(star2);
	levelSeven.attachChild(star3);

	Sprite lOneImage = new Sprite(CAMERA_WIDTH / 2 - 20, CAMERA_HEIGHT / 2,	levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager()) {

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionDown()) {
				
				fan.setVisible(true);
				fan.setPosition(pTouchAreaLocalX, pTouchAreaLocalY + 30);
				//balloonBody.setType(BodyType.DynamicBody);
				float vX = balloon.getX() - pTouchAreaLocalX + 30;
				float vY = balloon.getY() - pTouchAreaLocalY + 30;
				if (Math.abs(vX) <= 70 && Math.abs(vY) <= 70)
					balloonBody.setLinearVelocity(vX / 17, vY / 17);
			}
			if (pSceneTouchEvent.isActionUp()) {
				fan.setVisible(false);
			}
			
			return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
					pTouchAreaLocalY);
		}
	};
    levelSeven.registerTouchArea(lOneImage);
	levelSeven.setAnchorCenter(0,0);
	this.sceneManager.camera.setCenter(CAMERA_WIDTH/2, CAMERA_HEIGHT/2);
	
	lWall  = new Rectangle(0,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
	rWall  = new Rectangle(795,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
	dWall  = new Rectangle(0,0,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
	uWall  = new Rectangle(0,475,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
	
	smallWall1 = new Rectangle(140,0,30,250,this.sceneManager.engine.getVertexBufferObjectManager());
	smallWall2 = new Rectangle(325,225,30,255,this.sceneManager.engine.getVertexBufferObjectManager());
	smallWall3 = new Rectangle(515,0,30,250,this.sceneManager.engine.getVertexBufferObjectManager());
	smallWall4 = new Rectangle(685,295,70,25,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect1 = new Rectangle(155,300,10,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect2 = new Rectangle(340,170,10,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect3 = new Rectangle(530,300,10,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect4 = new Rectangle(560,30,235,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect5 = new Rectangle(690,280,60,10,this.sceneManager.engine.getVertexBufferObjectManager());
	
	smallWall1.setColor(Color.TRANSPARENT);
	smallWall2.setColor(Color.TRANSPARENT);
	smallWall3.setColor(Color.TRANSPARENT);
	smallWall4.setColor(Color.TRANSPARENT);
	sharpPointRect1.setColor(Color.TRANSPARENT);
	sharpPointRect2.setColor(Color.TRANSPARENT);
	sharpPointRect3.setColor(Color.TRANSPARENT);
	sharpPointRect4.setColor(Color.TRANSPARENT);
	sharpPointRect5.setColor(Color.TRANSPARENT);
	
	lWall.setColor(Color.WHITE);
	rWall.setColor(Color.WHITE);
	dWall.setColor(Color.WHITE);
	uWall.setColor(Color.WHITE);
	lWall.setAnchorCenter(0,0);
	rWall.setAnchorCenter(0,0);
	dWall.setAnchorCenter(0,0);
	uWall.setAnchorCenter(0,0);
	smallWall1.setAnchorCenter(0, 0);
	smallWall2.setAnchorCenter(0,0);
	smallWall3.setAnchorCenter(0,0);
	smallWall4.setAnchorCenter(0,0);
	sharpPointRect1.setAnchorCenter(0,0);
	sharpPointRect2.setAnchorCenter(0,0);
	sharpPointRect3.setAnchorCenter(0,0);
	sharpPointRect4.setAnchorCenter(0,0);
	sharpPointRect5.setAnchorCenter(0,0);
	
	
	lBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, lWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	rBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, rWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	uBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, uWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	dBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, dWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody1 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall1,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody2 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall2,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody3 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall3,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody4 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall4,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	
	levelSeven.attachChild(lWall);
	levelSeven.attachChild(rWall);
	levelSeven.attachChild(uWall);
	levelSeven.attachChild(dWall);
	levelSeven.attachChild(smallWall1);
	levelSeven.attachChild(smallWall2);
	levelSeven.attachChild(smallWall3);
	levelSeven.attachChild(smallWall4);
	levelSeven.attachChild(sharpPointRect1);
	levelSeven.attachChild(sharpPointRect2);
	levelSeven.attachChild(sharpPointRect3);
	levelSeven.attachChild(sharpPointRect4);
	levelSeven.attachChild(sharpPointRect5);
	
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lWall,lBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rWall,rBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(uWall,uBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dWall,dBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall1,smallWallBody1 ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall2,smallWallBody2 ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall3,smallWallBody3 ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall4,smallWallBody4 ));
	
	startGround = new Rectangle(0,0,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
	startGround.setAnchorCenter(0,0);
	startGround.setColor(Color.TRANSPARENT);
   	
	
	exitGround = new Rectangle(605,65,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
	exitGround.setAnchorCenter(0,0);
	exitGround.setColor(Color.TRANSPARENT);
	
	startGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,startGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	//middleGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,middleGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(startGround,startGroundBody ));
	//this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(middleGround,middleGroundBody ));

	levelSeven.attachChild(exitGround);
	levelSeven.attachChild(startGround);
	

    balloon = new Sprite(30, 100, balloonTR,this.sceneManager.engine.getVertexBufferObjectManager()) {
		
	          	@Override
	          	protected void onManagedUpdate(float pSecondsElapsed) {
	          	
                 
	          	  if(this.collidesWith(sharpPointRect1))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
	    				checkForGameOver(GameLevels.LEVEL7,7);
  					
  				  }
	          	  if(this.collidesWith(sharpPointRect2))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
	    				checkForGameOver(GameLevels.LEVEL7,7);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect3))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
	    				checkForGameOver(GameLevels.LEVEL7,7);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect4))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
	    				checkForGameOver(GameLevels.LEVEL7,7);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect5))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL7,7);
  					
  				}
                
                    if(this.collidesWith(exitGround))
                    {
                    	unregisterUpdateHandler(balloon);
						balloon.detachSelf();
						GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
						if (starCollected >= 2)
						{
							if(sceneManager.soundEnabled)
								 happySound.play();
							createGameOverScene(starCollected, 7);
						}
						else
						{
							if(sceneManager.soundEnabled)
								 sadSound.play();
							createGameOverScene(0, 7);
						}
						
                    }
		          		super.onManagedUpdate(pSecondsElapsed);
	          	}
     };
     balloon.setAnchorCenter(0,0);
	
     balloonBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, balloon, BodyType.DynamicBody,BALLOON_FIX);
     this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon,balloonBody, true,false ));
     levelSeven.attachChild(balloon);
     createGameOverResources();
     levelSeven.registerUpdateHandler(this.sceneManager.mPhysicsWorld);
		


     
     return levelSeven;
	}
	public void loadLevelEightResources(){
		// TODO Load Level 8 Resource 
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
		levelBackgroundTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),1024,1024);
		balloonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA, this.sceneManager.activity,"balloon.png",5,5);
		fanTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(levelBackgroundTA,this.sceneManager.activity,"fanSprite.png",125,5, 6, 1);
		levelBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA,this.sceneManager.activity,"eight.png",5,125); 
		levelBackgroundTA.load();
		
		star1TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star1TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star1TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star1TA.load();
		
		star2TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star2TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star2TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star2TA.load();
		
		star3TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star3TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star3TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star3TA.load();
		
		heartTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),30, 30);
		heart1TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart2TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart3TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heartTA.load();   
		
		fontTR =  new BitmapTextureAtlas(sceneManager.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(sceneManager.engine.getFontManager(),fontTR, Typeface.SANS_SERIF,24,true, Color.WHITE);
	    font.load();    
		
		sceneManager.mPhysicsWorld = new PhysicsWorld(new Vector2(0,-SensorManager.GRAVITY_EARTH + 6.5f), false);
		sceneManager.WALL_FIX = PhysicsFactory.createFixtureDef(1,0,1);
		BALLOON_FIX=PhysicsFactory.createFixtureDef(1,0.3f,1);
	
		
	}

	public Scene createLevelEightScene(){
		
    levelEight = new Scene();
 	starCollected = 0;
	backgroundImage = new Sprite(0,0,levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager());
    backgroundImage.setAnchorCenter(0,0);
    levelEight.attachChild(backgroundImage);
    
    heart1 = new Sprite(690,455,heart1TR,this.sceneManager.engine.getVertexBufferObjectManager());
    heart1.setVisible(true);
    levelEight.attachChild(heart1);
    
    heart2 = new Sprite(730,455,heart2TR,this.sceneManager.engine.getVertexBufferObjectManager());
    heart2.setVisible(true);
    levelEight.attachChild(heart2);
    
    heart3 = new Sprite(770,455,heart3TR,this.sceneManager.engine.getVertexBufferObjectManager());
    heart3.setVisible(true);
    levelEight.attachChild(heart3);
    
    fan = new AnimatedSprite(50,400,fanTR,this.sceneManager.engine.getVertexBufferObjectManager());
    long[] frameDuration = {50,50,50,50,50,50};
    fan.animate(frameDuration);
    levelEight.attachChild(fan);
    fan.setVisible(false);
    
    star1 = new AnimatedSprite(50,175,star1TR,this.sceneManager.engine.getVertexBufferObjectManager())
	{
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			 if(this.collidesWith(balloon)){   
				 if(sceneManager.soundEnabled)
					 starSound.play();
				 this.detachSelf();
				 star1TR = null;
				 starCollected++;
				 scoreText.setText("Stars : "+ starCollected+" ");
				 levelEight.unregisterUpdateHandler(this);
		      }
		    
			super.onManagedUpdate(pSecondsElapsed);
		}
	};
	
	star2 = new AnimatedSprite(395, 260, star2TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
		
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			if (this.collidesWith(balloon)) {
				if(sceneManager.soundEnabled)
					 starSound.play();
				this.detachSelf();
				star2TR = null;
				starCollected ++;
				scoreText.setText("Stars :" + starCollected + " ");
				levelEight.unregisterUpdateHandler(this);
			}

			super.onManagedUpdate(pSecondsElapsed);
		}
	};
	
	star3 = new AnimatedSprite(750, 200, star3TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
		
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			if (this.collidesWith(balloon)) {
				if(sceneManager.soundEnabled)
					 starSound.play();
				this.detachSelf();

				star3TR = null;
				starCollected ++;
				scoreText.setText("Stars :" + starCollected + " ");
				levelEight.unregisterUpdateHandler(this);
			}

			super.onManagedUpdate(pSecondsElapsed);
		}
	};
	
	long[] starFrameDuration = { 300, 150 };
	star1.animate(starFrameDuration);
	star2.animate(starFrameDuration);
	star3.animate(starFrameDuration);

	levelEight.registerUpdateHandler(star1);
	levelEight.registerUpdateHandler(star2);
	levelEight.registerUpdateHandler(star3);

	scoreText = new Text(60, 460, font, "Stars : XX",sceneManager.engine.getVertexBufferObjectManager());
	scoreText.setText("Stars : 0 ");
	levelEight.attachChild(scoreText);
	levelEight.attachChild(star1);
	levelEight.attachChild(star2);
	levelEight.attachChild(star3);

	Sprite lOneImage = new Sprite(CAMERA_WIDTH / 2 - 20, CAMERA_HEIGHT / 2,	levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager()) {

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionDown()) {
				
				fan.setVisible(true);
				fan.setPosition(pTouchAreaLocalX, pTouchAreaLocalY + 30);
				//balloonBody.setType(BodyType.DynamicBody);
				float vX = balloon.getX() - pTouchAreaLocalX + 30;
				float vY = balloon.getY() - pTouchAreaLocalY + 30;
				if (Math.abs(vX) <= 70 && Math.abs(vY) <= 70)
					balloonBody.setLinearVelocity(vX / 17, vY / 17);
			}
			if (pSceneTouchEvent.isActionUp()) {
				fan.setVisible(false);
			}
			
			return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
					pTouchAreaLocalY);
		}
	};
    levelEight.registerTouchArea(lOneImage);
	levelEight.setAnchorCenter(0,0);
	this.sceneManager.camera.setCenter(CAMERA_WIDTH/2, CAMERA_HEIGHT/2);
	
	lWall  = new Rectangle(0,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
	rWall  = new Rectangle(795,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
	dWall  = new Rectangle(0,0,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
	uWall  = new Rectangle(0,475,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
	
	smallWall1 = new Rectangle(240,0,30,180,this.sceneManager.engine.getVertexBufferObjectManager());
	smallWall2 = new Rectangle(485,230,30,255,this.sceneManager.engine.getVertexBufferObjectManager());
	smallWall3 = new Rectangle(365,305,75,25,this.sceneManager.engine.getVertexBufferObjectManager());
	smallWall4 = new Rectangle(685,265,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect1 = new Rectangle(0,0,800,30,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect2 = new Rectangle(250,220,10,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect3 = new Rectangle(500,170,10,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect4 = new Rectangle(375,293,60,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect5 = new Rectangle(695,225,100,10,this.sceneManager.engine.getVertexBufferObjectManager());
	
	smallWall1.setColor(Color.TRANSPARENT);
	smallWall2.setColor(Color.TRANSPARENT);
	smallWall3.setColor(Color.TRANSPARENT);
	smallWall4.setColor(Color.TRANSPARENT);
	sharpPointRect1.setColor(Color.TRANSPARENT);
	sharpPointRect2.setColor(Color.TRANSPARENT);
	sharpPointRect3.setColor(Color.TRANSPARENT);
	sharpPointRect4.setColor(Color.TRANSPARENT);
	sharpPointRect5.setColor(Color.TRANSPARENT);
	
	lWall.setColor(Color.WHITE);
	rWall.setColor(Color.WHITE);
	dWall.setColor(Color.WHITE);
	uWall.setColor(Color.WHITE);
	lWall.setAnchorCenter(0,0);
	rWall.setAnchorCenter(0,0);
	dWall.setAnchorCenter(0,0);
	uWall.setAnchorCenter(0,0);
	smallWall1.setAnchorCenter(0,0);
	smallWall2.setAnchorCenter(0,0);
	smallWall3.setAnchorCenter(0,0);
	smallWall4.setAnchorCenter(0,0);
	sharpPointRect1.setAnchorCenter(0,0);
	sharpPointRect2.setAnchorCenter(0,0);
	sharpPointRect3.setAnchorCenter(0,0);
	sharpPointRect4.setAnchorCenter(0,0);
	sharpPointRect5.setAnchorCenter(0,0);
	
	
	lBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, lWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	rBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, rWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	uBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, uWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	dBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, dWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody1 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall1,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody2 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall2,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody3 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall3,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody4 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall4,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	
	levelEight.attachChild(lWall);
	levelEight.attachChild(rWall);
	levelEight.attachChild(uWall);
	levelEight.attachChild(dWall);
	levelEight.attachChild(smallWall1);
	levelEight.attachChild(smallWall2);
	levelEight.attachChild(smallWall3);
	levelEight.attachChild(smallWall4);
	levelEight.attachChild(sharpPointRect1);
	levelEight.attachChild(sharpPointRect2);
	levelEight.attachChild(sharpPointRect3);
	levelEight.attachChild(sharpPointRect4);
	levelEight.attachChild(sharpPointRect5);
	
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lWall,lBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rWall,rBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(uWall,uBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dWall,dBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall1,smallWallBody1 ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall2,smallWallBody2 ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall3,smallWallBody3 ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall4,smallWallBody4 ));
	
	startGround = new Rectangle(0,215,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
	startGround.setAnchorCenter(0,0);
	startGround.setColor(Color.TRANSPARENT);
   	
	
	exitGround = new Rectangle(685,275,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
	exitGround.setAnchorCenter(0,0);
	exitGround.setColor(Color.TRANSPARENT);
	
	startGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,startGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	//middleGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,middleGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(startGround,startGroundBody ));
	//this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(middleGround,middleGroundBody ));

	levelEight.attachChild(exitGround);
	levelEight.attachChild(startGround);
	

    balloon = new Sprite(30, 300, balloonTR,this.sceneManager.engine.getVertexBufferObjectManager()) {
		
	          	@Override
	          	protected void onManagedUpdate(float pSecondsElapsed) {
	          	
                 
	          	  if(this.collidesWith(sharpPointRect1))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL8,8);
  					
  				  }
	          	  if(this.collidesWith(sharpPointRect2))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL8,8);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect3))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL8,8);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect4))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL8,8);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect5))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL8,8);
  					
  				}
                
                    if(this.collidesWith(exitGround))
                    {
                    	unregisterUpdateHandler(balloon);
						balloon.detachSelf();
						GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
						if (starCollected >= 2)
						{
							if(sceneManager.soundEnabled)
								 happySound.play();
							createGameOverScene(starCollected, 8);
						}
						else
						{
							if(sceneManager.soundEnabled)
								 sadSound.play();
							createGameOverScene(0, 8);
						}
						
                    }
		          		super.onManagedUpdate(pSecondsElapsed);
	          	}
     };
     balloon.setAnchorCenter(0,0);
	
     balloonBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, balloon, BodyType.DynamicBody,BALLOON_FIX);
     this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon,balloonBody, true,false ));
     levelEight.attachChild(balloon);
     createGameOverResources();
     levelEight.registerUpdateHandler(this.sceneManager.mPhysicsWorld);
     
     return levelEight;
	}
	
	public void loadLevelNineResources(){
		// TODO Load Level 9 Resource 
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
		levelBackgroundTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),1024,1024);
		balloonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA, this.sceneManager.activity,"balloon.png",5,5);
		fanTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(levelBackgroundTA,this.sceneManager.activity,"fanSprite.png",125,5, 6, 1);
		levelBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA,this.sceneManager.activity,"nine.png",5,125); 
		levelBackgroundTA.load();
		
		star1TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star1TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star1TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star1TA.load();
		
		star2TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star2TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star2TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star2TA.load();
		
		star3TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star3TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star3TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star3TA.load();
		
		heartTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),30, 30);
		heart1TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart2TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart3TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heartTA.load();   
		
		fontTR =  new BitmapTextureAtlas(sceneManager.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(sceneManager.engine.getFontManager(),fontTR, Typeface.SANS_SERIF,24,true, Color.WHITE);
	    font.load();    
		
		sceneManager.mPhysicsWorld = new PhysicsWorld(new Vector2(0,-SensorManager.GRAVITY_EARTH + 6.5f), false);
		sceneManager.WALL_FIX = PhysicsFactory.createFixtureDef(1,0,1);
		BALLOON_FIX=PhysicsFactory.createFixtureDef(1,0.3f,1);
	
		
	}
	public Scene createLevelNineScene(){		
    levelNine = new Scene();
 	starCollected = 0;
	backgroundImage = new Sprite(0,0,levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager());
    backgroundImage.setAnchorCenter(0,0);
    levelNine.attachChild(backgroundImage);
    
    heart1 = new Sprite(690,455,heart1TR,this.sceneManager.engine.getVertexBufferObjectManager());
    heart1.setVisible(true);
    levelNine.attachChild(heart1);
    
    heart2 = new Sprite(730,455,heart2TR,this.sceneManager.engine.getVertexBufferObjectManager());
    heart2.setVisible(true);
    levelNine.attachChild(heart2);
    
    heart3 = new Sprite(770,455,heart3TR,this.sceneManager.engine.getVertexBufferObjectManager());
    heart3.setVisible(true);
    levelNine.attachChild(heart3);
    
    fan = new AnimatedSprite(50,400,fanTR,this.sceneManager.engine.getVertexBufferObjectManager());
    long[] frameDuration = {50,50,50,50,50,50};
    fan.animate(frameDuration);
    levelNine.attachChild(fan);
    fan.setVisible(false);
    
    star1 = new AnimatedSprite(350,450,star1TR,this.sceneManager.engine.getVertexBufferObjectManager())
	{
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			 if(this.collidesWith(balloon)){ 
				 if(sceneManager.soundEnabled)
					 starSound.play();
				 this.detachSelf();
				 star1TR = null;
				 starCollected++;
				 scoreText.setText("Stars : "+ starCollected+" ");
				 levelNine.unregisterUpdateHandler(this);
		      }
		    
			super.onManagedUpdate(pSecondsElapsed);
		}
	};
	
	star2 = new AnimatedSprite(300, 175, star2TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
		
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			if (this.collidesWith(balloon)) {
				if(sceneManager.soundEnabled)
					 starSound.play();
				this.detachSelf();
				star2TR = null;
				starCollected ++;
				scoreText.setText("Stars :" + starCollected + " ");
				levelNine.unregisterUpdateHandler(this);
			}

			super.onManagedUpdate(pSecondsElapsed);
		}
	};
	
	star3 = new AnimatedSprite(750, 250, star3TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
		
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			if (this.collidesWith(balloon)) {
				if(sceneManager.soundEnabled)
					 starSound.play();
				this.detachSelf();

				star3TR = null;
				starCollected ++;
				scoreText.setText("Stars :" + starCollected + " ");
				levelNine.unregisterUpdateHandler(this);
			}

			super.onManagedUpdate(pSecondsElapsed);
		}
	};
	
	long[] starFrameDuration = { 300, 150 };
	star1.animate(starFrameDuration);
	star2.animate(starFrameDuration);
	star3.animate(starFrameDuration);

	levelNine.registerUpdateHandler(star1);
	levelNine.registerUpdateHandler(star2);
	levelNine.registerUpdateHandler(star3);

	scoreText = new Text(60, 460, font, "Stars : XX",sceneManager.engine.getVertexBufferObjectManager());
	scoreText.setText("Stars : 0 ");
	levelNine.attachChild(scoreText);
	levelNine.attachChild(star1);
	levelNine.attachChild(star2);
	levelNine.attachChild(star3);

	Sprite lOneImage = new Sprite(CAMERA_WIDTH / 2 - 20, CAMERA_HEIGHT / 2,	levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager()) {

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionDown()) {
				
				fan.setVisible(true);
				fan.setPosition(pTouchAreaLocalX, pTouchAreaLocalY + 30);
				//balloonBody.setType(BodyType.DynamicBody);
				float vX = balloon.getX() - pTouchAreaLocalX + 30;
				float vY = balloon.getY() - pTouchAreaLocalY + 30;
				if (Math.abs(vX) <= 70 && Math.abs(vY) <= 70)
					balloonBody.setLinearVelocity(vX / 17, vY / 17);
			}
			if (pSceneTouchEvent.isActionUp()) {
				fan.setVisible(false);
			}
			
			return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
					pTouchAreaLocalY);
		}
	};
    levelNine.registerTouchArea(lOneImage);
	levelNine.setAnchorCenter(0,0);
	this.sceneManager.camera.setCenter(CAMERA_WIDTH/2, CAMERA_HEIGHT/2);
	
	lWall  = new Rectangle(0,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
	rWall  = new Rectangle(795,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
	dWall  = new Rectangle(0,0,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
	uWall  = new Rectangle(0,475,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
	
	smallWall1 = new Rectangle(385,280,30,200,this.sceneManager.engine.getVertexBufferObjectManager());
	smallWall2 = new Rectangle(350,260,95,25,this.sceneManager.engine.getVertexBufferObjectManager());
	smallWall3 = new Rectangle(325,245,145,25,this.sceneManager.engine.getVertexBufferObjectManager());
	smallWall4 = new Rectangle(275,220,225,25,this.sceneManager.engine.getVertexBufferObjectManager());
	smallWall5 = new Rectangle(250,200,290,25,this.sceneManager.engine.getVertexBufferObjectManager());
	smallWall6 = new Rectangle(685,320,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
	
	sharpPointRect1 = new Rectangle(0,0,800,30,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect2 = new Rectangle(0,285,100,5,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect3 = new Rectangle(695,285,100,5,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect4 = new Rectangle(200,175,10,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect5 = new Rectangle(400,155,10,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect6 = new Rectangle(575,170,10,10,this.sceneManager.engine.getVertexBufferObjectManager());
	
	smallWall1.setColor(Color.TRANSPARENT);
	smallWall2.setColor(Color.TRANSPARENT);
	smallWall3.setColor(Color.TRANSPARENT);
	smallWall4.setColor(Color.TRANSPARENT);
	smallWall5.setColor(Color.TRANSPARENT);
	smallWall6.setColor(Color.TRANSPARENT);
	sharpPointRect1.setColor(Color.TRANSPARENT);
	sharpPointRect2.setColor(Color.TRANSPARENT);
	sharpPointRect3.setColor(Color.TRANSPARENT);
	sharpPointRect4.setColor(Color.TRANSPARENT);
	sharpPointRect5.setColor(Color.TRANSPARENT);
	sharpPointRect6.setColor(Color.TRANSPARENT);
	
	lWall.setColor(Color.WHITE);
	rWall.setColor(Color.WHITE);
	dWall.setColor(Color.WHITE);
	uWall.setColor(Color.WHITE);
	lWall.setAnchorCenter(0,0);
	rWall.setAnchorCenter(0,0);
	dWall.setAnchorCenter(0,0);
	uWall.setAnchorCenter(0,0);
	smallWall1.setAnchorCenter(0, 0);
	smallWall2.setAnchorCenter(0,0);
	smallWall3.setAnchorCenter(0,0);
	smallWall4.setAnchorCenter(0,0);
	smallWall5.setAnchorCenter(0,0);
	smallWall6.setAnchorCenter(0,0);
	sharpPointRect1.setAnchorCenter(0,0);
	sharpPointRect2.setAnchorCenter(0,0);
	sharpPointRect3.setAnchorCenter(0,0);
	sharpPointRect4.setAnchorCenter(0,0);
	sharpPointRect5.setAnchorCenter(0,0);
	sharpPointRect6.setAnchorCenter(0,0);
	
	
	lBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, lWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	rBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, rWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	uBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, uWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	dBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, dWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody1 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall1,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody2 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall2,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody3 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall3,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody4 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall4,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody5 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall5,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody6 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall6,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	
	levelNine.attachChild(lWall);
	levelNine.attachChild(rWall);
	levelNine.attachChild(uWall);
	levelNine.attachChild(dWall);
	levelNine.attachChild(smallWall1);
	levelNine.attachChild(smallWall2);
	levelNine.attachChild(smallWall3);
	levelNine.attachChild(smallWall4);
	levelNine.attachChild(smallWall5);
	levelNine.attachChild(smallWall6);
	levelNine.attachChild(sharpPointRect1);
	levelNine.attachChild(sharpPointRect2);
	levelNine.attachChild(sharpPointRect3);
	levelNine.attachChild(sharpPointRect4);
	levelNine.attachChild(sharpPointRect5);
	levelNine.attachChild(sharpPointRect6);
	
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lWall,lBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rWall,rBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(uWall,uBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dWall,dBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall1,smallWallBody1 ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall2,smallWallBody2 ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall3,smallWallBody3 ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall4,smallWallBody4 ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall5,smallWallBody5 ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall6,smallWallBody6 ));
	
	startGround = new Rectangle(0,330,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
	startGround.setAnchorCenter(0,0);
	startGround.setColor(Color.TRANSPARENT);
   	
	
	exitGround = new Rectangle(685,333,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
	exitGround.setAnchorCenter(0,0);
	exitGround.setColor(Color.TRANSPARENT);
	
	startGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,startGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	//middleGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,middleGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(startGround,startGroundBody ));
	//this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(middleGround,middleGroundBody ));

	levelNine.attachChild(exitGround);
	levelNine.attachChild(startGround);
	

    balloon = new Sprite(30, 400, balloonTR,this.sceneManager.engine.getVertexBufferObjectManager()) {
		
	          	@Override
	          	protected void onManagedUpdate(float pSecondsElapsed) {
	          	
                 
	          	  if(this.collidesWith(sharpPointRect1))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL9,9);
  					
  				  }
	          	  if(this.collidesWith(sharpPointRect2))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL9,9);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect3))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL9,9);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect4))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL9,9);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect5))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL9,9);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect6))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL9,9);
  					
  				}
                
                    if(this.collidesWith(exitGround))
                    {
                    	unregisterUpdateHandler(balloon);
						balloon.detachSelf();
						GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
						if (starCollected >= 2)
						{
							if(sceneManager.soundEnabled)
								 happySound.play();
							createGameOverScene(starCollected, 9);
						}
						else
						{
							if(sceneManager.soundEnabled)
								 sadSound.play();
							createGameOverScene(0, 9);
						}
						
                    }
		          		super.onManagedUpdate(pSecondsElapsed);
	          	}
     };
     balloon.setAnchorCenter(0,0);
	
     balloonBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, balloon, BodyType.DynamicBody,BALLOON_FIX);
     this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon,balloonBody, true,false ));
     levelNine.attachChild(balloon);
     createGameOverResources();
     levelNine.registerUpdateHandler(this.sceneManager.mPhysicsWorld);
		


     
     return levelNine;
	}

	public void loadLevelTenResources(){
		// TODO Load Level 10 Resource 
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
		levelBackgroundTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),1024,1024);
		balloonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA, this.sceneManager.activity,"balloon.png",5,5);
		fanTR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(levelBackgroundTA,this.sceneManager.activity,"fanSprite.png",125,5, 6, 1);
		levelBackgroundTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(levelBackgroundTA,this.sceneManager.activity,"ten.png",5,125); 
		levelBackgroundTA.load();
		
		star1TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star1TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star1TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star1TA.load();
		
		star2TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star2TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star2TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star2TA.load();
		
		star3TA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),80, 30);
		star3TR = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(star3TA,this.sceneManager.activity,"star.png",0,0, 2, 1);
		star3TA.load();
		
		heartTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),30, 30);
		heart1TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart2TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heart3TR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(heartTA,this.sceneManager.activity,"heart.png",0,0);
		heartTA.load();   
		
		fontTR =  new BitmapTextureAtlas(sceneManager.activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		font = new Font(sceneManager.engine.getFontManager(),fontTR, Typeface.SANS_SERIF,24,true, Color.WHITE);
	    font.load();    
		
		sceneManager.mPhysicsWorld = new PhysicsWorld(new Vector2(0,-SensorManager.GRAVITY_EARTH + 6.5f), false);
		sceneManager.WALL_FIX = PhysicsFactory.createFixtureDef(1,0,1);
		BALLOON_FIX=PhysicsFactory.createFixtureDef(1,0.3f,1);
	
		
	}
	public Scene createLevelTenScene(){		
    levelTen = new Scene();
 	starCollected = 0;
	backgroundImage = new Sprite(0,0,levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager());
    backgroundImage.setAnchorCenter(0,0);
    levelTen.attachChild(backgroundImage);
    
    heart1 = new Sprite(690,455,heart1TR,this.sceneManager.engine.getVertexBufferObjectManager());
    heart1.setVisible(true);
    levelTen.attachChild(heart1);
    
    heart2 = new Sprite(730,455,heart2TR,this.sceneManager.engine.getVertexBufferObjectManager());
    heart2.setVisible(true);
    levelTen.attachChild(heart2);
    
    heart3 = new Sprite(770,455,heart3TR,this.sceneManager.engine.getVertexBufferObjectManager());
    heart3.setVisible(true);
    levelTen.attachChild(heart3);
    
    fan = new AnimatedSprite(50,400,fanTR,this.sceneManager.engine.getVertexBufferObjectManager());
    long[] frameDuration = {50,50,50,50,50,50};
    fan.animate(frameDuration);
    levelTen.attachChild(fan);
    fan.setVisible(false);
    
    star1 = new AnimatedSprite(250,325,star1TR,this.sceneManager.engine.getVertexBufferObjectManager())
	{
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			 if(this.collidesWith(balloon)){   
				 if(sceneManager.soundEnabled)
					 starSound.play();
				 this.detachSelf();
				 star1TR = null;
				 starCollected++;
				 scoreText.setText("Stars : "+ starCollected+" ");
				 levelTen.unregisterUpdateHandler(this);
		      }
		    
			super.onManagedUpdate(pSecondsElapsed);
		}
	};
	
	star2 = new AnimatedSprite(750, 350, star2TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
		
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			if (this.collidesWith(balloon)) {
				if(sceneManager.soundEnabled)
					 starSound.play();
				this.detachSelf();
				star2TR = null;
				starCollected ++;
				scoreText.setText("Stars :" + starCollected + " ");
				levelTen.unregisterUpdateHandler(this);
			}

			super.onManagedUpdate(pSecondsElapsed);
		}
	};
	
	star3 = new AnimatedSprite(260, 150, star3TR, this.sceneManager.engine.getVertexBufferObjectManager()) {
		
		@Override
		protected void onManagedUpdate(float pSecondsElapsed) {
			if (this.collidesWith(balloon)) {
				if(sceneManager.soundEnabled)
					 starSound.play();
				this.detachSelf();

				star3TR = null;
				starCollected ++;
				scoreText.setText("Stars :" + starCollected + " ");
				levelTen.unregisterUpdateHandler(this);
			}

			super.onManagedUpdate(pSecondsElapsed);
		}
	};
	
	long[] starFrameDuration = { 300, 150 };
	star1.animate(starFrameDuration);
	star2.animate(starFrameDuration);
	star3.animate(starFrameDuration);

	levelTen.registerUpdateHandler(star1);
	levelTen.registerUpdateHandler(star2);
	levelTen.registerUpdateHandler(star3);

	scoreText = new Text(60, 460, font, "Stars : XX",sceneManager.engine.getVertexBufferObjectManager());
	scoreText.setText("Stars : 0 ");
	levelTen.attachChild(scoreText);
	levelTen.attachChild(star1);
	levelTen.attachChild(star2);
	levelTen.attachChild(star3);

	Sprite lOneImage = new Sprite(CAMERA_WIDTH / 2 - 20, CAMERA_HEIGHT / 2,	levelBackgroundTR,this.sceneManager.engine.getVertexBufferObjectManager()) {

		@Override
		public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
				float pTouchAreaLocalX, float pTouchAreaLocalY) {
			if (pSceneTouchEvent.isActionDown()) {
				
				fan.setVisible(true);
				fan.setPosition(pTouchAreaLocalX, pTouchAreaLocalY + 30);
				//balloonBody.setType(BodyType.DynamicBody);
				float vX = balloon.getX() - pTouchAreaLocalX + 30;
				float vY = balloon.getY() - pTouchAreaLocalY + 30;
				if (Math.abs(vX) <= 70 && Math.abs(vY) <= 70)
					balloonBody.setLinearVelocity(vX / 17, vY / 17);
			}
			if (pSceneTouchEvent.isActionUp()) {
				fan.setVisible(false);
			}
			
			return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
					pTouchAreaLocalY);
		}
	};
    levelTen.registerTouchArea(lOneImage);
	levelTen.setAnchorCenter(0,0);
	this.sceneManager.camera.setCenter(CAMERA_WIDTH/2, CAMERA_HEIGHT/2);
	
	lWall  = new Rectangle(0,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
	rWall  = new Rectangle(795,0,5,480,this.sceneManager.engine.getVertexBufferObjectManager());
	dWall  = new Rectangle(0,0,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
	uWall  = new Rectangle(0,475,800,5,this.sceneManager.engine.getVertexBufferObjectManager());
	
	smallWall1 = new Rectangle(120,235,280,15,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect1 = new Rectangle(0,0,800,30,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect2 = new Rectangle(185,185,45,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect3 = new Rectangle(292,185,45,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect4 = new Rectangle(548,222,252,40,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect5 = new Rectangle(630,375,60,10,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect6 = new Rectangle(140,240,235,20,this.sceneManager.engine.getVertexBufferObjectManager());
	sharpPointRect7 = new Rectangle(243,385,35,35,this.sceneManager.engine.getVertexBufferObjectManager());
	
	smallWall1.setColor(Color.TRANSPARENT);
	sharpPointRect1.setColor(Color.TRANSPARENT);
	sharpPointRect2.setColor(Color.TRANSPARENT);
	sharpPointRect3.setColor(Color.TRANSPARENT);
	sharpPointRect4.setColor(Color.TRANSPARENT);
	sharpPointRect5.setColor(Color.TRANSPARENT);
	sharpPointRect6.setColor(Color.TRANSPARENT);
	sharpPointRect7.setColor(Color.TRANSPARENT);
	
	lWall.setColor(Color.WHITE);
	rWall.setColor(Color.WHITE);
	dWall.setColor(Color.WHITE);
	uWall.setColor(Color.WHITE);
	lWall.setAnchorCenter(0,0);
	rWall.setAnchorCenter(0,0);
	dWall.setAnchorCenter(0,0);
	uWall.setAnchorCenter(0,0);
	smallWall1.setAnchorCenter(0, 0);
	sharpPointRect1.setAnchorCenter(0,0);
	sharpPointRect2.setAnchorCenter(0,0);
	sharpPointRect3.setAnchorCenter(0,0);
	sharpPointRect4.setAnchorCenter(0,0);
	sharpPointRect5.setAnchorCenter(0,0);
	sharpPointRect6.setAnchorCenter(0,0);
	sharpPointRect7.setAnchorCenter(0,0);
	
	
	lBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, lWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	rBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, rWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	uBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, uWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	dBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, dWall, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	smallWallBody1 = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,smallWall1,BodyType.StaticBody,this.sceneManager.WALL_FIX);
	
	levelTen.attachChild(lWall);
	levelTen.attachChild(rWall);
	levelTen.attachChild(uWall);
	levelTen.attachChild(dWall);
	levelTen.attachChild(smallWall1);
	levelTen.attachChild(sharpPointRect1);
	levelTen.attachChild(sharpPointRect2);
	levelTen.attachChild(sharpPointRect3);
	levelTen.attachChild(sharpPointRect4);
	levelTen.attachChild(sharpPointRect5);
	levelTen.attachChild(sharpPointRect6);
	levelTen.attachChild(sharpPointRect7);
	
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lWall,lBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rWall,rBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(uWall,uBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dWall,dBody ));
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(smallWall1,smallWallBody1 ));
	
	startGround = new Rectangle(0,50,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
	startGround.setAnchorCenter(0,0);
	startGround.setColor(Color.TRANSPARENT);
   	
	
	exitGround = new Rectangle(685,50,115,10,this.sceneManager.engine.getVertexBufferObjectManager());
	exitGround.setAnchorCenter(0,0);
	exitGround.setColor(Color.TRANSPARENT);
	
	startGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,startGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	//middleGroundBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld,middleGround, BodyType.StaticBody,this.sceneManager.WALL_FIX);
	
	this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(startGround,startGroundBody ));
	//this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(middleGround,middleGroundBody ));

	levelTen.attachChild(exitGround);
	levelTen.attachChild(startGround);
	

    balloon = new Sprite(30, 150, balloonTR,this.sceneManager.engine.getVertexBufferObjectManager()) {
		
	          	@Override
	          	protected void onManagedUpdate(float pSecondsElapsed) {
	          	
                 
	          	  if(this.collidesWith(sharpPointRect1))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL10,10);
  					
  				  }
	          	  if(this.collidesWith(sharpPointRect2))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL10,10);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect3))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL10,10);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect4))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL10,10);
  					
  				}
	          	  if(this.collidesWith(sharpPointRect5))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL10,10);
  					
  				}
                
	          	  if(this.collidesWith(sharpPointRect6))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
	    					balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL10,10);
  					
  				}
                
	          	  if(this.collidesWith(sharpPointRect7))
                  {   
                  	    unregisterUpdateHandler(balloon);
	    				balloon.detachSelf();
	    				GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
	    				if(sceneManager.soundEnabled)
		    				balloonPop.play();
		    			checkForGameOver(GameLevels.LEVEL10,10);
  					
  				}
                
                    if(this.collidesWith(exitGround))
                    {
                    	unregisterUpdateHandler(balloon);
						balloon.detachSelf();
						GameManager.this.sceneManager.mPhysicsWorld.destroyBody(balloonBody);
						if (starCollected >= 2)
						{
							if(sceneManager.soundEnabled)
								 happySound.play();
							createGameOverScene(starCollected, 10);
						}
						else
						{
							if(sceneManager.soundEnabled)
								 sadSound.play();
							createGameOverScene(0, 10);
						}
						
                    }
		          		super.onManagedUpdate(pSecondsElapsed);
	          	}
     };
     balloon.setAnchorCenter(0,0);
	
     balloonBody = PhysicsFactory.createBoxBody(this.sceneManager.mPhysicsWorld, balloon, BodyType.DynamicBody,BALLOON_FIX);
     this.sceneManager.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon,balloonBody, true,false ));
     levelTen.attachChild(balloon);
     createGameOverResources();
     levelTen.registerUpdateHandler(this.sceneManager.mPhysicsWorld);
		


     
     return levelTen;
	}

	public void checkForGameOver(GameLevels level, int levelNo) {
		// TODO GAME OVER
		starCollected = 0;
		
		if(lifeRemaining == 0)
		{
			heart3.setVisible(false);
			createGameOverResources();
			createGameOverScene(0, levelNo);
		}
		else
		{
			lifeRemaining --;
			
			if(getCurrentLevel() == GameLevels.LEVEL1)
			{
				loadLevelOneResources();
				createLevelOneScene();
			}
			else if(getCurrentLevel() == GameLevels.LEVEL2)
			{   loadLevelTwoResources();
				createLevelTwoScene();
			}
			else if(getCurrentLevel() == GameLevels.LEVEL3)
			{
				loadLevelThreeResources();
				createLevelThreeScene();
			}
			else if(getCurrentLevel()== GameLevels.LEVEL4)
			{
				loadLevelFourResources();
				createLevelFourScene();
				
			}
			else if(getCurrentLevel()== GameLevels.LEVEL5)
			{
				loadLevelFiveResources();
				createLevelFiveScene();
				
			}
			else if(getCurrentLevel()== GameLevels.LEVEL6)
			{
				loadLevelSixResources();
				createLevelSixScene();
				
			}
			else if(getCurrentLevel()== GameLevels.LEVEL7)
			{
				loadLevelSevenResources();
				createLevelSevenScene();
			}
			else if(getCurrentLevel()== GameLevels.LEVEL8)
			{
				loadLevelEightResources();
				createLevelEightScene();
			}
			else if(getCurrentLevel()== GameLevels.LEVEL9)
			{
				loadLevelNineResources();
				createLevelNineScene();
			}
			else if(getCurrentLevel()== GameLevels.LEVEL10)
			{
				loadLevelTenResources();
				createLevelTenScene();
			}
			
			switch(lifeRemaining){
			case 0:
				heart2.setVisible(false);
			case 1:
				heart1.setVisible(false);
			}
			setCurrentScene(level);
		}
	}
	
	public GameLevels getCurrentLevel() {
		return currentLevel;
	}

	public void createGameOverResources() {
		//TODO Create GAMEOVER Resource
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("button/");
	    gameCompleteTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),500, 400);
	    gameCompleteTR =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameCompleteTA,this.sceneManager.activity, "gameComplete.png", 0, 0);
	    gameCompleteTA.load();
	   
	    playAgainTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),150, 65);
	    playAgainTR =BitmapTextureAtlasTextureRegionFactory.createFromAsset(playAgainTA,this.sceneManager.activity, "playAgain.png", 0, 0);
	    playAgainTA.load();
	    
	    menuTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 130, 65);
		menuTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, this.sceneManager.activity, "menu.png",0,0);
		menuTA.load();
		
	    nextTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),130, 65);
	    nextTR =BitmapTextureAtlasTextureRegionFactory.createFromAsset(nextTA,this.sceneManager.activity, "next.png", 0, 0);
	    nextTA.load();
	    
		ratingStarTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 60, 60);
		ratingStarTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(ratingStarTA, this.sceneManager.activity, "ratingStar.png",0,0);
		ratingStarTA.load();	
		
		resultTA = new BitmapTextureAtlas(this.sceneManager.activity.getTextureManager(), 200, 50);
	}
	
	public void createGameOverScene(int starCollected,final int levelNo) {
		createGameOverResources();
		gameComplete = new Sprite(100, 50,gameCompleteTR,sceneManager.engine.getVertexBufferObjectManager());
		gameComplete.setAnchorCenter(0,0);
		stopAllMusic();
		if(starCollected >= 2)
		{   if(levelNo<10)
		    dbHandler.updateLevel(levelNo+1); // unlocks next level.
			resultTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(resultTA, this.sceneManager.activity, "win.png",0,0);
		}
			else
			resultTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(resultTA, this.sceneManager.activity, "lose.png",0,0);
			
		resultTA.load();
		resultImage= new Sprite(160,275,resultTR,this.sceneManager.engine.getVertexBufferObjectManager());
		resultImage.setAnchorCenter(0,0);
		
		ratingStar1= new Sprite(165,210,ratingStarTR,this.sceneManager.engine.getVertexBufferObjectManager());
		ratingStar1.setAnchorCenter(0,0);
		ratingStar2= new Sprite(230,210,ratingStarTR,this.sceneManager.engine.getVertexBufferObjectManager());
		ratingStar2.setAnchorCenter(0,0);
		ratingStar3= new Sprite(290,210,ratingStarTR,this.sceneManager.engine.getVertexBufferObjectManager());
		ratingStar3.setAnchorCenter(0,0);
		
		playAgain = new ButtonSprite(190, 70, playAgainTR,sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					lifeRemaining = 2;
					
					switch(levelNo)
					{
					 case 1: 	loadNextLevel(GameLevels.LEVEL0);
						        break;
					 case 2:    loadNextLevel(GameLevels.LEVEL1);
							   	break;
					 case 3:    loadNextLevel(GameLevels.LEVEL2);
							    break;
					 case 4:    loadNextLevel(GameLevels.LEVEL3);
							    break;
					 case 5:    loadNextLevel(GameLevels.LEVEL4);
							    break;
					 case 6:    loadNextLevel(GameLevels.LEVEL5);
							    break;
					 case 7:    loadNextLevel(GameLevels.LEVEL6);
					            break;
					 case 8:    loadNextLevel(GameLevels.LEVEL7);
			                    break;
					 case 9:    loadNextLevel(GameLevels.LEVEL8);
			                    break;
					 case 10:   loadNextLevel(GameLevels.LEVEL9);
			                    break;
		
					}
					
					scoreText.setText("Stars : 0");
					
					setCurrentScene(getCurrentLevel());
					
			 	}
			
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}
		};
		playAgain.setAnchorCenter(0, 0);
		
		menu = new ButtonSprite(100, 140, menuTR,sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					sceneManager.isGameManagerActive = false;
					sceneManager.backMusic.seekTo(0);
					if(sceneManager.soundEnabled)
						 sceneManager.backMusic.resume();
					sceneManager.createMenuScene();
					sceneManager.setCurrentScene(AllScenes.MENU);
			    	}
			
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}
		};
		
		menu.setAnchorCenter(0, 0);
		
		
		if(starCollected >= 2){
			next = new ButtonSprite(300, 140, nextTR,sceneManager.engine.getVertexBufferObjectManager()) {

				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if (pSceneTouchEvent.isActionUp()) {
						lifeRemaining = 2;
						
						loadNextLevel(getCurrentLevel());
					}
                    
					return super.onAreaTouched(pSceneTouchEvent,
							pTouchAreaLocalX, pTouchAreaLocalY);
				}
			};

			next.setAnchorCenter(0, 0);

			gameComplete.attachChild(menu);
			gameComplete.attachChild(next);
			gameComplete.attachChild(playAgain);
			sceneManager.engine.getScene().registerTouchArea(next);
		}
		else
		{
			playAgain.setPosition(190, 100);
			menu.setPosition(200, 175);
			gameComplete.attachChild(playAgain);
			gameComplete.attachChild(menu);
		}
		
		gameComplete.attachChild(resultImage);
		switch(starCollected){
		case 3:
			gameComplete.attachChild(ratingStar3);
			
		case 2:
			gameComplete.attachChild(ratingStar2);
			
		case 1:
			gameComplete.attachChild(ratingStar1);
		}
		
		sceneManager.engine.getScene().attachChild(gameComplete);
		sceneManager.engine.getScene().registerTouchArea(menu);
		sceneManager.engine.getScene().registerTouchArea(playAgain);
	}
	
	private void loadNextLevel(GameLevels currentLevel) {
		switch (currentLevel) {
		case LEVEL0:
			if(sceneManager.soundEnabled)
			{	
			stopAllMusic();
			levelMusic1.seekTo(0);
			if(sceneManager.soundEnabled)
				 levelMusic1.resume();
			}
			loadLevelOneResources();
			createLevelOneScene();
			setCurrentScene(GameLevels.LEVEL1);
			break;
			
		case LEVEL1:
			if(sceneManager.soundEnabled)
			{	
			stopAllMusic();
			levelMusic2.seekTo(0);
			if(sceneManager.soundEnabled)
				 levelMusic2.resume();
			}
			loadLevelTwoResources();
			createLevelTwoScene();
			setCurrentScene(GameLevels.LEVEL2);
			break;

		case LEVEL2:
			if(sceneManager.soundEnabled)
			{	
			stopAllMusic();
			levelMusic3.seekTo(0);
			if(sceneManager.soundEnabled)
				 levelMusic3.resume();
			}
			loadLevelThreeResources();
			createLevelThreeScene();
			setCurrentScene(GameLevels.LEVEL3);
			break;
		
		case LEVEL3:
			if(sceneManager.soundEnabled)
			{	
			stopAllMusic();
			levelMusic1.seekTo(0);
			if(sceneManager.soundEnabled)
				 levelMusic1.resume();
			}
			loadLevelFourResources();
			createLevelFourScene();
			setCurrentScene(GameLevels.LEVEL4);
			break;
		
		case LEVEL4:
			if(sceneManager.soundEnabled)
			{	
			stopAllMusic();
			levelMusic2.seekTo(0);
			if(sceneManager.soundEnabled)
				 levelMusic2.resume();
			}
			loadLevelFiveResources();
			createLevelFiveScene();
			setCurrentScene(GameLevels.LEVEL5);
			break;
			
		case LEVEL5:
			if(sceneManager.soundEnabled)
			{	
			stopAllMusic();
			levelMusic3.seekTo(0);
			if(sceneManager.soundEnabled)
				 levelMusic3.resume();
			}
			loadLevelSixResources();
			createLevelSixScene();
			setCurrentScene(GameLevels.LEVEL6);
			break;
			
		case LEVEL6:
			if(sceneManager.soundEnabled)
			{	
			stopAllMusic();
			levelMusic1.seekTo(0);
			if(sceneManager.soundEnabled)
				 levelMusic1.resume();
			}
			loadLevelSevenResources();
			createLevelSevenScene();
			setCurrentScene(GameLevels.LEVEL7);
			break;
			
		case LEVEL7:
			if(sceneManager.soundEnabled)
			{	
			stopAllMusic();
			levelMusic2.seekTo(0);
			if(sceneManager.soundEnabled)
				 levelMusic2.resume();
			}
			loadLevelEightResources();
			createLevelEightScene();
			setCurrentScene(GameLevels.LEVEL8);
			break;
			
		case LEVEL8:
			if(sceneManager.soundEnabled)
			{	
			stopAllMusic();
			levelMusic3.seekTo(0);
			if(sceneManager.soundEnabled)
				 levelMusic3.resume();
			}
			loadLevelNineResources();
			createLevelNineScene();
			setCurrentScene(GameLevels.LEVEL9);
			break;
			
		case LEVEL9:
			if(sceneManager.soundEnabled)
			{	
			stopAllMusic();
			levelMusic1.seekTo(0);
			if(sceneManager.soundEnabled)
				 levelMusic1.resume();
			}
			loadLevelTenResources();
			createLevelTenScene();
			setCurrentScene(GameLevels.LEVEL10);
			break;
			
		case LEVEL10:
			if(sceneManager.soundEnabled)
			{	
			stopAllMusic();
			}
			this.sceneManager.loadMenu();
			break;
			
		default:
			break;
		}
	}
	
	public void createLevelLockedResources() {
		//TODO Create Level Locked Resource
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("button/");
	    gameCompleteTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),500, 400);
	    gameCompleteTR =BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameCompleteTA,this.sceneManager.activity, "levelLocked.png", 0, 0);
	    gameCompleteTA.load();
	   
	    playAgainTA = new BitmapTextureAtlas(this.sceneManager.engine.getTextureManager(),130, 65);
	    playAgainTR =BitmapTextureAtlasTextureRegionFactory.createFromAsset(playAgainTA,this.sceneManager.activity, "play1.png", 0, 0);
	    playAgainTA.load();
	}
	
	public void createLevelLockedScene(final int levelClicked) {
		isLevelLocked = true;
		
		sceneManager.splashScene.detachChild(sceneManager.backGround);
		sceneManager.backGround = new Sprite(400, 240, this.sceneManager.splashTR, sceneManager.engine.getVertexBufferObjectManager());
		sceneManager.splashScene.attachChild(sceneManager.backGround);
		sceneManager.setCurrentScene(AllScenes.SPLASH);
		
		createLevelLockedResources();
			
		gameComplete = new Sprite(100, 50,gameCompleteTR,sceneManager.engine.getVertexBufferObjectManager());
		gameComplete.setAnchorCenter(0,0);
		
		playAgain = new ButtonSprite(200, 70, playAgainTR,sceneManager.engine.getVertexBufferObjectManager()) {
			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
							
					switch(levelClicked)
					{	
					 case 2:    
					 case 3:    
					 case 4:    
					 case 5:    createLevelOneSelectionScene();
					 			setCurrentScene(GameLevels.LEVELSELECT1);
							    break;
					 case 6:    
					 case 7:    
					 case 8:    
					 case 9:    
					 case 10:   createLevelTwoSelectionScene();
						 		setCurrentScene(GameLevels.LEVELSELECT2);
			                    break;
		
					}
			 	}
			
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX,
						pTouchAreaLocalY);
			}
		};
		playAgain.setAnchorCenter(0, 0);
		gameComplete.attachChild(playAgain);

		sceneManager.engine.getScene().attachChild(gameComplete);
		sceneManager.engine.getScene().registerTouchArea(playAgain);
	}
	
	public void setCurrentScene(GameLevels currentLevel) {
		this.currentLevel = currentLevel;
		switch (currentLevel) {
		case LEVELSELECT1:
			this.sceneManager.engine.setScene(levelSelectOne);
			break;
			
		case LEVELSELECT2:
			this.sceneManager.engine.setScene(levelSelectTwo);
			break;
			
		case LEVEL1:
			this.sceneManager.engine.setScene(levelOne);
			break;
			
		case LEVEL2:
			this.sceneManager.engine.setScene(levelTwo);
			break;
			
		case LEVEL3:
			this.sceneManager.engine.setScene(levelThree);
			break;
			
		case LEVEL4:
			this.sceneManager.engine.setScene(levelFour);
			break;
			
		case LEVEL5:
			this.sceneManager.engine.setScene(levelFive);
			break;
			
		case LEVEL6:
			this.sceneManager.engine.setScene(levelSix);
			break;
		case LEVEL7:
			this.sceneManager.engine.setScene(levelSeven);
			break;
		case LEVEL8:
			this.sceneManager.engine.setScene(levelEight);
			break;
		case LEVEL9:
			this.sceneManager.engine.setScene(levelNine);
			break;
		case LEVEL10:
			this.sceneManager.engine.setScene(levelTen);
			break;
		default:
			break;
		}
	}
	
	
		
}
