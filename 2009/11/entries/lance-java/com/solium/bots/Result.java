package com.solium.bots;

/**
 * Created by IntelliJ IDEA.
 * User: lance
 * Date: 2-Dec-2009
 * Time: 1:53:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class Result {
    MoveCode _myMove;
    MoveCode _theirMove;

    public Result(MoveCode myMove, MoveCode theirMove) {
	_myMove = myMove;
	_theirMove = theirMove;
    }

    public MoveCode getMyMove() {
	return _myMove;
    }

    public MoveCode getTheirMove() {
	return _theirMove;
    }

    public void setMyMove(MoveCode myMove) {
	_myMove = myMove;
    }

    public void setTheirMove(MoveCode theirMove) {
	_theirMove = theirMove;
    }

    public boolean loss() {
	return _myMove.getMoveToBeat() == _theirMove;
    }
}
