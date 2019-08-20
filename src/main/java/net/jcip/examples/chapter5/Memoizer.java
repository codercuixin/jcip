package net.jcip.examples.chapter5;

import net.jcip.examples.utils.LaunderThrowable;

import java.util.Map;
import java.util.concurrent.*;

/**
 * * @Author: cuixin
 * * @Date: 2019/8/20 16:26
 * Final implementation of Memoizer
 */
public class Memoizer<A, V> implements Computable<A, V> {
    private final Map<A, Future<V>> cache = new ConcurrentHashMap<>();
    private final Computable<A, V> c;

    public Memoizer(Computable<A, V> c) {
        this.c = c;
    }

    @Override
    public V compute(A arg) throws InterruptedException {
        while (true) {
            Future<V> f = cache.get(arg);
            if (f == null) {
                Callable<V> eval = new Callable<V>() {
                    @Override
                    public V call() throws Exception {
                        return c.compute(arg);
                    }
                };
                FutureTask<V> ft = new FutureTask<>(eval);
                f = cache.putIfAbsent(arg, ft);
                if(f == null){
                    f = ft;
                    ft.run();
                }
            }
            try {
                return f.get();
            }catch (CancellationException e){
                cache.remove(arg, f);
            }
            catch (ExecutionException e) {
                throw LaunderThrowable.launderThrowable(e.getCause());
            }
        }
    }
}
