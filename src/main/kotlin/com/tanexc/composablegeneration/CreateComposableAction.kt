package com.tanexc.composablegeneration

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.roots.ProjectRootManager
import javax.swing.Icon
import com.intellij.openapi.util.IconLoader
import com.intellij.lang.Language

class CreateComposableAction : CreateFileFromTemplateAction(
    "Composable Function",
    "Create a new Composable function",
    ComposableIcons.COMPOSABLE
) {
    override fun buildDialog(
        project: Project,
        directory: PsiDirectory,
        builder: CreateFileFromTemplateDialog.Builder
    ) {
        builder
            .setTitle("Create Composable Function")
            .addKind("Composable Function", ComposableIcons.COMPOSABLE, "Composable.kt")
    }

    override fun createFileFromTemplate(name: String, template: FileTemplate, dir: PsiDirectory): PsiFile {
        val fileName = if (name.endsWith(".kt")) name else "$name.kt"
        val functionName = fileName.removeSuffix(".kt")

        val packageName = getPackageName(dir)
        val fileContent = createComposableContent(packageName, functionName)

        return WriteCommandAction.writeCommandAction(dir.project).compute<PsiFile, Throwable> {
            val psiFile = PsiFileFactory
                .getInstance(dir.project)
                .createFileFromText(fileName, Language.findLanguageByID("kotlin")!!, fileContent)

            dir.add(psiFile) as PsiFile
        }
    }

    private fun createComposableContent(packageName: String, functionName: String): String {
        return buildString {
            if (packageName.isNotEmpty()) {
                appendLine("package $packageName")
                appendLine()
            }

            appendLine("import androidx.compose.runtime.Composable")
            appendLine("import androidx.compose.ui.tooling.preview.Preview")
            appendLine()

            appendLine("@Composable")
            appendLine("fun $functionName() {")
            appendLine("    // TODO: Add your composable content here")
            appendLine("}")
            appendLine()

            appendLine("@Preview")
            appendLine("@Composable")
            appendLine("fun ${functionName}Preview() {")
            appendLine("    $functionName()")
            appendLine("}")
        }
    }

    private fun getPackageName(dir: PsiDirectory): String {
        return try {
            ProjectRootManager
                .getInstance(dir.project)
                .fileIndex
                .getPackageNameByDirectory(dir.virtualFile) ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String {
        return "Create Composable Function"
    }
}

object ComposableIcons {
    val COMPOSABLE: Icon = IconLoader.getIcon("/icons/compose.svg", CreateComposableAction::class.java)
}