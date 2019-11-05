package com.zrp.latte.ui.launcher;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;

public class LauncherHolderCreator implements CBViewHolderCreator<LuncherHolder> {

    @Override
    public LuncherHolder createHolder() {
        return new LuncherHolder();
    }
}
