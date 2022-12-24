package kitty.site.data.service;

import java.util.function.BiFunction;

public interface Difference<E, F extends Boolean> {
    Boolean isWithinMarginCompare(E first, E second, BiFunction<E, E, F> eefBiFunction);
}
