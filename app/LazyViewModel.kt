package com.example.slotmachine

import androidx.lifecycle.ViewModel
import com.example.lazycolrows.R

class LazyViewModel : ViewModel() {
    val imageResIds: List<Int> = listOf(
        R.drawable.cherry,
        R.drawable.grape,
        R.drawable.pear,
        R.drawable.strawberry,
    )
}