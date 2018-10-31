package com.fr.conf;

import com.fr.design.designer.properties.items.Item;
import com.fr.report.fun.ReportFitAttrProvider;
import com.fr.stable.StringUtils;

/**
 * Created by Administrator on 2016/4/13/0013.
 */
public enum FitAttrState {
    DEFAULT(0) {
        @Override
        public String description() {
            return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Fit_Default");
        }

    },
    // 横向自适应, 纵向根据横向的比例来适配
    HORIZONTAL_FIT(1) {
        @Override
        public String description() {
            return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Fit_Horizontal");
        }
    },
    // 双向自适应, 横纵向都是根据页面宽高来计算
    DOUBLE_FIT(2) {
        @Override
        public String description() {
            return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Fit_Horizontal_Vertical");
        }
    },

    NOT_FIT(3) {
        @Override
        public String description() {
            return com.fr.design.i18n.Toolkit.i18nText("FR-Designer_Fit_No");
        }
    };


    private int state;


    FitAttrState(int state) {
        this.state = state;
    }

    public static FitAttrState parse(ReportFitAttrProvider attr) {

        if (attr == null) {
            return DEFAULT;
        }

        for (FitAttrState attrState : values()) {
            if (attrState.state == attr.fitStateInPC()) {
                return attrState;
            }
        }

        return DEFAULT;
    }

    public int getState() {
        return this.state;
    }


    public String description() {
        return StringUtils.EMPTY;
    }

    public Item propertyItem() {
        return new Item(this.description(), this.getState());
    }

}
