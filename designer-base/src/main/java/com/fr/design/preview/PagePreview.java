package com.fr.design.preview;

import com.fr.base.io.IOFile;
import com.fr.design.fun.impl.AbstractPreviewProvider;


/**
 * @author richie
 * @date 2015-03-19
 * @since 8.0
 */
public class PagePreview extends AbstractPreviewProvider {
    @Override
    public String nameForPopupItem() {
        return com.fr.design.i18n.Toolkit.i18nText("M-Page_Preview");
    }

    @Override
    public String iconPathForPopupItem() {
        return "com/fr/design/images/buttonicon/pages.png";
    }

    @Override
    public String iconPathForLarge() {
        return "com/fr/design/images/buttonicon/pageb24.png";
    }

    @Override
    public int previewTypeCode() {
        return IOFile.DEFAULT_PREVIEW_TYPE;
    }
}