package zjhmale.paredit;

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

abstract class AbstractSexpActionHandler extends EditorWriteActionHandler {
    protected AbstractSexpActionHandler() {
    }

    private static boolean anyOf(char c, String s) {
        return s.indexOf(c) != -1;
    }

    private static boolean isWrap(PsiElement element, int caretPos) {
        String text = element.getText();
        int offset = element.getTextOffset();
        boolean isWrapBraces = false;
        boolean isWrapParenthesis = false;
        boolean isWrapBrackets = false;
        if (text.contains("{") && text.contains("}")) {
            int lpos = offset + text.indexOf("{");
            int rpos = offset + text.indexOf("}");
            isWrapBraces = lpos <= caretPos && caretPos <= rpos;
        }
        if (text.contains("(") && text.contains(")")) {
            int lpos = offset + text.indexOf("(");
            int rpos = offset + text.indexOf(")");
            isWrapParenthesis = lpos <= caretPos && caretPos <= rpos;
        }
        if (text.contains("[") && text.contains("]")) {
            int lpos = offset + text.indexOf("[");
            int rpos = offset + text.indexOf("]");
            isWrapBrackets = lpos <= caretPos && caretPos <= rpos;
        }
        return isWrapBraces || isWrapParenthesis || isWrapBrackets;
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
