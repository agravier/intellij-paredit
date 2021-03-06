package zjhmale.paredit;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

public final class BarfBackwardsAction extends EditorAction {

    public BarfBackwardsAction() {
        super(new BarfBackwardsActionHandler());
    }

    private static class BarfBackwardsActionHandler extends AbstractSexpActionHandler {
        protected BarfBackwardsActionHandler() {
            super();
        }

        @Override
        protected void executeWriteAction(PsiElement sexp, Editor editor, Project project, DataContext dataContext) {
            PsiElement barfee = SexpUtils.firstChildSexp(sexp);
            if (barfee == null) {
                return;
            }

            PsiElement copy = barfee.copy();
            barfee.delete();
            sexp.getParent().addBefore(copy, sexp);
        }
    }
}
