package com.shykun.volodymyr.videoeditor.fragment

import com.shykun.volodymyr.videoeditor.usecase.CutUseCase

class CutFragment : RangeSeekBarFragment() {
    override val confirmAction = {
        CutUseCase(mainViewModel.selectedVideoUri.value!!, context!!).execute(curStartValue, curEndValue)
    }
}