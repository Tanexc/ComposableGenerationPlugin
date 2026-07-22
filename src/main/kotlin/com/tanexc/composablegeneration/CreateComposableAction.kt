package com.tanexc.composablegeneration

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplate
import com.intellij.lang.Language
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import javax.swing.Icon

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
            .addKind("Composable", ComposableIcons.COMPOSABLE, "Composable.kt")
            .addKind("Composable + Preview", ComposableIcons.COMPOSABLE, "ComposableWithPreview.kt")
    }

    override fun createFileFromTemplate(
        name: String,
        template: FileTemplate,
        dir: PsiDirectory
    ): PsiFile {
        val functionName = name.removeKtSuffix()
        val fileName = "$functionName.kt"
        val fileContent = createComposableContent(
            packageName = getPackageName(dir),
            functionName = functionName,
            includePreview = template.name.removeSuffix(".kt") == PREVIEW_TEMPLATE_NAME
        )

        return WriteCommandAction.writeCommandAction(dir.project).compute<PsiFile, Throwable> {
            val kotlinLanguage = checkNotNull(Language.findLanguageByID("kotlin")) {
                "The Kotlin plugin is required to create a composable function"
            }
            val psiFile = PsiFileFactory.getInstance(dir.project)
                .createFileFromText(fileName, kotlinLanguage, fileContent)

            dir.add(psiFile) as PsiFile
        }
    }

    private fun getPackageName(directory: PsiDirectory): String =
        JavaDirectoryService.getInstance()
            .getPackage(directory)
            ?.qualifiedName
            .orEmpty()

    override fun getActionName(directory: PsiDirectory, newName: String, templateName: String): String {
        return "Create Composable Function"
    }

    private companion object {
        const val PREVIEW_TEMPLATE_NAME = "ComposableWithPreview"
    }
}

private fun String.removeKtSuffix(): String =
    if (endsWith(".kt", ignoreCase = true)) dropLast(3) else this

object ComposableIcons {
    val COMPOSABLE: Icon = IconLoader.getIcon("/icons/compose.svg", CreateComposableAction::class.java)
}
