package me.hehaiyang.codegen;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;

/**
 * Created by hehaiyang on 2017/3/10.
 */
public class HelloAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here

        Application application = ApplicationManager.getApplication();

        HelloCompontent myComponent = application.getComponent(HelloCompontent.class);

        myComponent.sayHello();
    }
}
