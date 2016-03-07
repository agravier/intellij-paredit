package zjhmale.paredit;/*
 * Copyright 2009 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorWriteActionHandler;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An action handler that operates on the current s-expression in the current editor.
 *
 * @author <a href="mailto:ianp@ianp.org">Ian Phillips</a>
 */
abstract class AbstractSexpActionHandler extends EditorWriteActionHandler {
    protected AbstractSexpActionHandler() {
    }

    private static boolean anyOf(char c, String s) {
        return s.indexOf(c) != -1;
    }

    private static boolean isWrap(PsiElement element, int caretPos) {
        String text = element.getText();
        int offset = element.getTextOffset();
        if (text.contains("{") && text.contains("}")) {
            int lpos = offset + text.indexOf("{");
            int rpos = offset + text.indexOf("}");
            return lpos <= caretPos && caretPos <= rpos;
        }
        if (text.contains("(") && text.contains(")")) {
            int lpos = offset + text.indexOf("(");
            int rpos = offset + text.indexOf(")");
            return lpos <= caretPos && caretPos <= rpos;
        }
        if (text.contains("[") && text.contains("]")) {
            int lpos = offset + text.indexOf("[");
            int rpos = offset + text.indexOf("]");
            return lpos <= caretPos && caretPos <= rpos;
        }
        return false;
    }

    public static
    @Nullable
    PsiElement findSexpAtCaret(@NotNull Editor editor) {
        Project project = editor.getProject();
        if (project == null) {
            return null;
        }

        VirtualFile vfile = FileDocumentManager.getInstance().getFile(editor.getDocument());

        if (vfile == null) return null;

        PsiFile file = PsiManager.getInstance(project).findFile(vfile);
        if (file == null) {
            return null;
        }

        CharSequence chars = editor.getDocument().getCharsSequence();
        int offset = editor.getCaretModel().getOffset();

        if (offset == 0) {
            return null;
        }

        PsiElement element = file.findElementAt(offset);
        while (element != null && !isWrap(element, offset)) {
            element = element.getParent();
        }
        return element;
    }

    @Override
    public void executeWriteAction(Editor editor, DataContext dataContext) {
        Project project = editor.getProject();
        if (project == null) {
            return;
        }

        PsiElement sexp = findSexpAtCaret(editor);
        if (sexp == null) return;

        executeWriteAction(sexp, editor, project, dataContext);
    }

    protected abstract void executeWriteAction(PsiElement exp, Editor editor, Project project, DataContext dataContext);

}
