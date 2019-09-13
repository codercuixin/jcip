package net.jcip.examples.chapter08;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 10:57
 */
public class PuzzleImpl implements Puzzle<Position, Move> {
    private final Position initialPosition = new Position(0, 0);
    private final Position goalPosition = new Position(2, 3);
    private final Set<Move> legalMoves = new HashSet<>();
    {
        legalMoves.add(Move.RIGHT);
        legalMoves.add(Move.UP);
    }
    @Override
    public Position initialPosition() {
        return initialPosition;
    }

    @Override
    public boolean isGoal(Position position) {
        return position.equals(goalPosition);
    }

    @Override
    public Set<Move> legalMoves(Position position) {
        if(position.getX() > goalPosition.getX()){
            return Collections.emptySet();
        }
        if(position.getY() > goalPosition.getY()){
            return Collections.emptySet();
        }
        HashSet<Move> hashSet = new HashSet<>();
        if(position.getX() < goalPosition.getX()){
                hashSet.add(Move.RIGHT);
        }
        if(position.getY() < goalPosition.getY()){
            hashSet.add(Move.UP);
        }
        return hashSet;
    }

    @Override
    public Position move(Position position, Move move) {
        if(move == Move.RIGHT){
            return new Position(position.getX()+1, position.getY());
        }else if(move == Move.UP){
            return new Position(position.getX(), position.getY()+1);
        }
        return null;
    }
}
class Position {
    private final int x;
    private final int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}
enum  Move {
    /**
     * 这里我们规定每次只能向右，或者向上移动一格
     */
    RIGHT, UP,
}