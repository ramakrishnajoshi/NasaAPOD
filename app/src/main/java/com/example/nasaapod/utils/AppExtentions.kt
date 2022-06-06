package com.example.nasaapod.utils

import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker

fun FragmentManager.showCalender(onDatePicked: (Long) -> Unit) {
    getCalender().also {
        it.addOnPositiveButtonClickListener(onDatePicked)
    }.show(this, "tag")
}

fun getCalender() = MaterialDatePicker.Builder.datePicker()
    .setTitleText("Choose End Date")
    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
    .setCalendarConstraints(
        CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now()).build()
    )
    .build()


