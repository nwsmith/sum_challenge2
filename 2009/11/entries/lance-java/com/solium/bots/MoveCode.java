package com.solium.bots;

public enum MoveCode {
    R("P"),
    P("S"),
    S("R"),
    UNKNOWN("UNKNOWN");

    String _moveToBeat;

    MoveCode(String moveToBeat) {
	_moveToBeat = moveToBeat;
    }

    MoveCode getMoveToBeat() {
	return MoveCode.valueOf(_moveToBeat);
    }
}
