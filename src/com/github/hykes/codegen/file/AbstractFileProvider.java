package com.github.hykes.codegen.file;

import com.github.hykes.codegen.model.CodeContext;
import com.github.hykes.codegen.model.CodeTemplate;
import com.github.jknack.handlebars.Handlebars;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.github.hykes.codegen.utils.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.jetbrains.java.generate.velocity.VelocityFactory;

import java.util.Objects;

/**
 * 文件生成器抽象类
 *
 * @author: hehaiyangwork@qq.com
 * @date: 2017/04/07
 */
public abstract class AbstractFileProvider {

    protected final static Handlebars HANDLEBARS = HandlebarsFactory.getInstance();

    protected final static VelocityEngine velocityEngine = VelocityFactory.getVelocityEngine();

    public Project project;

    public PsiDirectory psiDirectory;

    public AbstractFileProvider(Project project, PsiDirectory psiDirectory) {
        this.project = project;
        this.psiDirectory = psiDirectory;
    }

    public abstract void create(CodeTemplate template, CodeContext context) throws Exception;

    public PsiDirectory subDirectory(PsiDirectory psiDirectory, String subPath, Boolean isResources){
        if(StringUtils.isEmpty(subPath)){
            return psiDirectory;
        }else{
            if("/".equals(subPath.substring(0,1))){
                subPath = subPath.substring(1);
            }
            String[] subPathAttr = subPath.split("/");
            if(Objects.nonNull(isResources) && isResources){
                psiDirectory = findResourcesDirectory(psiDirectory);
            }
            return createSubDirectory(psiDirectory, subPathAttr, 0);
        }
    }

    /**
     * 创建子目录
     * @param psiDirectory
     * @param temp
     * @param level
     * @return
     */
    private PsiDirectory createSubDirectory(PsiDirectory psiDirectory, String[] temp, int level){
        PsiDirectory subdirectory = psiDirectory.findSubdirectory(temp[level]);
        if(subdirectory == null){
            subdirectory = psiDirectory.createSubdirectory(temp[level]);
        }
        if(temp.length != level + 1){
            return createSubDirectory(subdirectory, temp, level + 1);
        }
        return subdirectory;
    }

    /**
     * 根据选择的package目录，找到resources目录
     * @param psiDirectory
     * @return
     */
    private PsiDirectory findResourcesDirectory(PsiDirectory psiDirectory){

        PsiDirectory parentDirectory = psiDirectory.getParentDirectory();

        VirtualFile virtualFile = parentDirectory.getVirtualFile();
        String path = virtualFile.getPresentableUrl();

        if(!path.endsWith("src/main")){
            // 如果已经找到了项目根路径, 终止递归
            if (!path.endsWith("/" + project.getName())) {
                return findResourcesDirectory(parentDirectory);
            }
        }
        PsiDirectory resourcesDirectory = parentDirectory.findSubdirectory("resources");
        if(resourcesDirectory == null){
            resourcesDirectory = parentDirectory.createSubdirectory("resources");
        }
        return resourcesDirectory;
    }

}