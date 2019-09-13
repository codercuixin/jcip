package net.jcip.examples.chapter08;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 13:54
 * 可以识别不存在解法的解答器。
 */
public class PuzzleSolver<P, M> extends ConcurrentPuzzleSolver<P, M> {
    PuzzleSolver(Puzzle<P,M> puzzle){
        super(puzzle);
    }

    @Override
    protected Runnable newTask(P p, M m, PuzzleNode<P, M> n) {
        return new CountingSolverTask(p, m, n);
    }

    private final AtomicInteger taskCount = new AtomicInteger(0);

    /**
     * 每次创建一个新任务时，taskCount加1，任务完成减1。
     * 等到taskCount值等于0，表明没有解法。
     */
    class CountingSolverTask extends SolverTask{
        CountingSolverTask(P pos, M move, PuzzleNode<P, M> prev){
            super(pos, move, prev);
            taskCount.incrementAndGet();
        }

        @Override
        public void run() {
            try {
                super.run();
            }
            finally {
                if(taskCount.decrementAndGet() == 0){
                    solution.setValue(null);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Puzzle<Position, Move> puzzle = new PuzzleImpl();
        PuzzleSolver<Position, Move> solver = new PuzzleSolver<>(puzzle);
        List<Move> moves = solver.solve();
        for (Move move : moves) {
            System.out.println(move);
        }
    }
}
