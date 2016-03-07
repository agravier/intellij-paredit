package zjhmale.paredit;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;

public final class BarfForwardsAction extends EditorAction {

    public BarfForwardsAction() {
        super(new BarfForwardsActionHandler());
    }

    public static PsiElement lastChildSexp(PsiElement element) {
        PsiElement[] children = element.getChildren();
        return children.length != 0 ? children[children.length - 1] : null;
    }

    private static class BarfForwardsActionHandler extends AbstractSexpActionHandler {
        protected BarfForwardsActionHandler() {
            super();
        }

        @Override
        protected void executeWriteAction(PsiElement sexp, Editor editor, Project project, DataContext dataContext) {
            PsiElement barfee = lastChildSexp(sexp);
            if (barfee == null) {
                return;
            }

            PsiElement copy = barfee.copy();
            barfee.delete();
            sexp.getParent().addAfter(copy, sexp);

            if (sexp.getChildren().length == 0) {
                sexp.delete();
            }
        }
    }

}
