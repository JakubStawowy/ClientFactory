package Factory;

import java.io.IOException;

public interface Observer {
    void update();
    void close() throws IOException;
    String getFeedback();
}
