package com.wangduwei.gradle

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


class GradlePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        System.out.println("---------------apply------------")
        project.extensions.getByType(AppExtension).registerTransform(new MyTransform(project))

    }
}