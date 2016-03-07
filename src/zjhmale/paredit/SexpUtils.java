package zjhmale.paredit;

import com.intellij.psi.PsiElement;

/**
 * Created by zjh on 16/3/7.
 */
public class SexpUtils {
    public static PsiElement lastChildSexp(PsiElement element) {
        PsiElement[] children = element.getChildren();
        return children.length != 0 ? children[children.length - 1] : null;
    }

    public static PsiElement firstChildSexp(PsiElement element) {
        PsiElement[] children = element.getChildren();
        return children.length != 0 ? children[0] : null;
    }
}
