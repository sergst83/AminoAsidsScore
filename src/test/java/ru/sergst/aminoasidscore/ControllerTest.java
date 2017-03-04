package ru.sergst.aminoasidscore;

import org.junit.jupiter.api.Test;

/**
 * @author sergey.stanislavsky
 *         created on 19.01.17.
 */
public class ControllerTest {
    @Test
    public void testDouble() {
        System.out.println(Double.parseDouble("2,3".replaceAll(",",".")));
    }
}
