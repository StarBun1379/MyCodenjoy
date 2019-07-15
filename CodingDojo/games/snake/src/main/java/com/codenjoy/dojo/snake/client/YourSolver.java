package com.codenjoy.dojo.snake.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

import java.util.HashSet;
import java.util.Set;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        //System.out.println(board.toString());
        Point appleP = board.getApples().get(0);
        Point curP = board.getHead();
        Point stone = board.getStones().get(0);
        Direction curD = board.getSnakeDirection();

        if (curD == null) return Direction.random().toString();

        Set<Direction> forbiddenMoves = new HashSet<>();
        switch (curD) {
            case LEFT:
                forbiddenMoves.add(Direction.RIGHT);
                break;
            case RIGHT:
                forbiddenMoves.add(Direction.LEFT);
                break;
            case UP:
                forbiddenMoves.add(Direction.DOWN);
                break;
            case DOWN:
                forbiddenMoves.add(Direction.UP);
                break;
        }

        if  (curP.getX() == 13) forbiddenMoves.add(Direction.RIGHT);
        if  (curP.getX() == 1) forbiddenMoves.add(Direction.LEFT);
        if  (curP.getY() == 13) forbiddenMoves.add(Direction.UP);
        if  (curP.getY() == 1) forbiddenMoves.add(Direction.DOWN);

        for (Point p : board.getSnake()){
            if (curP.getX() == p.getX()+1 && curP.getY() == p.getY()) forbiddenMoves.add(Direction.LEFT);
            if (curP.getX() == p.getX()-1 && curP.getY() == p.getY()) forbiddenMoves.add(Direction.RIGHT);
            if (curP.getY() == p.getY()+1 && curP.getX() == p.getX()) forbiddenMoves.add(Direction.DOWN);
            if (curP.getY() == p.getY()-1 && curP.getX() == p.getX()) forbiddenMoves.add(Direction.UP);
        }

        if (curP.getX() == stone.getX()+1 && curP.getY() == stone.getY()) forbiddenMoves.add(Direction.LEFT);
        if (curP.getX() == stone.getX()-1 && curP.getY() == stone.getY()) forbiddenMoves.add(Direction.RIGHT);
        if (curP.getY() == stone.getY()+1 && curP.getX() == stone.getX()) forbiddenMoves.add(Direction.DOWN);
        if (curP.getY() == stone.getY()-1 && curP.getX() == stone.getX()) forbiddenMoves.add(Direction.UP);


        Direction nextMove = Direction.random(); ;

        if (appleP.getX() == curP.getX() && appleP.getY() > curP.getY() && curD == Direction.UP) nextMove = Direction.UP;
        if (appleP.getX() == curP.getX() && appleP.getY() < curP.getY() && curD == Direction.DOWN) nextMove = Direction.DOWN;
        if (appleP.getX() < curP.getX() && appleP.getY() == curP.getY() && curD == Direction.LEFT) nextMove = Direction.LEFT;
        if (appleP.getX() > curP.getX() && appleP.getY() == curP.getY() && curD == Direction.RIGHT) nextMove = Direction.RIGHT;

        if (((Direction.RIGHT == curD) || ((Direction.LEFT == curD))) && appleP.getY() > curP.getY()) nextMove = Direction.UP;
        else if (((Direction.RIGHT == curD) || ((Direction.LEFT == curD))) && appleP.getY() < curP.getY())nextMove =  Direction.DOWN;
        else if (((Direction.UP == curD) || ((Direction.DOWN == curD))) && appleP.getX() > curP.getX()) nextMove = Direction.RIGHT;
        else if (((Direction.UP == curD) || ((Direction.DOWN == curD))) && appleP.getX() < curP.getX()) nextMove = Direction.LEFT;


        System.out.println("forbidden Moves: " + forbiddenMoves.toString());

        int i = 0;
        while (forbiddenMoves.contains(nextMove)){
            if (i == 20) break;
            else i++;
            nextMove = Direction.random();
        }

        return nextMove.toString();
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://localhost:8080/codenjoy-contest/board/player/mnt36nn68xoooqjq3eq6?code=7889305304650998830&",
                new YourSolver(new RandomDice()),
                new Board());
    }

}
