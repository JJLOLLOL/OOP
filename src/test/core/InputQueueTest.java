package core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class InputQueueTest {

    @Test
    void offerAndPollOperateInFifoOrder() {
        InputQueue queue = new InputQueue();

        queue.offer("one");
        queue.offer("two");

        assertEquals("one", queue.poll());
        assertEquals("two", queue.poll());
        assertNull(queue.poll());
    }
}
