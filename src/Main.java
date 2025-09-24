import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

public class Main {

    private void init() {
    // Initializor
    GLFWErrorCallback.createPrint(System.err).set();
}
    public static void main(String[] args) {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
    }
}
