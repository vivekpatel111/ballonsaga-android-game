package com.rocketapps.balloonsaga;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;

import android.content.Intent;
import android.hardware.SensorManager;
import android.net.Uri;
import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.rocketapps.balloonsaga.GameManager.GameLevels;

public class SceneManager{

	public enum AllScenes{
		SPLASH,
		MENU,
		HELP,
		CREDITS,
		QUIT
	};
	
	public AllScenes currentScene;
	public BaseGameActivity activity;
	public Engine engine;
	public Camera camera;
	public GameManager gameManager;
	public boolean isGameManagerActive = false;
	
	public BitmapTextureAtlas splashTA;
	public ITextureRegion splashTR;
	private BitmapTextureAtlas playTA,playonTA,helpTA,helponTA,quitTA,quitonTA,creditsTA,creditsonTA,
				yesTA,yesonTA,noTA,noonTA,nextTA,backTA,backonTA,menuTA,menuonTA,soundonTA,soundoffTA,
				chaitanyaTA,savanTA,vivekTA,helpScreenTA,instructionTA,rateTA;
	private ITextureRegion playTR,playonTR,helpTR,helponTR,quitTR,quitonTR,creditsTR,creditsonTR,
				yesTR,yesonTR,noTR,noonTR,nextTR,backTR,backonTR,menuTR,menuonTR,soundonTR,soundoffTR,
				chaitanyaTR,savanTR,vivekTR,helpScreenTR,instructionTR,rateTR;	
	public Scene splashScene, menuScene, helpScene, creditScene, quitScene;
	
	private ButtonSprite play,quit,sound,help,menu,yes,no,credit,back,rate;
	public boolean soundEnabled;
	public Sprite backGround,helpImageSprite;
	public int currentHelpScreen = 0, totalHelpScreen = 4;

	public  Music backMusic,levelMusic1,levelMusic2,levelMusic3;
	public Sound clickSound;
	
	public PhysicsWorld mPhysicsWorld;
	public FixtureDef WALL_FIX,BALLOON_FIX;
	
	private Rectangle lWall, rWall, dWall, uWall;
	private Body lBody,rBody,uBody,dBody;
	private BitmapTextureAtlas balloonTA;
	private TextureRegion balloonTR;
	private Sprite balloon1,balloon2,balloon3;
	private Body balloonBody1,balloonBody2,balloonBody3;
	TimerHandler balloonSprite;
	private int velocityX,velocityY;
	
	public SceneManager(BaseGameActivity activity, Engine engine, Camera camera) {
		this.activity = activity;
		this.engine = engine;
		this.camera = camera;
	}
	
	public void loadSplashResources() throws IllegalStateException, IOException{
		//TODO Load Splash Resource
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("background/");
		splashTA = new BitmapTextureAtlas(this.activity.getTextureManager(),800,480);
		splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTA, this.activity, "splash.png",0,0);
		splashTA.load();

		MusicFactory.setAssetBasePath("sound/");
		SoundFactory.setAssetBasePath("sound/");
		try {
			backMusic = MusicFactory.createMusicFromAsset(engine.getMusicManager(),activity, "backgroundMusic.ogg");
			clickSound = SoundFactory.createSoundFromAsset(engine.getSoundManager(), activity, "buttonClick.ogg");
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		backMusic.setVolume(1f);
		backMusic.setLooping(true);
		clickSound.setLoopCount(0);
		clickSound.setVolume(1f);		
	}
	
