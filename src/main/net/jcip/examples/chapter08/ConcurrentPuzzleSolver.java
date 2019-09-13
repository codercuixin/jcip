package net.jcip.examples.chapter08;

import java.util.List;
import java.util.concurrent.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/9/11 13:33
 */
public class ConcurrentPuzzleSolver<P, M> {
    protected final ValueLatch<PuzzleNode<P, M>> solution = new ValueLatch<>();
    private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    private final ConcurrentMap<P, Boolean> seen;

    public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
        this.exec = initThreadPool();
        this.seen = new ConcurrentHashMap<>();
        if (exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
            //默认拒绝处理器是抛出异常，这里改成Discard。
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Puzzle<Position, Move> puzzle = new PuzzleImpl();
        ConcurrentPuzzleSolver<Position, Move> solver = new ConcurrentPuzzleSolver<>(puzzle);
        List<Move> moves = solver.solve();
        for (Move move : moves) {
            System.out.println(move);
        }
    }

    public List<M> solve() throws InterruptedException {
        try {
            P p = puzzle.initialPosition();
            exec.execute(newTask(p, null, null));
            //阻塞当前线程直到发现解法
            // 如果没有找到解法，那么solution.getValue会永远阻塞当前线程!!!
            PuzzleNode<P, M> solnPuzleNode = solution.getValue();
            return (solnPuzleNode == null) ? null : solnPuzleNode.asMoveList();
        } finally {
            exec.shutdown();
        }
    }

    private ExecutorService initThreadPool() {
        return Executors.newCachedThreadPool();
    }

    protected Runnable newTask(P p, M m, PuzzleNode<P, M> n) {
        return new SolverTask(p, m, n);
    }

    protected class SolverTask extends PuzzleNode<P, M> implements Runnable {
        SolverTask(P pos, M move, PuzzleNode<P, M> prev) {
            super(pos, move, prev);
        }

        @Override
        public void run() {
            //已经解决问题或者处理过当前位置
            if (solution.isSet() || seen.putIfAbsent(pos, true) != null) {
                return;
            }
            if (puzzle.isGoal(pos)) {
                solution.setValue(this);
            } else {
                for (M m : puzzle.legalMoves(pos)) {
                    exec.execute(newTask(puzzle.move(pos, m), m, this));
                }
            }
        }
    }
}
