package day24

import io.ksmt.KContext
import io.ksmt.solver.z3.KZ3Solver
import io.ksmt.utils.getValue
import kotlin.time.Duration.Companion.seconds

fun main() {
    val ctx = KContext()
    with(ctx) {
        // create symbolic variables
        val a by boolSort
        val b by intSort
        val c by intSort

        // create an expression
        val constraint = a and (b ge c + 3.expr)

        KZ3Solver(this).use { solver -> // create a Z3 SMT solver instance
            // assert expression
            solver.assert(constraint)
            solver.assert(b eq 1.expr)

            // check assertions satisfiability with timeout
            val satisfiability = solver.check(timeout = 1.seconds)
            println(satisfiability) // SAT

            // obtain model
            val model = solver.model()

            println("$a = ${model.eval(a)}") // a = true
            println("$b = ${model.eval(b)}") // b = 0
            println("$c = ${model.eval(c)}") // c = -3
        }

    }
}