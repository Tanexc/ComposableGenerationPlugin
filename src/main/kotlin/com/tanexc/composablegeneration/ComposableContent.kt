package com.tanexc.composablegeneration

internal fun createComposableContent(
    packageName: String,
    functionName: String,
    includePreview: Boolean
): String = buildString {
    if (packageName.isNotEmpty()) {
        appendLine("package $packageName")
        appendLine()
    }

    appendLine("import androidx.compose.runtime.Composable")
    if (includePreview) {
        appendLine("import androidx.compose.ui.tooling.preview.Preview")
    }
    appendLine()

    appendLine("@Composable")
    appendLine("fun $functionName() {")
    appendLine("")
    appendLine("}")

    if (includePreview) {
        appendLine()
        appendLine("@Preview")
        appendLine("@Composable")
        appendLine("fun ${functionName}Preview() {")
        appendLine("    $functionName()")
        appendLine("}")
    }
}
