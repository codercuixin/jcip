package net.jcip.examples.chapter08;

import java.util.Set;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 10:44
 * Abstraction for puzzles like the 'sliding blocks puzzle'
 */
public interface Puzzle<P, M> {
    P initialPosition();
    boolean isGoal(P position);
    Set<M> legalMoves(P position);
    P move(P position, M move);
}