	public void loadMenuResources(){
		//TODO Load Menu Resource
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("background/");
		splashTA = new BitmapTextureAtlas(this.activity.getTextureManager(),800,480);
		splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTA, this.activity, "menu.png",0,0);
		splashTA.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("button/");
		playTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 160, 80);
		playTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playTA, this.activity, "play.png",0,0);
		playTA.load();
		
		playonTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 160, 80);
		playonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(playonTA, this.activity, "play_pressed.png",0,0);
		playonTA.load();
		
		soundonTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 75, 75);
		soundonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(soundonTA, this.activity, "soundon.png",0,0);
		soundonTA.load();
		
		soundoffTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 75, 75);
		soundoffTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(soundoffTA, this.activity, "soundoff.png",0,0);
		soundoffTA.load();
		
		helpTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		helpTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(helpTA, this.activity, "help.png",0,0);
		helpTA.load();
		
		helponTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		helponTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(helponTA, this.activity, "help_pressed.png",0,0);
		helponTA.load();
		
		creditsTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 170, 65);
		creditsTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(creditsTA, this.activity, "credits.png",0,0);
		creditsTA.load();
		
		creditsonTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 150, 65);
		creditsonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(creditsonTA, this.activity, "credits_pressed.png",0,0);
		creditsonTA.load();
		
		rateTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		rateTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(rateTA, this.activity, "rate.png",0,0);
		rateTA.load();
		
		quitTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		quitTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(quitTA, this.activity, "quit.png",0,0);
		quitTA.load();
		
		quitonTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		quitonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(quitonTA, this.activity, "quit_pressed.png",0,0);
		quitonTA.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("level/");
		balloonTA = new BitmapTextureAtlas(this.activity.getTextureManager(),60,60);
		balloonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(balloonTA,this.activity,"balloon.png",0,0);
		balloonTA.load();
		
		BALLOON_FIX=PhysicsFactory.createFixtureDef(1,1,1);
		this.mPhysicsWorld = new PhysicsWorld(new Vector2(0,SensorManager.GRAVITY_EARTH - 7f), false);
		this.WALL_FIX = PhysicsFactory.createFixtureDef(1,0,1);
		
		if(soundEnabled)
			backMusic.play();
	}

	public void loadCreditResources(){
		//TODO Load Credit Resource
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("background/");
		splashTA = new BitmapTextureAtlas(this.activity.getTextureManager(),800,480);
		splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTA, this.activity, "credit.png",0,0);
		splashTA.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("button/");
		backTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		backTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backTA, this.activity, "back.png",0,0);
		backTA.load();
		
		backonTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		backonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backonTA, this.activity, "back_pressed.png",0,0);
		backonTA.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("developer/");
		chaitanyaTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 120, 120);
		chaitanyaTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(chaitanyaTA, this.activity, "chaitanya.png",0,0);
		chaitanyaTA.load();
		
		savanTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 120, 120);
		savanTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(savanTA, this.activity, "savan.png",0,0);
		savanTA.load();
		
		vivekTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 120, 120);
		vivekTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(vivekTA, this.activity, "vivek.png",0,0);
		vivekTA.load();
		
		mPhysicsWorld = new PhysicsWorld(new Vector2(0, -SensorManager.GRAVITY_EARTH + 5f), false);
		WALL_FIX = PhysicsFactory.createFixtureDef(1,.8f,0);
	}
	
	public void loadHelpResources(){
		//TODO Load Help Resource
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("background/");
		splashTA = new BitmapTextureAtlas(this.activity.getTextureManager(),800,480);
		splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTA, this.activity, "help.png",0,0);
		splashTA.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("button/");
		menuTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		menuTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTA, this.activity, "menu.png",0,0);
		menuTA.load();
		
		menuonTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		menuonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuonTA, this.activity, "menu_pressed.png",0,0);
		menuonTA.load();
		
		nextTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 200, 100);
		nextTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(nextTA, this.activity, "nextcloud.png",0,0);
		nextTA.load();
		
		backTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 200, 100);
		backTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(backTA, this.activity, "backcloud.png",0,0);
		backTA.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("help/");
		helpScreenTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 600, 400);
		helpScreenTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(helpScreenTA, this.activity, "help"+currentHelpScreen+".png",0,0);
		helpScreenTA.load();
	}
	
	public void loadQuitResources(){
		//TODO Load Quit Resource
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("background/");
		splashTA = new BitmapTextureAtlas(this.activity.getTextureManager(),800,480);
		splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTA, this.activity, "menu.png",0,0);
		splashTA.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("button/");
		quitTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		quitTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(quitTA, this.activity, "quit.png",0,0);
		quitTA.load();
		
		yesTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		yesTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(yesTA, this.activity, "yes.png",0,0);
		yesTA.load();
		
		yesonTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		yesonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(yesonTA, this.activity, "yes_pressed.png",0,0);
		yesonTA.load();
		
		noTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		noTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(noTA, this.activity, "no.png",0,0);
		noTA.load();
		
		noonTA = new BitmapTextureAtlas(this.activity.getTextureManager(), 130, 65);
		noonTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(noonTA, this.activity, "no_pressed.png",0,0);
		noonTA.load();
	}
	
	public Scene createSplashScene(){
		splashScene = new Scene();
		 
		backGround = new Sprite(400, 240, splashTR, engine.getVertexBufferObjectManager());
		splashScene.attachChild(backGround);
		return splashScene;
	}
	
	public Scene createMenuScene(){
		//TODO Load Menu Scene
		menuScene = new Scene();
		menuScene.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		
		backGround = new Sprite(400, 240, splashTR, engine.getVertexBufferObjectManager());
		menuScene.attachChild(backGround);
		
		lWall  = new Rectangle(0,0,5,480,this.engine.getVertexBufferObjectManager());
		rWall  = new Rectangle(795,0,5,480,this.engine.getVertexBufferObjectManager());
		dWall  = new Rectangle(0,0,800,5,this.engine.getVertexBufferObjectManager());
		uWall  = new Rectangle(0,475,800,5,this.engine.getVertexBufferObjectManager());
		
		lWall.setAnchorCenter(0,0);
		rWall.setAnchorCenter(0,0);
		dWall.setAnchorCenter(0,0);
		uWall.setAnchorCenter(0,0);
		
		lWall.setColor(Color.WHITE);
		rWall.setColor(Color.WHITE);
		dWall.setColor(Color.WHITE);
		uWall.setColor(Color.WHITE);
		
		menuScene.attachChild(lWall);
		menuScene.attachChild(rWall);
		menuScene.attachChild(uWall);
		menuScene.attachChild(dWall);
		
		lBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, lWall, BodyType.StaticBody,this.WALL_FIX);
		rBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, rWall, BodyType.StaticBody,this.WALL_FIX);
		uBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, uWall, BodyType.StaticBody,this.WALL_FIX);
		dBody = PhysicsFactory.createBoxBody(this.mPhysicsWorld, dWall, BodyType.StaticBody,this.WALL_FIX);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(lWall,lBody ));
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(rWall,rBody ));
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(uWall,uBody ));
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(dWall,dBody ));
	
		balloon1 = new Sprite(50, 100, balloonTR,this.engine.getVertexBufferObjectManager());

		balloon1.setAnchorCenter(0, 0);
		balloonBody1 = PhysicsFactory.createBoxBody(this.mPhysicsWorld,	balloon1, BodyType.DynamicBody, BALLOON_FIX);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon1, balloonBody1, true, false));
		menuScene.attachChild(balloon1);
		
		balloon2 = new Sprite(400, 100, balloonTR,this.engine.getVertexBufferObjectManager());

		balloon2.setAnchorCenter(0, 0);
		balloonBody2 = PhysicsFactory.createBoxBody(this.mPhysicsWorld,	balloon2, BodyType.DynamicBody, BALLOON_FIX);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon2, balloonBody2, true, false));
		menuScene.attachChild(balloon2);
		
		balloon3 = new Sprite(750, 100, balloonTR,this.engine.getVertexBufferObjectManager());

		balloon3.setAnchorCenter(0, 0);
		balloonBody3 = PhysicsFactory.createBoxBody(this.mPhysicsWorld,	balloon3, BodyType.DynamicBody, BALLOON_FIX);
		this.mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(balloon3, balloonBody3, true, false));
		menuScene.attachChild(balloon3);
		menuScene.registerUpdateHandler(this.mPhysicsWorld);
		
		engine.registerUpdateHandler(balloonSprite = new TimerHandler(3f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				balloonSprite.reset();
				velocityX = (int) (-7 + Math.random() * 15);
				velocityY = (int) (-3 + Math.random() * 7);
				balloonBody1.setLinearVelocity(velocityX, velocityY);
				
				velocityX = (int) (-7 + Math.random() * 15);
				velocityY = (int) (-3 + Math.random() * 7);
				balloonBody2.setLinearVelocity(velocityX, velocityY);
				
				velocityX = (int) (-7 + Math.random() * 15);
				velocityY = (int) (-3 + Math.random() * 7);
				balloonBody3.setLinearVelocity(velocityX, velocityY);
			}
		}));
		
		play = new ButtonSprite(400, 350, playTR, engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(soundEnabled)
					{
						clickSound.play();
						backMusic.stop();
					}
					loadGame();
				}
				else{
					play.detachSelf();
					play = new ButtonSprite(400, 350, playonTR, engine.getVertexBufferObjectManager());
					menuScene.attachChild(play);
					setCurrentScene(AllScenes.MENU);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		menuScene.registerTouchArea(play);
		menuScene.attachChild(play);
		
		if (soundEnabled) {
			sound = new ButtonSprite(725, 400, soundonTR,engine.getVertexBufferObjectManager()) {

				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if(pSceneTouchEvent.isActionDown()){
						if(soundEnabled)
							clickSound.play();
						changeSound();
					}
					return super.onAreaTouched(pSceneTouchEvent,pTouchAreaLocalX, pTouchAreaLocalY);
				}

			};
		} else {
			sound = new ButtonSprite(725, 400, soundoffTR,engine.getVertexBufferObjectManager()) {

				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if(pSceneTouchEvent.isActionDown()){
						if(soundEnabled)
							clickSound.play();
						changeSound();
					}
					return super.onAreaTouched(pSceneTouchEvent,pTouchAreaLocalX, pTouchAreaLocalY);
				}

			};
		}
		menuScene.registerTouchArea(sound);
		menuScene.attachChild(sound);
		
		help = new ButtonSprite(200, 250, helpTR, engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(soundEnabled)
						clickSound.play();
					loadHelp();
				}
				else{
					help.detachSelf();
					help = new ButtonSprite(200, 250, helponTR, engine.getVertexBufferObjectManager());
					menuScene.attachChild(help);
					setCurrentScene(AllScenes.MENU);
				}
					
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		menuScene.registerTouchArea(help);
		menuScene.attachChild(help);
		
		rate = new ButtonSprite(700, 50, rateTR, engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(soundEnabled)
						clickSound.play();
				
					Intent intent = new Intent(Intent.ACTION_VIEW);
				    intent.setData(Uri.parse("market://details?id=com.rocketapps.balloonsaga"));
				
				    activity.startActivity(intent);
				   
				}
					
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		menuScene.registerTouchArea(rate);
		menuScene.attachChild(rate);
		
		credit = new ButtonSprite(400, 250, creditsTR, engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(soundEnabled)
						clickSound.play();
					loadCredit();
				}
				else{
					credit.detachSelf();
					credit = new ButtonSprite(400, 250, creditsonTR, engine.getVertexBufferObjectManager());
					menuScene.attachChild(credit);
					setCurrentScene(AllScenes.MENU);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		menuScene.registerTouchArea(credit);
		menuScene.attachChild(credit);
		
		quit = new ButtonSprite(600, 250, quitTR, engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(soundEnabled)
						clickSound.play();
					loadQuit();
				}
				else{
					quit.detachSelf();
					quit = new ButtonSprite(600, 250, quitonTR, engine.getVertexBufferObjectManager());
					menuScene.attachChild(quit);
					setCurrentScene(AllScenes.MENU);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		menuScene.registerTouchArea(quit);
		menuScene.attachChild(quit);
		
		return menuScene;
	}

	protected void loadCredit() {
		loadCreditResources();
		createCreditScene();
		setCurrentScene(AllScenes.CREDITS);
	}

	protected void loadGame() {
		// TODO Initialize Game Manager
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("background/");
		instructionTA = new BitmapTextureAtlas(this.activity.getTextureManager(),800,480);
		instructionTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(instructionTA, this.activity, "instruction.png",0,0);
		instructionTA.load();

		MusicFactory.setAssetBasePath("sound/");
		SoundFactory.setAssetBasePath("sound/");
		try {
			levelMusic1 = MusicFactory.createMusicFromAsset(engine.getMusicManager(),activity, "levelMusic1.ogg");
			levelMusic2 = MusicFactory.createMusicFromAsset(engine.getMusicManager(),activity, "levelMusic2.ogg");
			levelMusic3 = MusicFactory.createMusicFromAsset(engine.getMusicManager(),activity, "levelMusic3.ogg");
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
		
		createSplashScene();
		try {
			loadSplashResources();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		splashScene.detachChild(backGround);
		backGround = new Sprite(400, 240, instructionTR, engine.getVertexBufferObjectManager());
		splashScene.attachChild(backGround);
		setCurrentScene(AllScenes.SPLASH);
		
		engine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				engine.unregisterUpdateHandler(pTimerHandler);
				SceneManager.this.gameManager = new GameManager(SceneManager.this);
				gameManager.loadLevelOneSelectionResources();
				gameManager.createLevelOneSelectionScene();
				isGameManagerActive = true;
				if(backMusic.isPlaying())
					backMusic.pause();
				backMusic.setVolume(0.25f);
				/*if(soundEnabled)
					backMusic.play();*/
				
				gameManager.setCurrentScene(GameLevels.LEVELSELECT1);
			}
		}));
		}

	protected void loadHelp() {
		loadHelpResources();
		createHelpScene();
		setCurrentScene(AllScenes.HELP);
	}

	protected void changeSound() {
		// TODO Toggle sound
		sound.detachSelf();
		soundEnabled = !soundEnabled;
		
		if (soundEnabled) {
			backMusic.play();
			sound = new ButtonSprite(725, 400, soundonTR,	engine.getVertexBufferObjectManager());
		} else {
			backMusic.pause();
			sound = new ButtonSprite(725, 400, soundoffTR, engine.getVertexBufferObjectManager());
		}
		menuScene.attachChild(sound);
		setCurrentScene(AllScenes.MENU);
	}

	protected void loadQuit() {
		loadQuitResources();
		createQuitScene();
		setCurrentScene(AllScenes.QUIT);
	}

	public Scene createCreditScene(){
		//TODO Load Credit Scene
		creditScene = new Scene();
		creditScene.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		
		backGround = new Sprite(400, 240, splashTR, this.activity.getVertexBufferObjectManager());
		creditScene.attachChild(backGround);
		
		Sprite chaitanya = new Sprite(80, 50, chaitanyaTR, this.activity.getVertexBufferObjectManager());
		chaitanya.setAnchorCenter(0, 0);
		Body chaitanyaB = PhysicsFactory.createCircleBody(mPhysicsWorld, chaitanya, BodyType.DynamicBody, WALL_FIX);
		creditScene.attachChild(chaitanya);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(chaitanya, chaitanyaB));
		
		Sprite savan = new Sprite(285, 50, savanTR, this.activity.getVertexBufferObjectManager());
		savan.setAnchorCenter(0, 0);
		Body savanB = PhysicsFactory.createCircleBody(mPhysicsWorld, savan, BodyType.DynamicBody, WALL_FIX);
		creditScene.attachChild(savan);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(savan, savanB));
		
		Sprite vivek = new Sprite(460, 50, vivekTR, this.activity.getVertexBufferObjectManager());
		vivek.setAnchorCenter(0, 0);
		Body vivekB = PhysicsFactory.createCircleBody(mPhysicsWorld, vivek, BodyType.DynamicBody, WALL_FIX);
		creditScene.attachChild(vivek);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(vivek, vivekB));
		
		Rectangle upper = new Rectangle(0, 470, 800, 10,this.engine.getVertexBufferObjectManager());
		Rectangle bottom = new Rectangle(0, 0, 800, 10,this.engine.getVertexBufferObjectManager());
		Rectangle left = new Rectangle(0, 0, 10, 480,this.engine.getVertexBufferObjectManager());
		Rectangle right = new Rectangle(790, 0, 10, 480,this.engine.getVertexBufferObjectManager());

		upper.setAnchorCenter(0, 0);
		bottom.setAnchorCenter(0, 0);
		left.setAnchorCenter(0, 0);
		right.setAnchorCenter(0, 0);

		upper.setColor(Color.WHITE);
		bottom.setColor(Color.WHITE);
		left.setColor(Color.WHITE);
		right.setColor(Color.WHITE);

		Body upperB = PhysicsFactory.createBoxBody(mPhysicsWorld, upper, BodyType.StaticBody, WALL_FIX);
		Body bottomB = PhysicsFactory.createBoxBody(mPhysicsWorld, bottom, BodyType.StaticBody, WALL_FIX);
		Body leftB = PhysicsFactory.createBoxBody(mPhysicsWorld, left, BodyType.StaticBody, WALL_FIX);
		Body rightB = PhysicsFactory.createBoxBody(mPhysicsWorld, right, BodyType.StaticBody, WALL_FIX);


		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(upper, upperB));
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(bottom, bottomB));
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(left, leftB));
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(right, rightB));

		creditScene.registerUpdateHandler(mPhysicsWorld);
		creditScene.attachChild(upper);
		creditScene.attachChild(bottom);
		creditScene.attachChild(left);
		creditScene.attachChild(right);
		
		back = new ButtonSprite(100, 400, backTR, engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(soundEnabled)
						clickSound.play();
					loadMenu();
				}
				else{
					back.detachSelf();
					back = new ButtonSprite(100, 400, backonTR, engine.getVertexBufferObjectManager());
					creditScene.attachChild(back);
					setCurrentScene(AllScenes.CREDITS);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		creditScene.registerTouchArea(back);
		creditScene.attachChild(back);
		return creditScene;
	}
	
	public Scene createHelpScene(){
		//TODO Load Help Scene
		helpScene = new Scene();
		helpScene.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		
		backGround = new Sprite(400, 240, splashTR, engine.getVertexBufferObjectManager());
		helpScene.attachChild(backGround);
		
		helpImageSprite= new Sprite(400, 240, helpScreenTR, engine.getVertexBufferObjectManager());
		helpScene.attachChild(helpImageSprite);
		
		menu = new ButtonSprite(400, 420, menuTR, engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(soundEnabled)
						clickSound.play();
					loadMenu();
				}
				else{
					menu.detachSelf();
					menu = new ButtonSprite(400, 420, menuonTR, engine.getVertexBufferObjectManager());
					helpScene.attachChild(menu);
					setCurrentScene(AllScenes.HELP);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		helpScene.registerTouchArea(menu);
		helpScene.attachChild(menu);
		
		ButtonSprite next = new ButtonSprite(700, 400, nextTR, engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(soundEnabled)
						clickSound.play();
					loadHelpScreen(true);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		helpScene.registerTouchArea(next);
		helpScene.attachChild(next);
		
		ButtonSprite back = new ButtonSprite(100, 400, backTR, engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(soundEnabled)
						clickSound.play();
					loadHelpScreen(false);
				}
				
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		helpScene.registerTouchArea(back);
		helpScene.attachChild(back);
		return helpScene;
}
	protected void loadHelpScreen(boolean next) {
		if(next)
			currentHelpScreen = (currentHelpScreen + 1) % totalHelpScreen;
		else
			currentHelpScreen = (currentHelpScreen + totalHelpScreen - 1) % totalHelpScreen;
		
		helpImageSprite.detachSelf();
		helpScreenTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(helpScreenTA, this.activity, "help"+currentHelpScreen+".png",0,0);
		helpImageSprite= new Sprite(400, 240, helpScreenTR, engine.getVertexBufferObjectManager());
		helpScene.attachChild(helpImageSprite);
		setCurrentScene(AllScenes.HELP);
	}

	public Scene createQuitScene(){
		//TODO Load Quit Scene
		quitScene = new Scene();
		quitScene.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
		
		backGround = new Sprite(400, 240, splashTR, engine.getVertexBufferObjectManager());
		quitScene.attachChild(backGround);
		
		quit = new ButtonSprite(400,375,quitTR,engine.getVertexBufferObjectManager());
		quitScene.attachChild(quit);
		
		yes = new ButtonSprite(250,250,yesTR,engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(soundEnabled)
						clickSound.play();
					quitGame();
				}
				else{
					yes.detachSelf();
					yes = new ButtonSprite(250, 250, yesonTR, engine.getVertexBufferObjectManager());
					quitScene.attachChild(yes);
					setCurrentScene(AllScenes.QUIT);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		quitScene.registerTouchArea(yes);
		quitScene.attachChild(yes);
		
		no = new ButtonSprite(550,250,noTR,engine.getVertexBufferObjectManager()){

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(soundEnabled)
						clickSound.play();
					loadMenu();
				}
				else{
					no.detachSelf();
					no = new ButtonSprite(550, 250, noonTR, engine.getVertexBufferObjectManager());
					quitScene.attachChild(no);
					setCurrentScene(AllScenes.QUIT);
				}
				return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
			
		};
		quitScene.registerTouchArea(no);
		quitScene.attachChild(no);		
		return quitScene;
	}
	
	protected void quitGame() {
		backMusic.stop();
		this.activity.finish();
	}

	protected void loadMenu() {
		// TODO Load menu scene
		isGameManagerActive = false;
		
		loadMenuResources();
		createMenuScene();
		setCurrentScene(AllScenes.MENU);
		
		if(backMusic.isPlaying())
			backMusic.pause();
		backMusic.setVolume(1f);
		if(soundEnabled)
			backMusic.resume();
	}

	public AllScenes getCurrentScene() {
		return currentScene;
	}

	public void setCurrentScene(AllScenes currentScene) {
		this.currentScene = currentScene;
		switch (currentScene) {
		case SPLASH:
			this.engine.setScene(splashScene);
			break;

		case MENU:
			this.engine.setScene(menuScene);
			break;
			
		case CREDITS:
			this.engine.setScene(creditScene);
			break;
			
		case HELP:
			this.engine.setScene(helpScene);
			break;
			
		case QUIT:
			this.engine.setScene(quitScene);
			break;
			
		default:
			break;
		}
	}


	public void stopMusic(){
	
		if(backMusic.isPlaying())
			backMusic.pause();
		if(levelMusic1.isPlaying())
			levelMusic1.pause();
		if(levelMusic2.isPlaying())
			levelMusic2.pause();
		if(levelMusic3.isPlaying())
			levelMusic3.pause();
		
		Log.e("Uka","Called");
	}
	}	