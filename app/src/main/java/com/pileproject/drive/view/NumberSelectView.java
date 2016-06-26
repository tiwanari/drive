/*
 * Copyright (C) 2011-2015 PILE Project, Inc. <dev@pileproject.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pileproject.drive.view;

import android.content.Context;
import android.widget.LinearLayout;

import com.pileproject.drive.util.Range;

public abstract class NumberSelectView extends LinearLayout {

    final protected Range<Double> mRange;
    protected Context mContext;

    public NumberSelectView(Context context, Range<Double> range) {
        super(context);

        mContext = context;
        mRange = range;
    }

    public abstract void setNum(int num);

    public abstract double getSelectedNum();
}