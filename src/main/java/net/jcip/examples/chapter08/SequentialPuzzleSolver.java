package net.jcip.examples.chapter08;

import javafx.geometry.Pos;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 10:51
 * 串行的谜题解答器
 */
public class SequentialPuzzleSolver<P, M> {
    private final Puzzle<P, M> puzzle;
    private final Set<P> seen = new HashSet<>();
    public SequentialPuzzleSolver(Puzzle<P, M> puzzle){
        this.puzzle = puzzle;
    }

    public List<M> solve(){
        P pos = puzzle.initialPosition();
        return search(new PuzzleNode<P, M>(pos, null, null));
    }

    private List<M> search(PuzzleNode<P, M> node){
        if(!seen.contains(node.pos)){
            seen.add(node.pos);
            if(puzzle.isGoal(node.pos)){
                return node.asMoveList();
            }
            for(M move: puzzle.legalMoves(node.pos)){
                P pos = puzzle.move(node.pos, move);
                PuzzleNode<P, M> child = new PuzzleNode<>(pos, move, node);
                List<M> result = search(child);
                if(result!= null){
                    return result;
                }
            }
        }
        return null;
    }

    public static void main(String[] args){
        Puzzle<Position, Move> puzzle = new PuzzleImpl();
        SequentialPuzzleSolver<Position, Move> solver = new SequentialPuzzleSolver<Position, Move>(puzzle);
        List<Move> list=  solver.solve();
        for(Move move: list){
            System.out.println(move);
        }
    }
}
