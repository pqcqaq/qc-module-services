package test;

import online.zust.qcqcqc.services.utils.Conditions;
import org.junit.jupiter.api.Test;

/**
 * @author qcqcqc
 * Date: 2024/5/13
 * Time: 下午2:00
 */
public class TestConditions {
    @Test
    public void test() {
        Conditions.Tree conditionTree = Conditions.createConditionTree(() -> true);
        conditionTree.onTrue(() -> true).onTrue(() -> false).onFalse(() -> false);
        boolean go = conditionTree.go();
        System.out.println(go);
    }
}
