package me.hehaiyang.codegen;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

/**
 * Created by hehaiyang on 2017/3/9.
 */
public class HelloCompontent implements ApplicationComponent {
    public HelloCompontent() {
    }

    @Override
    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "HelloCompontent";
    }



    public void sayHello() {

        // Show dialog with message

        Messages.showMessageDialog(
                "Hello World!",
                "Sample",
                Messages.getInformationIcon()
        );
    }

}
