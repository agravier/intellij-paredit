package zjhmale.paredit;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl;
import com.intellij.psi.util.PsiTreeUtil;

public final class SlurpForwardsAction extends EditorAction {

    public SlurpForwardsAction() {
        super(new SlurpForwardsActionHandler());
    }

    private static class SlurpForwardsActionHandler extends AbstractSexpActionHandler {
        protected SlurpForwardsActionHandler() {
            super();
        }

        @Override
        protected void executeWriteAction(PsiElement sexp, Editor editor, Project project, DataContext dataContext) {
            PsiElement slurpee = PsiTreeUtil.getNextSiblingOfType(sexp, PsiElement.class);

            while (slurpee instanceof PsiWhiteSpaceImpl) {
                slurpee = PsiTreeUtil.getNextSiblingOfType(slurpee, PsiElement.class);
            }

            if (slurpee == null) {
                return;
            }

            PsiElement copy = slurpee.copy();
            slurpee.delete();
            PsiElement lastChild = SexpUtils.lastChildSexp(sexp);
            if (lastChild != null) {
                sexp.addAfter(copy, lastChild);
            } else {
                sexp.addBefore(copy, sexp.getLastChild());
            }
        }
    }
}
