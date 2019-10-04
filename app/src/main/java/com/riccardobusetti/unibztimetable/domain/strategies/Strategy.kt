package com.riccardobusetti.unibztimetable.domain.strategies

/**
 * Interface which describes the behavior of a strategy which is responsible
 * of defining which kind of strategy are we using the fetch data.
 *
 * A strategy is than used by the repository which based on different variables chooses
 * intelligently which kind of strategy is more appropriate to fetch data with.
 *
 * An example of strategies can be database, file, cache, remote...
 *
 * @author Riccardo Busetti
 */
interface Strategy