package com.hackathon.tetris.board;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hackathon.tetris.blocks.Blocks;
import com.hackathon.tetris.blocks.BlocksTypesEnum;
import com.hackathon.tetris.blocks.SmallBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.hackathon.tetris.blocks.BlocksTypesEnum.*;

public class GameBoard {
    int xCorner=0;
    int yCorner=0;
    Rectangle board=new Rectangle(xCorner,yCorner,200,200);

    List<Blocks> currentBlocks;
    Blocks activeBlock =null;

    public GameBoard(){
        ShapeRenderer shapeRenderer=new ShapeRenderer();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        currentBlocks = new ArrayList<Blocks>();
        activeBlock = generateNewBlock();
    }
    public void removeRow(int number) {

    }
    public boolean checkBorderCollision() {
        for(int i=0; i<activeBlock.getVertices().length;i++) {
            //left or right
            if (i%2==0) {
                if (activeBlock.getVertices()[i]<board.getX())
                    return true;
                if (activeBlock.getVertices()[i]>board.getX()+board.getWidth())
                    return true;
            }
            //down
            else {
                if (activeBlock.getVertices()[i]>board.getY())
                    return true;
            }
        }
        return false;
    }
    public Blocks generateNewBlock() {
        if (activeBlock!=null)
            currentBlocks.add(activeBlock);
        Random random=new Random();
        int numberOfType=random.nextInt(7)+1;
        BlocksTypesEnum newBlockType=null;
        switch (numberOfType) {
            case 1:
                newBlockType=Z;
                break;
            case 2:
                newBlockType=NAIL;
                break;
            case 3:
                newBlockType=L;
                break;
            case 4:
                newBlockType=Z_INVERSE;
                break;
            case 5:
                newBlockType=SQUARE;
                break;
            case 6:
                newBlockType=L_INVERSE;
                break;
            case 7:
                newBlockType=LINE;
        }
        float scale=board.getWidth()/10;
        Blocks block = new Blocks(newBlockType,scale);
        int rotation = random.nextInt(4);
        Vector2 position=new Vector2(xCorner+board.getWidth()/2-scale,yCorner+board.getHeight());
        block.setRotation(rotation);
        block.setPosition(position);
        return block;


    }
    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(22)){ //RIGHT - 22
            activeBlock.reposition(new Vector2(40,0));
        }
        else if(Gdx.input.isKeyJustPressed(21))
            activeBlock.translate(-40,0);
        else if(Gdx.input.isKeyJustPressed(19))
            activeBlock.handleRotation();
    }
    public void updatePosition(float dt) {
        handleInput();
        Vector2 newPosition=activeBlock.getPosition().add(activeBlock.getVelocity().scl(dt));
        activeBlock.setPosition(newPosition.x,newPosition.y);
    }
    public void handleRotation() {
        activeBlock.rotate(90);
        if(checkCollisionsAfterRotation()) {
            activeBlock.rotate(-90);
        }
    }
    public boolean checkCollisionsAfterRotation() {
        float[] transformedVertices=activeBlock.getTransformedVertices();
        if (checkBorderCollision())
            return true;
        for (int i=0;i<transformedVertices.length-1;i++) {
            if (activeBlock.contains(i, i + 1))
                return true;
        }
        return false;
    }

    public List<Blocks> getCurrentBlocks() {
        return currentBlocks;
    }

}
