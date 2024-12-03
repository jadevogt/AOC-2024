package com.expedient.adventofcodejade.solutions;

import com.expedient.adventofcodejade.BaseSolution;
import com.expedient.adventofcodejade.common.PuzzleInput;
import com.expedient.adventofcodejade.util.StringTools;

public class SolutionDay3 extends BaseSolution {
    public SolutionDay3(PuzzleInput input, PuzzleInput sampleInputOne, PuzzleInput sampleInputTwo) {
        super(input, sampleInputOne, sampleInputTwo);
    }

    @Override
    public String partOne(PuzzleInput input) {
        var m = StringTools.getMatcher(input.getSingleString(), "mul\\(\\d{1,3},\\d{1,3}\\)");
        int total = 0;
        while (m.find()) {
            var g = m.group();
            int mul1 = Integer.parseInt(g.split(",")[0].replace("mul(", ""));
            int mul2 = Integer.parseInt(g.split(",")[1].replace(")", ""));
            total += mul1 * mul2;
        }
        return Integer.toString(total);
    }

    @Override
    public String partTwo(PuzzleInput input) {
        var m = StringTools.getMatcher(input.getSingleString(), "(mul\\(\\d{1,3},\\d{1,3}\\))|(do\\(\\))|(don't\\(\\))");
        int total = 0;
        boolean active = true;
        while (m.find()) {
            var g = m.group();
            if (g.equals("do()")) {
                active = true;
                continue;
            }
            if (g.equals("don't()")) {
                active = false;
                continue;
            }
            if (active) {
                int mul1 = Integer.parseInt(g.split(",")[0].replace("mul(", ""));
                int mul2 = Integer.parseInt(g.split(",")[1].replace(")", ""));
                total += mul1 * mul2;
            }
        }
        return Integer.toString(total);
    }
}
