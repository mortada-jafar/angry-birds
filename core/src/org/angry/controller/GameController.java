package org.angry.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.angry.Model.Vector;
import org.angry.view.Bird;
import org.angry.view.Box;
import org.angry.view.Pig;
import org.angry.Model.World;
import org.angry.Model.ControllerLogic;
import org.angry.Model.ImpulseMath;


import javax.swing.*;

/**
 * Created by MyBaby on 12/3/2015.
 */
public class GameController implements Disposable {
    public Stage stage;
    private Viewport viewport;
    public Label pig, box, addBird, state;
    World world;


    public void setState(String text) {
        this.state.setText(text);
    }

    public GameController(World world, SpriteBatch sb, Bird bird) {

        viewport = new FitViewport(AngryBirds.V_WIDTH, AngryBirds.V_HIEGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Table table = new Table();

        table.setFillParent(true);
        addBird = new Label("Add new Bird", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        addBird.addListener(new birdListener(world, bird));
        box = new Label("Add New Box", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        box.addListener(new AddNewBox(world));

        pig = new Label("Add Pig", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        pig.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Ellipse ellipse = new Ellipse();
                ellipse.width = 50;
                ellipse.setPosition(250, 250);
                ControllerLogic.circleArray.add(new Pig(world, ellipse));
            }
        });

        state = new Label(bird.getState().toString(), new Label.LabelStyle(new BitmapFont(), Color.WHITE));


        Slider.SliderStyle style = new Slider.SliderStyle(new SpriteDrawable(new Sprite(new Texture("angrybirds/slider/back.png"))), new SpriteDrawable(new Sprite(new Texture("angrybirds/slider/arrow.png"))));
        Slider s = new Slider(-100, 100, 1, false, style);
        s.setValue(-50f);
        s.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ImpulseMath.GRAVITY = new Vector(0.0f, s.getValue());
            }
        });

        Slider.SliderStyle timestyle = new Slider.SliderStyle(new SpriteDrawable(new Sprite(new Texture("angrybirds/slider/back.png"))), new SpriteDrawable(new Sprite(new Texture("angrybirds/slider/arrowspeed.png"))));
        Slider speed= new Slider(0, 0.1f, 0.01f, false, timestyle );
        speed.setValue(0.05f);
        speed.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ImpulseMath.DT= speed.getValue();
                world.setDt(ImpulseMath.DT);
            }
        });

        table.top();
        table.add(addBird).expandX().padTop(10).top();
        table.add(box).expandX().padTop(10).top();
        table.add(pig).expandX().padTop(10).top();
        stage.addActor(table);
        s.setSize(400, 40);
        stage.addActor(s);
        speed.setSize(400, 40);
        speed.setPosition(500,06);
        stage.addActor(speed);
        state.setPosition(1200, 0);
        stage.addActor(state);

    }

    @Override
    public void dispose() {
        stage.dispose();

    }

    private class birdListener extends ClickListener {
        Bird bird;
        World world;

        public birdListener(World world, Bird bird) {
            this.bird = bird;
            this.world = world;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
//            world.destroyBody(bird.b2body);
//            bird = new Bird(world, 0);
        }
    }

    private class AddNewBox extends ClickListener {
        World world;

        public AddNewBox(World world) {
            this.world = world;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            JTextField xField = new JTextField(5);
            JTextField yField = new JTextField(5);
            JTextField sf= new JTextField(5);
            JTextField df= new JTextField(5);

            JPanel myPanel = new JPanel();
            myPanel.add(new JLabel("Width :"));
            myPanel.add(xField);
            myPanel.add(javax.swing.Box.createHorizontalStrut(15)); // a spacer
            myPanel.add(new JLabel("Height :"));
            myPanel.add(yField);
            myPanel.add(javax.swing.Box.createHorizontalStrut(15)); // a spacer
            myPanel.add(new JLabel("Static:"));
            myPanel.add(sf);
            myPanel.add(javax.swing.Box.createHorizontalStrut(15)); // a spacer
            myPanel.add(new JLabel("Dynamic :"));
            myPanel.add(df);
            int result;
            float width, Height,sff,dff;
            do {
                result = JOptionPane.showConfirmDialog(null, myPanel, "Please Enter Width and Height , static & dynamic Values", JOptionPane.OK_CANCEL_OPTION);

                width = Float.valueOf(xField.getText().isEmpty() ? "10" : xField.getText());
                Height = Float.valueOf(yField.getText().isEmpty() ? "10" : yField.getText());
                dff = Float.valueOf(yField.getText().isEmpty() ? "0.5": df.getText());
                sff= Float.valueOf(yField.getText().isEmpty() ? "0.5" : sf.getText());
            } while ((width > 350 || Height > 350||dff>1||sff>1) && result != JOptionPane.CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                Rectangle rect = new Rectangle(200, 200, width, Height);
                ControllerLogic.boxArray.add(new Box(world, rect,dff,sff));
                //boxArray.add(new (world, rect));
            }
        }


    }

}
