package com.rocketapps.balloonsaga;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.BaseGameActivity;

import com.rocketapps.balloonsaga.GameManager.GameLevels;
import com.rocketapps.balloonsaga.SceneManager.AllScenes;

public class MainActivity extends BaseGameActivity{

	public static final int CAMERA_WIDTH = 800;
	public static final int CAMERA_HEIGHT = 480;
	PhysicsWorld physicsWorld;
	SceneManager sceneManager;
	Camera mCamera;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO General initialization
		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);
		options.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		options.getAudioOptions().setNeedsMusic(true);
		options.getAudioOptions().setNeedsSound(true);
		
		return options;
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		// TODO Create engine compatible for all devices
		//return super.onCreateEngine(pEngineOptions);
		return new LimitedFPSEngine(pEngineOptions, 100);
	}
	
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		// TODO Load splash scene
		sceneManager = new SceneManager(this, mEngine, mCamera);
		sceneManager.loadSplashResources(); 
		
		// TODO Preference Manager 
		sceneManager.soundEnabled = true;
				
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		pOnCreateSceneCallback.onCreateSceneFinished(this.sceneManager.createSplashScene());
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
			
			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {
				// TODO Load Menu after splash scene
				
				BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("background/");
				sceneManager.splashTA = new BitmapTextureAtlas(sceneManager.activity.getTextureManager(),800,480);
				sceneManager.splashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(sceneManager.splashTA, sceneManager.activity, "title.png",0,0);
				sceneManager.splashTA.load();
				
				sceneManager.splashScene.detachChild(sceneManager.backGround);
				sceneManager.backGround = new Sprite(400, 240, sceneManager.splashTR, sceneManager.engine.getVertexBufferObjectManager());
				sceneManager.splashScene.attachChild(sceneManager.backGround);
				sceneManager.setCurrentScene(AllScenes.SPLASH);
				
				mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {
					
					@Override
					public void onTimePassed(TimerHandler pTimerHandler1) {
							mEngine.unregisterUpdateHandler(pTimerHandler1);
							sceneManager.isGameManagerActive = false;
							
							sceneManager.loadMenuResources();
							sceneManager.createMenuScene();
							sceneManager.setCurrentScene(AllScenes.MENU);
					}
				}));
			}
		}));
		
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	@Override
	public void onBackPressed() {
		// TODO onBackPressed event handling function
		if(sceneManager.isGameManagerActive && sceneManager.gameManager.isLevelLocked && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVELSELECT1)
		{
			sceneManager.isGameManagerActive = true;
			sceneManager.gameManager.isLevelLocked = false;
			sceneManager.gameManager.createLevelOneSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT1);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.isLevelLocked && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVELSELECT2)
		{
			sceneManager.isGameManagerActive = true;
			sceneManager.gameManager.isLevelLocked = false;
			sceneManager.gameManager.createLevelTwoSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT2);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVEL1)
		{
			if(GameManager.levelMusic1.isPlaying())
				GameManager.levelMusic1.pause();
			sceneManager.gameManager.createLevelOneSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT1);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVEL2)
		{
			if(GameManager.levelMusic2.isPlaying())
				GameManager.levelMusic2.pause();
			sceneManager.gameManager.createLevelOneSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT1);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVEL3)
		{
			if(GameManager.levelMusic3.isPlaying())
				GameManager.levelMusic3.pause();
			sceneManager.gameManager.createLevelOneSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT1);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVEL4)
		{
			if(GameManager.levelMusic1.isPlaying())
				GameManager.levelMusic1.pause();
			sceneManager.gameManager.createLevelOneSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT1);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVEL5)
		{
			if(GameManager.levelMusic2.isPlaying())
				GameManager.levelMusic2.pause();
			sceneManager.gameManager.createLevelOneSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT1);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVEL6)
		{
			sceneManager.gameManager.createLevelTwoSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT2);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVEL7)
		{
			if(GameManager.levelMusic1.isPlaying())
				GameManager.levelMusic1.pause();
			sceneManager.gameManager.createLevelTwoSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT2);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVEL8)
		{
			if(GameManager.levelMusic2.isPlaying())
				GameManager.levelMusic2.pause();
			sceneManager.gameManager.createLevelTwoSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT2);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVEL9)
		{
			if(GameManager.levelMusic3.isPlaying())
				GameManager.levelMusic3.pause();
			sceneManager.gameManager.createLevelTwoSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT2);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVEL10)
		{
			if(GameManager.levelMusic1.isPlaying())
				GameManager.levelMusic1.pause();
			sceneManager.gameManager.createLevelTwoSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT2);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVELSELECT2)
		{
			sceneManager.gameManager.loadLevelOneSelectionResources();
			sceneManager.gameManager.createLevelOneSelectionScene();
			sceneManager.gameManager.setCurrentScene(GameLevels.LEVELSELECT1);
		}
		else if(sceneManager.isGameManagerActive && sceneManager.gameManager.getCurrentLevel() == GameLevels.LEVELSELECT1){
			sceneManager.isGameManagerActive = false;
			sceneManager.loadMenu();
			sceneManager.setCurrentScene(AllScenes.MENU);
			
		}
		else if(sceneManager.getCurrentScene() == AllScenes.CREDITS || 
				sceneManager.getCurrentScene() == AllScenes.HELP ||
				sceneManager.getCurrentScene() == AllScenes.QUIT){
			sceneManager.loadMenu();
		}
		else if(sceneManager.getCurrentScene() == AllScenes.MENU){
			sceneManager.loadQuitResources();
			sceneManager.createQuitScene();
			sceneManager.setCurrentScene(AllScenes.QUIT);
		}
		else
			super.onBackPressed();
	}
	
}