package org.ovirt.engine.core.bll;

import static org.junit.Assert.assertTrue;

import java.util.BitSet;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.ovirt.engine.core.common.action.ActionType;

public class ActionTypeTest {
    private static final int bitSetSize = 5000;

    @Test
    public void testAuditLogTypeValueUniqueness() {
        BitSet bitset = new BitSet(bitSetSize);
        Set<Integer> nonUniqueValues = new TreeSet<>();

        for (ActionType at : ActionType.values()) {
            if (bitset.get(at.getValue())) {
                nonUniqueValues.add(at.getValue());
            }
            else {
                bitset.set(at.getValue());
            }
        }
        assertTrue("ActionType contains the following non unique values: " + nonUniqueValues, nonUniqueValues.isEmpty());
    }

    @Test
    public void testCommandClassExistence() {
        CommandEnumTestUtils.testCommandsExist(ActionType.class, at -> CommandsFactory.getCommandClass(at.name()));
    }

    @Test
    public void testCommandClassHasEnum() {
        CommandEnumTestUtils.testCommandClassHasEnum(ActionType.class, CommandBase.class, "Command");
    }
}
