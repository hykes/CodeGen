package me.hehaiyang.codegen.file;

import com.github.jknack.handlebars.Handlebars;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import me.hehaiyang.codegen.handlebars.HandlebarsFactory;
import me.hehaiyang.codegen.model.CodeContext;
import me.hehaiyang.codegen.model.CodeTemplate;
import me.hehaiyang.codegen.utils.StringUtils;

import java.util.Objects;

public abstract class FileProvider {

    public final static Handlebars handlebars = HandlebarsFactory.getInstance();

    public Project project;

    public PsiDirectory psiDirectory;

    public FileProvider(Project project, PsiDirectory psiDirectory) {
        this.project = project;
        this.psiDirectory = psiDirectory;
    }

    public abstract void create(CodeTemplate template, CodeContext context) throws Exception;

    public PsiDirectory subDirectory(PsiDirectory psiDirectory, String subPath, Boolean isResources){
        if(StringUtils.isEmpty(subPath)){
            return psiDirectory;
        }else{
            if(subPath.substring(0,1).equals("/")){
                subPath = subPath.substring(1);
            }
            String subPathAttr[] = subPath.split("/");
            if(Objects.nonNull(isResources) && isResources){
                psiDirectory = findResourcesDirectory(psiDirectory);
            }
            return createSubDirectory(psiDirectory, subPathAttr, 0);
        }
    }

    // 创建子目录
    private PsiDirectory createSubDirectory(PsiDirectory psiDirectory, String temp[], int level){
        PsiDirectory subdirectory = psiDirectory.findSubdirectory(temp[level]);
        if(subdirectory == null){
            subdirectory = psiDirectory.createSubdirectory(temp[level]);
        }
        if(temp.length != level + 1){
            return createSubDirectory(subdirectory, temp, level + 1);
        }
        return subdirectory;
    }

    // 根据选择的package目录，找到resources目录
    private PsiDirectory findResourcesDirectory(PsiDirectory psiDirectory){

        PsiDirectory parentDirectory = psiDirectory.getParentDirectory();

        VirtualFile virtualFile = parentDirectory.getVirtualFile();
        String path = virtualFile.getPresentableUrl();

        if(!path.endsWith("src/main")){
            return findResourcesDirectory(parentDirectory);
        }
        PsiDirectory resourcesDirectory = parentDirectory.findSubdirectory("resources");
        if(resourcesDirectory == null){
            resourcesDirectory = parentDirectory.createSubdirectory("resources");
        }
        return resourcesDirectory;
    }

}