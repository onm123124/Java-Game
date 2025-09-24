import org.lwjgl.Version;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main {

     // The window handle
    private long window;

    public void run() {
        init();// initialize the things refer to private void init() for more

        //configure the glfw and create a window

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // Window stays hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // window is resizable by ME lol


        //MAIN WINDOW CREATION CODE BELOW HERE
        //DONOT TOUCH UNLESS YOU KNOW WHAT YOU ARE DOING (idont)
        window = glfwCreateWindow(300, 300, "First window", NULL, NULL); //glfwCreateWindow(width, height, title, monitor, share)
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window :(  ");
        // KEY PRESSED MOMENT (processing DEJAVU MOMENT)
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> { // glfwSetKeyCallback(window, callback)
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // I (not me but the program) will detect this in the rendering loop also this will close the program when it should (kinda obv)
        });

        // now we do the C stuff (very complicated memory allocation and management stuff)
        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        loop(); //loop as like a window loop (window is open and on)
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        // this code below frees callbacks and destroys the window (like for a clean screen and to free up some memory created during initialization)
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // this code below terminates glfw and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // Initializor

        //Setup an error callback. defaul stuff
        //will print error in System.err
        GLFWErrorCallback.createPrint(System.err).set();

        // initialize GLFW (this bitch need dat). most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW"); //for case of error (obvisouly)
    } 

    private void loop() {
        // THIS IS VERY IMPORTANT LINE for lwjgls operation with GLFW
        //any context is managed externally (inlcuding openGL)
        //LWJGL detects the context that is current in the current thread
        //creates the GLCapabilities instance and makes them OpenGL
        //bindings available for use.
        GL.createCapabilities();

        // set za color
        glClearColor(1.0f, 0.0f, 1.0f, 0.0f);

        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

            glfwSwapBuffers(window); // swap the color buffers

            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    };

    public static void main(String[] args) {
        new Main().run(); // <-- This is essential!
    }
}
