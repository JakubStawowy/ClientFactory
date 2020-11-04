import Client.Client;
import Factory.FirstProductManufacture;
import Factory.SecondProductManufacture;
import Factory.Server;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {

    @Test
    void getDatabaseConnector() {
        assertDoesNotThrow(()->{
            Server server = new Server();
            server.start();
            Thread.sleep(2000);
            assertNotEquals(null, server.getDatabaseConnector());
            server.closeServer();
        });
    }

    @Test
    void getFirstProductManufacture() {
        assertDoesNotThrow(()->{
            Server server = new Server();
            server.start();
            Thread.sleep(2000);
            assertEquals(server.getFirstProductManufacture().getClass(), FirstProductManufacture.class);
            server.closeServer();
        });
    }

    @Test
    void getSecondProductManufacture() {
        assertDoesNotThrow(()->{
            Server server = new Server();
            server.start();
            Thread.sleep(2000);
            assertEquals(server.getSecondProductManufacture().getClass(), SecondProductManufacture.class);
            server.closeServer();
        });
    }
    @Test
    void getObservers() {
        assertDoesNotThrow(()->{
            Server server = new Server();
            server.start();
            Thread.sleep(2000);
            for (int i = 0; i < 10; i++) {
                new Client();
            }
            Thread.sleep(2000);
            assertEquals(10, server.getObservers().size());
            server.closeServer();
        });
    }
}