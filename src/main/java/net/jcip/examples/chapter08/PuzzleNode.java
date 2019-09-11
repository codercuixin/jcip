package net.jcip.examples.chapter08;

import net.jcip.annotations.Immutable;

import java.util.LinkedList;
import java.util.List;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 10:46
 * 用于谜题解决框架的链表节点
 */
@Immutable
public class PuzzleNode <P, M>{
    final P pos;
    final M move;
    final PuzzleNode<P, M> prev;

    public PuzzleNode(P pos, M move, PuzzleNode<P, M> prev) {
        this.pos = pos;
        this.move = move;
        this.prev = prev;
    }

    /**
     * 将之前走的步骤转换成一个链表，最开始在前面。
     */
    List<M> asMoveList(){
        List<M> solution = new LinkedList<>();
        for(PuzzleNode<P, M> n= this; n.move != null; n = n.prev){
            solution.add(0, n.move);
        }
        return solution;
    }
}
