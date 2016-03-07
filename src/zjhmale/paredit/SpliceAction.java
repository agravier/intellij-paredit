package zjhmale.paredit;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

public class SpliceAction extends EditorAction {

    public SpliceAction() {
        super(new SpliceActionHandler());
    }

    private static class SpliceActionHandler extends AbstractSexpActionHandler {
        protected SpliceActionHandler() {
            super();
        }

        @Override
        protected void executeWriteAction(PsiElement sexp, Editor editor, Project project, DataContext dataContext) {
            PsiElement first = sexp.getFirstChild();
            PsiElement last = sexp.getLastChild();

            if (first.getText().equals("{") && last.getText().equals("}")) {
                first.delete();
                last.delete();
            }
            if (first.getText().equals("[") && last.getText().equals("]")) {
                first.delete();
                last.delete();
            }
            if (first.getText().equals("(") && last.getText().equals(")")) {
                first.delete();
                last.delete();
            }
        }
    }
}
