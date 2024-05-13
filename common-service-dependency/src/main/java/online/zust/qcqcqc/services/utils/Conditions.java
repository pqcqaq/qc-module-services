package online.zust.qcqcqc.services.utils;

import online.zust.qcqcqc.services.utils.intfs.SimpleCondition;

/**
 * @author qcqcqc
 * Date: 2024/5/13
 * Time: 下午1:41
 */
public class Conditions {

    private Conditions() {
    }

    public static class Node {
        private SimpleCondition condition;
        private Node onTrue;
        private Node onFalse;
        private Node parent;
        private boolean result;

        private boolean go() {
            if (condition == null) {
                // 如果自己没有设置条件，那么就返回结果
                return parent.result;
            }
            result = condition.test();
            if (result) {
                if (onTrue == null) {
                    return true;
                }
                return onTrue.go();
            } else {
                if (onFalse == null) {
                    return false;
                }
                return onFalse.go();
            }
        }

        public Node onTrue(SimpleCondition simpleCondition) {
            onTrue = new Node();
            onTrue.condition = simpleCondition;
            onTrue.parent = this;
            return onTrue;
        }

        public Node onFalse(SimpleCondition simpleCondition) {
            onFalse = new Node();
            onFalse.condition = simpleCondition;
            onFalse.parent = this;
            return onFalse;
        }
    }

    public static class Tree {
        private final Node root;

        public Tree(SimpleCondition simpleCondition) {
            root = new Node();
            root.condition = simpleCondition;
        }

        public boolean go() {
            return root.go();
        }

        public Node onTrue(SimpleCondition simpleCondition) {
            root.onTrue(simpleCondition);
            return root;
        }

        public Node onFalse(SimpleCondition simpleCondition) {
            root.onFalse(simpleCondition);
            return root;
        }
    }

    public static Tree createConditionTree(SimpleCondition simpleCondition) {
        return new Tree(simpleCondition);
    }
}
